package com.atlas.logger.ui.settings

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.atlas.logger.ui.theme.*
import com.atlas.logger.util.*

@Composable
fun SettingsScreen(
    locale: AppLocale,
    weightUnit: WeightUnit,
    onLocaleChange: (AppLocale) -> Unit,
    onWeightUnitChange: (WeightUnit) -> Unit,
    onBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // File picker for import
    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.importData(it, locale) }
    }

    // Show snackbar
    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearSnackbar()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = AtlasBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // ═══ Top Bar ═══
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AtlasSurfaceVariant.copy(alpha = 0.6f))
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = AtlasTextPrimary)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = AtlasStrings.projectAtlas(locale),
                    style = MaterialTheme.typography.titleLarge,
                    color = AtlasPrimary,
                    fontWeight = FontWeight.Black,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                    letterSpacing = (-1).sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ═══ Language ═══
            SettingsSection(title = AtlasStrings.language(locale)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SettingsOptionButton(
                        label = "English",
                        isSelected = locale == AppLocale.ENGLISH,
                        onClick = { onLocaleChange(AppLocale.ENGLISH) },
                        modifier = Modifier.weight(1f)
                    )
                    SettingsOptionButton(
                        label = "العربية",
                        isSelected = locale == AppLocale.ARABIC,
                        onClick = { onLocaleChange(AppLocale.ARABIC) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // ═══ Image Prompt ═══
            val clipboardManager = androidx.compose.ui.platform.LocalClipboardManager.current
            val promptText = AtlasStrings.imagePromptText()
            
            SettingsSection(title = AtlasStrings.imagePromptTitle(locale)) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AtlasSurface, MaterialTheme.shapes.medium)
                        .border(1.dp, AtlasBorder, MaterialTheme.shapes.medium)
                        .padding(12.dp)
                ) {
                    Text(
                        text = promptText,
                        style = MaterialTheme.typography.bodySmall,
                        color = AtlasTextSecondary,
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { 
                            clipboardManager.setText(androidx.compose.ui.text.AnnotatedString(promptText))
                        },
                        modifier = Modifier.align(Alignment.End),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AtlasPrimary.copy(alpha = 0.1f),
                            contentColor = AtlasPrimary
                        ),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                        shape = MaterialTheme.shapes.small
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(if (locale == AppLocale.ARABIC) "نسخ" else "Copy", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = AtlasBorder, modifier = Modifier.padding(horizontal = 20.dp))
            Spacer(modifier = Modifier.height(8.dp))

            // ═══ Export/Import ═══
            SettingsActionItem(
                icon = Icons.Default.Upload,
                label = AtlasStrings.exportData(locale),
                onClick = { viewModel.exportData(locale) }
            )

            SettingsActionItem(
                icon = Icons.Default.Download,
                label = AtlasStrings.importData(locale),
                onClick = { importLauncher.launch("application/json") }
            )

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = AtlasBorder, modifier = Modifier.padding(horizontal = 20.dp))
            Spacer(modifier = Modifier.height(16.dp))

            // ═══ About ═══
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${AtlasStrings.version(locale)} 1.0.0",
                    style = MaterialTheme.typography.bodySmall,
                    color = AtlasTextSecondary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = AtlasStrings.developedBy(locale),
                    style = MaterialTheme.typography.bodySmall,
                    color = AtlasTextSecondary.copy(alpha = 0.5f),
                    letterSpacing = 1.sp
                )
            }

            // ═══ Danger Zone ═══
            Spacer(modifier = Modifier.height(24.dp))
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(
                    text = if (locale == AppLocale.ARABIC) "منطقة الخطر" else "Danger Zone",
                    style = MaterialTheme.typography.titleSmall,
                    color = Color(0xFFFF6B6B),
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                var showClearHistoryDialog by remember { mutableStateOf(false) }
                
                if (showClearHistoryDialog) {
                    AlertDialog(
                        onDismissRequest = { showClearHistoryDialog = false },
                        title = { Text(AtlasStrings.clearHistoryTitle(locale)) },
                        text = { Text(AtlasStrings.clearHistoryConfirm(locale)) },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    viewModel.clearAllHistory()
                                    showClearHistoryDialog = false
                                },
                                colors = ButtonDefaults.textButtonColors(contentColor = AtlasError)
                            ) {
                                Text(AtlasStrings.clearHistoryAction(locale))
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showClearHistoryDialog = false }) {
                                Text(AtlasStrings.cancel(locale), color = AtlasTextSecondary)
                            }
                        },
                        containerColor = AtlasBackground,
                        titleContentColor = AtlasTextPrimary,
                        textContentColor = AtlasTextSecondary
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFFF6B6B).copy(alpha = 0.1f), MaterialTheme.shapes.medium)
                        .border(1.dp, Color(0xFFFF6B6B).copy(alpha = 0.3f), MaterialTheme.shapes.medium)
                        .clickable { showClearHistoryDialog = true }
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = AtlasStrings.clearHistoryAction(locale),
                        style = MaterialTheme.typography.labelLarge,
                        color = Color(0xFFFF6B6B),
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = AtlasTextSecondary,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.5.sp
        )
        Spacer(modifier = Modifier.height(12.dp))
        content()
    }
}

@Composable
private fun SettingsOptionButton(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(48.dp)
            .background(
                if (isSelected) AtlasPrimary.copy(alpha = 0.15f) else AtlasSurface,
                MaterialTheme.shapes.medium
            )
            .border(
                1.dp,
                if (isSelected) AtlasPrimary else AtlasBorder,
                MaterialTheme.shapes.medium
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = if (isSelected) AtlasPrimary else AtlasTextSecondary,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}

@Composable
private fun SettingsActionItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = AtlasSecondary,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = AtlasTextPrimary
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            Icons.Default.ChevronRight,
            contentDescription = null,
            tint = AtlasTextSecondary.copy(alpha = 0.4f),
            modifier = Modifier.size(20.dp)
        )
    }
}
