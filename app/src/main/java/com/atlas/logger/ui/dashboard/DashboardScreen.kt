package com.atlas.logger.ui.dashboard

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.atlas.logger.ui.dashboard.components.RecentActivityCard
import com.atlas.logger.ui.dashboard.components.VolumePulseGraph
import com.atlas.logger.ui.theme.*
import com.atlas.logger.util.*

@Composable
fun DashboardScreen(
    locale: AppLocale,
    weightUnit: WeightUnit,
    onLocaleChange: (AppLocale) -> Unit,
    onWeightUnitChange: (WeightUnit) -> Unit,
    onStartWorkout: () -> Unit,
    onViewSession: (Long) -> Unit,
    onOpenSettings: () -> Unit,
    onViewAllHistory: () -> Unit = {},
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isRtl = locale == AppLocale.ARABIC

    Box(modifier = Modifier.fillMaxSize().background(AtlasBackground)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 120.dp)
        ) {
            // ═══ Top App Bar ═══
            item {
                TopBar(
                    locale = locale,
                    weightUnit = weightUnit,
                    onLocaleChange = onLocaleChange,
                    onWeightUnitChange = onWeightUnitChange,
                    onOpenSettings = onOpenSettings,
                    isRtl = isRtl
                )
            }

            // ═══ Welcome Message ═══
            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
                    Text(
                        text = if (uiState.recentSessions.isEmpty()) AtlasStrings.welcome(locale) else AtlasStrings.welcomeBack(locale),
                        style = MaterialTheme.typography.titleMedium,
                        color = AtlasTextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = AtlasStrings.readyForCycle(locale),
                        style = MaterialTheme.typography.bodyMedium,
                        color = AtlasTextSecondary
                    )
                }
            }

            // ═══ Volume Pulse Graph ═══
            item {
                VolumePulseGraph(
                    weeklyVolumes = uiState.weeklyVolumes,
                    currentWeekVolume = uiState.currentWeekVolume,
                    changePercent = uiState.volumeChangePercent,
                    locale = locale,
                    weightUnit = weightUnit,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }

            // ═══ Recent Activity Header ═══
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = AtlasStrings.recentActivity(locale),
                        style = MaterialTheme.typography.titleLarge,
                        color = AtlasTextPrimary
                    )
                    Text(
                        text = AtlasStrings.viewAll(locale),
                        style = MaterialTheme.typography.labelMedium,
                        color = AtlasSecondary,
                        modifier = Modifier.clickable { onViewAllHistory() }
                    )
                }
            }

            // ═══ Recent Activity Cards or Empty State ═══
            if (uiState.recentSessions.isEmpty()) {
                item {
                    // Ghost empty state
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .height(120.dp)
                            .background(AtlasSurface, MaterialTheme.shapes.large)
                            .border(1.dp, AtlasBorder.copy(alpha = 0.3f), MaterialTheme.shapes.large),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = AtlasStrings.noWorkoutsYet(locale),
                            style = MaterialTheme.typography.bodyMedium,
                            color = AtlasTextSecondary.copy(alpha = 0.5f),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            } else {
                items(uiState.recentSessions) { session ->
                    var showDeleteDialog by remember { mutableStateOf(false) }

                    if (showDeleteDialog) {
                        AlertDialog(
                            onDismissRequest = { showDeleteDialog = false },
                            title = { Text(AtlasStrings.deleteSessionTitle(locale)) },
                            text = { Text(AtlasStrings.deleteSessionConfirm(locale)) },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        viewModel.deleteSession(session.id)
                                        showDeleteDialog = false
                                    },
                                    colors = ButtonDefaults.textButtonColors(contentColor = AtlasError)
                                ) {
                                    Text(AtlasStrings.delete(locale))
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = { showDeleteDialog = false }) {
                                    Text(AtlasStrings.cancel(locale), color = AtlasTextSecondary)
                                }
                            },
                            containerColor = AtlasBackground,
                            titleContentColor = AtlasTextPrimary,
                            textContentColor = AtlasTextSecondary
                        )
                    }

                    RecentActivityCard(
                        session = session,
                        locale = locale,
                        weightUnit = weightUnit,
                        onClick = {
                            onViewSession(session.id)
                        },
                        onDelete = { showDeleteDialog = true },
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun TopBar(
    locale: AppLocale,
    weightUnit: WeightUnit,
    onLocaleChange: (AppLocale) -> Unit,
    onWeightUnitChange: (WeightUnit) -> Unit,
    onOpenSettings: () -> Unit,
    isRtl: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(AtlasSurfaceVariant.copy(alpha = 0.6f))
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (!isRtl) {
            // LTR: Profile on Left
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Profile circle
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(AtlasSurface)
                        .border(1.dp, AtlasBorder, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("👤", fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = AtlasStrings.projectAtlas(locale),
                    style = MaterialTheme.typography.titleLarge,
                    color = AtlasPrimary,
                    fontWeight = FontWeight.Black,
                    fontStyle = FontStyle.Italic,
                    letterSpacing = (-1).sp
                )
            }
        }

        // Toggles & Settings
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            // Language Toggle
            TogglePill(
                options = listOf("EN", "AR"),
                selectedIndex = if (locale == AppLocale.ENGLISH) 0 else 1,
                onSelect = { i -> onLocaleChange(if (i == 0) AppLocale.ENGLISH else AppLocale.ARABIC) },
                accentColor = AtlasPrimary
            )


            // Settings
            IconButton(onClick = onOpenSettings, modifier = Modifier.size(36.dp)) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = AtlasPrimary,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        if (isRtl) {
            // RTL: Profile on Right
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = AtlasStrings.projectAtlas(locale),
                    style = MaterialTheme.typography.titleLarge,
                    color = AtlasPrimary,
                    fontWeight = FontWeight.Black,
                    fontStyle = FontStyle.Italic,
                    letterSpacing = (-1).sp
                )
                Spacer(modifier = Modifier.width(10.dp))
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(AtlasSurface)
                        .border(1.dp, AtlasBorder, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("👤", fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
fun TogglePill(
    options: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(AtlasSurface, MaterialTheme.shapes.small)
            .border(1.dp, AtlasBorder, MaterialTheme.shapes.small)
            .padding(2.dp)
    ) {
        options.forEachIndexed { i, label ->
            val isSelected = i == selectedIndex
            Box(
                modifier = Modifier
                    .clickable { onSelect(i) }
                    .background(
                        if (isSelected) accentColor else Color.Transparent,
                        MaterialTheme.shapes.extraSmall
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isSelected) AtlasTextOnPrimary else AtlasTextSecondary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
