package com.smilestudio.android

import android.content.Context
import android.net.Uri
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.smilestudio.android.apikey.AndroidKeystoreApiKeyEncryptor
import com.smilestudio.android.apikey.ApiKeyStore
import com.smilestudio.android.apikey.SharedPreferencesKeyValueStore
import com.smilestudio.android.recognition.AndroidImageResizer
import com.smilestudio.android.recognition.FirebaseCloudRecognizer
import com.smilestudio.android.recognition.ImageRecognitionCoordinator
import com.smilestudio.android.recognition.ImageRecognitionOutcome
import com.smilestudio.vision.recognizeStructure

private const val API_KEY_PREFS_NAME = "api_key_prefs"

private const val IMAGE_READ_FAILURE_MESSAGE = "画像の読み込みに失敗しました"
private const val REMAINING_FREE_COUNT_MESSAGE_FORMAT = "あと%d回無料でご利用いただけます"

enum class AppTab(val label: String) {
    HOME("ホーム"),
    HOW_TO("使い方"),
}

class SmilesStudioAppState(
    private val apiKeyStore: ApiKeyStore,
    private val recognitionCoordinator: ImageRecognitionCoordinator,
    private val showSnackbar: suspend (String) -> Unit,
) {
    var selectedTab by mutableStateOf(AppTab.HOME)
    var smilesText by mutableStateOf("")
    var menuExpanded by mutableStateOf(false)
    var showAboutDialog by mutableStateOf(false)
    var showApiKeyDialog by mutableStateOf(false)
    var showPaywall by mutableStateOf(false)
        private set
    var currentApiKey by mutableStateOf(apiKeyStore.get())
        private set
    var showImageSourceSheet by mutableStateOf(false)
    var isRecognizing by mutableStateOf(false)
        private set
    var pendingCaptureUri by mutableStateOf<Uri?>(null)

    fun saveApiKey(apiKey: String) {
        apiKeyStore.save(apiKey)
        currentApiKey = apiKeyStore.get()
    }

    fun deleteApiKey() {
        apiKeyStore.delete()
        currentApiKey = null
    }

    fun dismissPaywall() {
        showPaywall = false
    }

    suspend fun runRecognition(imageBytes: ByteArray?) {
        if (imageBytes == null) {
            showSnackbar(IMAGE_READ_FAILURE_MESSAGE)
            return
        }
        isRecognizing = true
        val outcome = try {
            recognitionCoordinator.recognize(imageBytes)
        } finally {
            isRecognizing = false
        }
        when (outcome) {
            is ImageRecognitionOutcome.Recognized -> {
                smilesText = outcome.smiles
                val remaining = outcome.remainingFreeCount
                if (remaining != null) {
                    showSnackbar(REMAINING_FREE_COUNT_MESSAGE_FORMAT.format(remaining))
                }
            }
            is ImageRecognitionOutcome.Failed -> showSnackbar(outcome.reason)
            ImageRecognitionOutcome.FreeTierExhausted -> showPaywall = true
        }
    }
}

@Composable
fun rememberSmilesStudioAppState(snackbarHostState: SnackbarHostState): SmilesStudioAppState {
    val context = LocalContext.current
    val apiKeyStore = remember {
        ApiKeyStore(
            encryptor = AndroidKeystoreApiKeyEncryptor(),
            store = SharedPreferencesKeyValueStore(
                context.getSharedPreferences(API_KEY_PREFS_NAME, Context.MODE_PRIVATE),
            ),
        )
    }
    val recognitionCoordinator = remember {
        ImageRecognitionCoordinator(
            getApiKey = apiKeyStore::get,
            resizeImage = AndroidImageResizer::resize,
            recognize = ::recognizeStructure,
            recognizeViaCloud = FirebaseCloudRecognizer::recognize,
        )
    }
    return remember {
        SmilesStudioAppState(
            apiKeyStore = apiKeyStore,
            recognitionCoordinator = recognitionCoordinator,
            showSnackbar = { message -> snackbarHostState.showSnackbar(message) },
        )
    }
}
