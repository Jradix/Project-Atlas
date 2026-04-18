package com.atlas.logger.ui.summary

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.atlas.logger.domain.model.PRType
import com.atlas.logger.domain.model.PersonalRecord
import com.atlas.logger.ui.theme.*
import com.atlas.logger.util.*

@Composable
fun WorkoutSummaryScreen(
    sessionId: Long,
    locale: AppLocale,
    weightUnit: WeightUnit,
    onFinish: () -> Unit,
    onStartWorkout: () -> Unit,
    viewModel: WorkoutSummaryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val session = uiState.session

    // Entrance animation
    val scale = remember { Animatable(0.8f) }
    LaunchedEffect(Unit) {
        scale.animateTo(1f, animationSpec = spring(dampingRatio = 0.6f, stiffness = 300f))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AtlasBackground)
            .verticalScroll(rememberScrollState())
            .statusBarsPadding()
            .padding(24.dp)
            .scale(scale.value),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // ═══ Completion Badge ═══
        Box(
            modifier = Modifier
                .size(72.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(AtlasPrimary, AtlasPrimary.copy(alpha = 0.3f))
                    ),
                    shape = androidx.compose.foundation.shape.CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Check,
                contentDescription = "Complete",
                tint = AtlasTextOnPrimary,
                modifier = Modifier.size(36.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ═══ "WORKOUT COMPLETE" Title ═══
        Text(
            text = AtlasStrings.workoutComplete(locale),
            style = MaterialTheme.typography.displaySmall,
            color = AtlasPrimary,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center,
            lineHeight = 38.sp
        )

        if (session != null) {
            Spacer(modifier = Modifier.height(8.dp))

            // Date and duration subtitle
            Text(
                text = "${formatDate(session.date, locale)} • ${session.durationSeconds / 60} ${AtlasStrings.min(locale)}",
                style = MaterialTheme.typography.bodyMedium,
                color = AtlasTextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            // ═══ Total Volume ═══
            Text(
                text = AtlasStrings.totalVolume(locale),
                style = MaterialTheme.typography.labelMedium,
                color = AtlasTextSecondary,
                letterSpacing = 3.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = UnitConverter.formatWeight(session.totalVolume, weightUnit).let {
                    // Format as a big number (e.g. 14,250)
                    val value = if (weightUnit == WeightUnit.LBS) UnitConverter.kgToLbs(session.totalVolume) else session.totalVolume
                    "%,.0f".format(value)
                },
                style = MaterialTheme.typography.displayLarge.copy(fontSize = 64.sp),
                color = AtlasPrimary,
                fontWeight = FontWeight.Black,
                letterSpacing = (-3).sp
            )
            Text(
                text = if (weightUnit == WeightUnit.KG) AtlasStrings.kgMoved(locale) else AtlasStrings.lbsMoved(locale),
                style = MaterialTheme.typography.labelMedium,
                color = AtlasPrimary.copy(alpha = 0.7f),
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // ═══ Stats Grid ═══
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    icon = Icons.Default.Timer,
                    label = AtlasStrings.duration(locale),
                    value = formatDuration(session.durationSeconds),
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    icon = Icons.Default.LocalFireDepartment,
                    label = AtlasStrings.calories(locale),
                    value = uiState.estimatedCalories.toString(),
                    valueColor = AtlasWarning,
                    modifier = Modifier.weight(1f)
                )
            }

            // ═══ Personal Records ═══
            if (uiState.personalRecords.isNotEmpty()) {
                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        Icons.Default.EmojiEvents,
                        contentDescription = null,
                        tint = AtlasPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = AtlasStrings.newRecords(locale),
                        style = MaterialTheme.typography.titleMedium,
                        color = AtlasTextPrimary,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                uiState.personalRecords.forEach { pr ->
                    PRCard(pr = pr, locale = locale, weightUnit = weightUnit)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
            // ═══ Exercises Breakdown ═══
            if (uiState.exercises.isNotEmpty()) {
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = if (locale == AppLocale.ARABIC) "تفاصيل التمارين" else "EXERCISES",
                    style = MaterialTheme.typography.titleMedium,
                    color = AtlasTextPrimary,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    modifier = Modifier.align(Alignment.Start)
                )
                Spacer(modifier = Modifier.height(12.dp))

                uiState.exercises.forEach { workoutExercise ->
                    val exName = if (locale == AppLocale.ARABIC) workoutExercise.exercise.nameAr.ifEmpty { workoutExercise.exercise.name } else workoutExercise.exercise.name.ifEmpty { "Exercise" }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(AtlasSurface, MaterialTheme.shapes.medium)
                            .border(1.dp, AtlasBorder, MaterialTheme.shapes.medium)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = exName,
                            style = MaterialTheme.typography.bodyMedium,
                            color = AtlasPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        workoutExercise.sets.forEach { set ->
                            val weightVal = if (weightUnit == WeightUnit.LBS) UnitConverter.kgToLbs(set.weight) else set.weight
                            val weightStr = if (weightVal > 0) UnitConverter.formatWeight(set.weight, weightUnit) else "0"
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = if (locale == AppLocale.ARABIC) "مجموعة ${set.setOrder + 1}" else "Set ${set.setOrder + 1}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = AtlasTextSecondary
                                )
                                Text(
                                    text = "$weightStr ${UnitConverter.unitLabel(weightUnit)} × ${set.reps}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = AtlasTextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

        }

        Spacer(modifier = Modifier.height(40.dp))
        Spacer(modifier = Modifier.weight(1f))

        val context = androidx.compose.ui.platform.LocalContext.current

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(
                onClick = {
                    viewModel.repeatSession {
                        onStartWorkout()
                    }
                },
                modifier = Modifier.weight(1f).height(56.dp),
                border = BorderStroke(1.dp, AtlasPrimary),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = AtlasPrimary)
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (locale == AppLocale.ARABIC) "تكرار الجلسة" else "REPEAT",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

            OutlinedButton(
                onClick = {
                    if (session != null) {
                        com.atlas.logger.util.ShareUtils.shareWorkout(context, uiState, locale, weightUnit)
                    }
                },
                modifier = Modifier.size(56.dp),
                border = BorderStroke(1.dp, AtlasBorder),
                contentPadding = PaddingValues(0.dp)
            ) {
                Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(20.dp), tint = AtlasTextPrimary)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onFinish,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AtlasBackground,
                contentColor = AtlasTextPrimary
            ),
            shape = MaterialTheme.shapes.large,
            border = BorderStroke(1.dp, AtlasBorder)
        ) {
            Text(
                text = if (locale == AppLocale.ARABIC) "إغلاق" else "CLOSE",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun StatCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    valueColor: Color = AtlasTextPrimary,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(AtlasSurface, MaterialTheme.shapes.large)
            .border(1.dp, AtlasBorder, MaterialTheme.shapes.large)
            .padding(16.dp)
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = AtlasSecondary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = label.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = AtlasTextSecondary,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.headlineMedium,
            color = valueColor,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun PRCard(
    pr: PersonalRecord,
    locale: AppLocale,
    weightUnit: WeightUnit
) {
    val name = if (locale == AppLocale.ARABIC) pr.exerciseNameAr else pr.exerciseName
    val prLabel = when (pr.type) {
        PRType.WEIGHT_PR -> AtlasStrings.weightPR(locale)
        PRType.VOLUME_PR -> AtlasStrings.volumePR(locale)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(AtlasSurface, MaterialTheme.shapes.large)
            .border(1.dp, AtlasPrimary.copy(alpha = 0.2f), MaterialTheme.shapes.large)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(AtlasSurfaceVariant, MaterialTheme.shapes.medium),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    if (pr.type == PRType.WEIGHT_PR) Icons.Default.EmojiEvents else Icons.Default.TrendingUp,
                    contentDescription = null,
                    tint = AtlasPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = name.uppercase(),
                    style = MaterialTheme.typography.titleSmall,
                    color = AtlasTextPrimary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = prLabel,
                    style = MaterialTheme.typography.labelSmall,
                    color = AtlasTextSecondary
                )
            }
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = UnitConverter.formatWeight(pr.value, weightUnit),
                style = MaterialTheme.typography.headlineSmall,
                color = AtlasPrimary,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = UnitConverter.unitLabel(weightUnit),
                style = MaterialTheme.typography.labelSmall,
                color = AtlasTextSecondary
            )
        }
    }
}

private fun formatDuration(seconds: Int): String {
    val h = seconds / 3600
    val m = (seconds % 3600) / 60
    val s = seconds % 60
    return if (h > 0) "%d:%02d:%02d".format(h, m, s) else "%d:%02d".format(m, s)
}

private fun formatDate(timestamp: Long, locale: AppLocale): String {
    val format = java.text.SimpleDateFormat(
        "EEEE, dd MMM",
        if (locale == AppLocale.ARABIC) java.util.Locale("ar") else java.util.Locale.ENGLISH
    )
    return format.format(java.util.Date(timestamp)).uppercase()
}
