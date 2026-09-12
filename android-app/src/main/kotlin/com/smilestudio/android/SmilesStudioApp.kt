package com.smilestudio.android

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.smilestudio.android.apikey.AndroidKeystoreApiKeyEncryptor
import com.smilestudio.android.apikey.ApiKeyStore
import com.smilestudio.android.apikey.SharedPreferencesKeyValueStore
import com.smilestudio.android.theme.expressivePressScale

private const val API_KEY_PREFS_NAME = "api_key_prefs"

private enum class AppTab(val label: String) {
    HOME("ホーム"),
    HOW_TO("使い方"),
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SmilesStudioApp() {
    var selectedTab by remember { mutableStateOf(AppTab.HOME) }
    var smilesText by remember { mutableStateOf("") }
    var menuExpanded by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }
    var showApiKeyDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val apiKeyStore = remember {
        ApiKeyStore(
            encryptor = AndroidKeystoreApiKeyEncryptor(),
            store = SharedPreferencesKeyValueStore(
                context.getSharedPreferences(API_KEY_PREFS_NAME, Context.MODE_PRIVATE),
            ),
        )
    }
    var currentApiKey by remember { mutableStateOf(apiKeyStore.get()) }

    val menuButtonInteractionSource = remember { MutableInteractionSource() }
    val clearItemInteractionSource = remember { MutableInteractionSource() }
    val aboutItemInteractionSource = remember { MutableInteractionSource() }
    val apiKeyItemInteractionSource = remember { MutableInteractionSource() }
    val homeTabInteractionSource = remember { MutableInteractionSource() }
    val howToTabInteractionSource = remember { MutableInteractionSource() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("SmilesStudio", style = MaterialTheme.typography.titleLarge) },
                actions = {
                    IconButton(
                        onClick = { menuExpanded = true },
                        interactionSource = menuButtonInteractionSource,
                        modifier = Modifier.size(48.dp).expressivePressScale(menuButtonInteractionSource),
                    ) {
                        Icon(Icons.Rounded.MoreVert, contentDescription = "メニュー")
                    }
                    DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                        if (selectedTab == AppTab.HOME) {
                            DropdownMenuItem(
                                text = { Text("入力をクリア") },
                                onClick = {
                                    smilesText = ""
                                    menuExpanded = false
                                },
                                interactionSource = clearItemInteractionSource,
                                modifier = Modifier.expressivePressScale(clearItemInteractionSource),
                            )
                        }
                        DropdownMenuItem(
                            text = { Text("このアプリについて") },
                            onClick = {
                                menuExpanded = false
                                showAboutDialog = true
                            },
                            interactionSource = aboutItemInteractionSource,
                            modifier = Modifier.expressivePressScale(aboutItemInteractionSource),
                        )
                        DropdownMenuItem(
                            text = { Text("APIキー設定") },
                            onClick = {
                                menuExpanded = false
                                showApiKeyDialog = true
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
                    selected = selectedTab == AppTab.HOME,
                    onClick = { selectedTab = AppTab.HOME },
                    icon = { Icon(Icons.Rounded.Home, contentDescription = null) },
                    label = { Text(AppTab.HOME.label, style = MaterialTheme.typography.labelMedium) },
                    interactionSource = homeTabInteractionSource,
                    modifier = Modifier.expressivePressScale(homeTabInteractionSource),
                )
                NavigationBarItem(
                    selected = selectedTab == AppTab.HOW_TO,
                    onClick = { selectedTab = AppTab.HOW_TO },
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
            targetState = selectedTab,
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
                    smilesText = smilesText,
                    onSmilesTextChange = { smilesText = it },
                    modifier = Modifier.fillMaxSize(),
                )
                AppTab.HOW_TO -> HowToContent(modifier = Modifier.fillMaxSize())
            }
        }
    }

    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            confirmButton = {
                TextButton(onClick = { showAboutDialog = false }) { Text("閉じる") }
            },
            title = { Text("SmilesStudioについて") },
            text = { Text("SMILES記法をパースし、構造式をCanvasに描画するツールです。") },
        )
    }

    if (showApiKeyDialog) {
        ApiKeySettingsDialog(
            currentApiKey = currentApiKey,
            onSave = { apiKey ->
                apiKeyStore.save(apiKey)
                currentApiKey = apiKeyStore.get()
            },
            onDelete = {
                apiKeyStore.delete()
                currentApiKey = null
            },
            onDismiss = { showApiKeyDialog = false },
        )
    }
}
