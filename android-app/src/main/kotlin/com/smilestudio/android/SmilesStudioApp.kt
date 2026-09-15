package com.smilestudio.android

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.QuestionMark
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.smilestudio.android.recognition.createCaptureImageUri
import com.smilestudio.android.recognition.readImageBytes
import com.smilestudio.android.subscription.SubscriptionPaywall
import com.smilestudio.android.theme.expressivePressScale
import kotlinx.coroutines.launch

private const val PRIVACY_POLICY_URL = "https://itisnomatter.github.io/SmilesStudio/privacy-policy/"

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SmilesStudioApp() {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val appState = rememberSmilesStudioAppState(snackbarHostState)

    fun runRecognition(imageUri: Uri) {
        coroutineScope.launch {
            appState.runRecognition(readImageBytes(context, imageUri))
        }
    }

    val takePictureLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        val uri = appState.pendingCaptureUri
        if (success && uri != null) {
            runRecognition(uri)
        }
    }
    val pickImageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            runRecognition(uri)
        }
    }

    val menuButtonInteractionSource = remember { MutableInteractionSource() }
    val clearItemInteractionSource = remember { MutableInteractionSource() }
    val aboutItemInteractionSource = remember { MutableInteractionSource() }
    val apiKeyItemInteractionSource = remember { MutableInteractionSource() }
    val homeTabInteractionSource = remember { MutableInteractionSource() }
    val howToTabInteractionSource = remember { MutableInteractionSource() }

    Box(modifier = Modifier.fillMaxSize()) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("SmilesStudio", style = MaterialTheme.typography.titleLarge) },
                actions = {
                    IconButton(
                        onClick = { appState.menuExpanded = true },
                        interactionSource = menuButtonInteractionSource,
                        modifier = Modifier.size(48.dp).expressivePressScale(menuButtonInteractionSource),
                    ) {
                        Icon(Icons.Rounded.MoreVert, contentDescription = "メニュー")
                    }
                    DropdownMenu(
                        expanded = appState.menuExpanded,
                        onDismissRequest = { appState.menuExpanded = false },
                    ) {
                        if (appState.selectedTab == AppTab.HOME) {
                            DropdownMenuItem(
                                text = { Text("入力をクリア") },
                                onClick = {
                                    appState.smilesText = ""
                                    appState.menuExpanded = false
                                },
                                interactionSource = clearItemInteractionSource,
                                modifier = Modifier.expressivePressScale(clearItemInteractionSource),
                            )
                        }
                        DropdownMenuItem(
                            text = { Text("このアプリについて") },
                            onClick = {
                                appState.menuExpanded = false
                                appState.showAboutDialog = true
                            },
                            interactionSource = aboutItemInteractionSource,
                            modifier = Modifier.expressivePressScale(aboutItemInteractionSource),
                        )
                        DropdownMenuItem(
                            text = { Text("APIキー設定") },
                            onClick = {
                                appState.menuExpanded = false
                                appState.showApiKeyDialog = true
                            },
                            interactionSource = apiKeyItemInteractionSource,
                            modifier = Modifier.expressivePressScale(apiKeyItemInteractionSource),
                        )
                    }
                },
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = appState.selectedTab == AppTab.HOME,
                    onClick = { appState.selectedTab = AppTab.HOME },
                    icon = { Icon(Icons.Rounded.Home, contentDescription = null) },
                    label = { Text(AppTab.HOME.label, style = MaterialTheme.typography.labelMedium) },
                    interactionSource = homeTabInteractionSource,
                    modifier = Modifier.expressivePressScale(homeTabInteractionSource),
                )
                NavigationBarItem(
                    selected = appState.selectedTab == AppTab.HOW_TO,
                    onClick = { appState.selectedTab = AppTab.HOW_TO },
                    icon = { Icon(Icons.Rounded.QuestionMark, contentDescription = null) },
                    label = { Text(AppTab.HOW_TO.label, style = MaterialTheme.typography.labelMedium) },
                    interactionSource = howToTabInteractionSource,
                    modifier = Modifier.expressivePressScale(howToTabInteractionSource),
                )
            }
        },
    ) { innerPadding ->
        val enterAlphaSpec = MaterialTheme.motionScheme.defaultEffectsSpec<Float>()
        val enterOffsetSpec = MaterialTheme.motionScheme.defaultSpatialSpec<IntOffset>()
        AnimatedContent(
            targetState = appState.selectedTab,
            modifier = Modifier.padding(innerPadding),
            transitionSpec = {
                (fadeIn(animationSpec = enterAlphaSpec) +
                    slideInVertically(animationSpec = enterOffsetSpec) { it / 8 })
                    .togetherWith(fadeOut(animationSpec = tween(durationMillis = 120)))
            },
            label = "tabContent",
        ) { tab ->
            when (tab) {
                AppTab.HOME -> HomeContent(
                    smilesText = appState.smilesText,
                    onSmilesTextChange = { appState.smilesText = it },
                    onImageRecognitionClick = { appState.showImageSourceSheet = true },
                    modifier = Modifier.fillMaxSize(),
                )
                AppTab.HOW_TO -> HowToContent(modifier = Modifier.fillMaxSize())
            }
        }
    }

    if (appState.showAboutDialog) {
        AlertDialog(
            onDismissRequest = { appState.showAboutDialog = false },
            confirmButton = {
                TextButton(onClick = { appState.showAboutDialog = false }) { Text("閉じる") }
            },
            title = { Text("SmilesStudioについて") },
            text = {
                Column {
                    Text("SMILES記法をパースし、構造式をCanvasに描画するツールです。")
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "プライバシーポリシー",
                        color = MaterialTheme.colorScheme.primary,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable {
                            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(PRIVACY_POLICY_URL)))
                        },
                    )
                }
            },
        )
    }

    if (appState.showApiKeyDialog) {
        ApiKeySettingsDialog(
            currentApiKey = appState.currentApiKey,
            onSave = appState::saveApiKey,
            onDelete = appState::deleteApiKey,
            onDismiss = { appState.showApiKeyDialog = false },
        )
    }

    if (appState.showPaywall) {
        Dialog(
            onDismissRequest = { appState.dismissPaywall() },
            properties = DialogProperties(usePlatformDefaultWidth = false),
        ) {
            SubscriptionPaywall(onDismiss = { appState.dismissPaywall() })
        }
    }

    if (appState.showImageSourceSheet) {
        ImageSourceBottomSheet(
            onTakePhoto = {
                appState.showImageSourceSheet = false
                val uri = createCaptureImageUri(context)
                appState.pendingCaptureUri = uri
                takePictureLauncher.launch(uri)
            },
            onPickFromGallery = {
                appState.showImageSourceSheet = false
                pickImageLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            },
            onDismiss = { appState.showImageSourceSheet = false },
        )
    }

    if (appState.isRecognizing) {
        RecognitionLoadingOverlay()
    }
    }
}
