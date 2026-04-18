package com.atlas.logger.ui.dashboard.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.atlas.logger.domain.model.WeeklyVolume
import com.atlas.logger.ui.theme.*
import com.atlas.logger.util.*

@Composable
fun VolumePulseGraph(
    weeklyVolumes: List<WeeklyVolume>,
    currentWeekVolume: Double,
    changePercent: Int,
    locale: AppLocale,
    weightUnit: WeightUnit,
    modifier: Modifier = Modifier
) {
    val isEmpty = weeklyVolumes.isEmpty() || weeklyVolumes.all { it.totalVolume == 0.0 }

    // Animation
    val animatedProgress = remember { Animatable(0f) }
    LaunchedEffect(weeklyVolumes) {
        animatedProgress.snapTo(0f)
        animatedProgress.animateTo(1f, animationSpec = tween(1000, easing = EaseOutCubic))
    }

    Column(modifier = modifier) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = AtlasStrings.volumePulse(locale),
                style = MaterialTheme.typography.titleLarge,
                color = AtlasTextPrimary
            )
            Text(
                text = AtlasStrings.pastWeeks(locale),
                style = MaterialTheme.typography.labelSmall,
                color = AtlasTextSecondary,
                letterSpacing = 2.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Graph Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(AtlasSurface, MaterialTheme.shapes.large)
                .border(1.dp, AtlasBorder, MaterialTheme.shapes.large)
                .padding(20.dp)
        ) {
            // Blue gradient overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color.Transparent, AtlasSecondaryGlow.copy(alpha = 0.1f))
                        )
                    )
            )

            Column(modifier = Modifier.fillMaxSize()) {
                // Volume + Change badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = UnitConverter.formatVolume(currentWeekVolume, weightUnit),
                            style = MaterialTheme.typography.displaySmall,
                            color = AtlasSecondary
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${UnitConverter.unitLabel(weightUnit)} Total",
                            style = MaterialTheme.typography.labelSmall,
                            color = AtlasTextSecondary,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                    }
                    if (changePercent != 0) {
                        Box(
                            modifier = Modifier
                                .background(
                                    if (changePercent > 0) AtlasPrimary.copy(alpha = 0.15f)
                                    else AtlasError.copy(alpha = 0.15f),
                                    MaterialTheme.shapes.small
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "${if (changePercent > 0) "↗" else "↘"} ${if (changePercent > 0) "+" else ""}$changePercent%",
                                style = MaterialTheme.typography.labelMedium,
                                color = if (changePercent > 0) AtlasPrimary else AtlasError
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // The Graph
                if (isEmpty) {
                    // Ghost empty state
                    GhostGraph(modifier = Modifier.fillMaxSize())
                } else {
                    LineGraph(
                        volumes = weeklyVolumes.map { it.totalVolume.toFloat() },
                        progress = animatedProgress.value,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
private fun LineGraph(
    volumes: List<Float>,
    progress: Float,
    modifier: Modifier = Modifier
) {
    val lineColor = AtlasSecondary
    val glowColor = AtlasSecondaryGlow

    Canvas(modifier = modifier) {
        if (volumes.isEmpty()) return@Canvas

        val maxVol = volumes.max().coerceAtLeast(1f)
        val minVol = 0f
        val range = maxVol - minVol

        val points = volumes.mapIndexed { i, vol ->
            val x = if (volumes.size <= 1) size.width / 2 else (i.toFloat() / (volumes.size - 1)) * size.width
            val y = size.height - ((vol - minVol) / range) * size.height * 0.85f
            Offset(x, y)
        }

        // Draw line path
        val path = Path()
        points.forEachIndexed { i, point ->
            val animatedPoint = Offset(point.x, size.height + (point.y - size.height) * progress)
            if (i == 0) path.moveTo(animatedPoint.x, animatedPoint.y)
            else {
                val prev = points[i - 1]
                val animatedPrev = Offset(prev.x, size.height + (prev.y - size.height) * progress)
                val cp1 = Offset((animatedPrev.x + animatedPoint.x) / 2, animatedPrev.y)
                val cp2 = Offset((animatedPrev.x + animatedPoint.x) / 2, animatedPoint.y)
                path.cubicTo(cp1.x, cp1.y, cp2.x, cp2.y, animatedPoint.x, animatedPoint.y)
            }
        }

        // Glow
        drawPath(
            path = path,
            color = glowColor,
            style = Stroke(width = 6f, cap = StrokeCap.Round)
        )

        // Main line
        drawPath(
            path = path,
            color = lineColor,
            style = Stroke(width = 2.5f, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )

        // Data points
        points.forEachIndexed { i, point ->
            val animatedPoint = Offset(point.x, size.height + (point.y - size.height) * progress)
            val isLast = i == points.lastIndex
            drawCircle(
                color = if (isLast) lineColor else AtlasBackground,
                radius = if (isLast) 5f else 4f,
                center = animatedPoint
            )
            drawCircle(
                color = lineColor,
                radius = if (isLast) 5f else 4f,
                center = animatedPoint,
                style = Stroke(width = 2f)
            )
        }
    }
}

@Composable
private fun GhostGraph(modifier: Modifier = Modifier) {
    val ghostColor = AtlasTextSecondary.copy(alpha = 0.15f)
    Canvas(modifier = modifier) {
        // Draw dashed ghost line
        val ghostPoints = listOf(0.8f, 0.6f, 0.65f, 0.45f, 0.5f, 0.35f, 0.3f)
        val path = Path()
        ghostPoints.forEachIndexed { i, ratio ->
            val x = (i.toFloat() / (ghostPoints.size - 1)) * size.width
            val y = size.height * ratio
            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }
        drawPath(
            path = path,
            color = ghostColor,
            style = Stroke(width = 2f, cap = StrokeCap.Round, pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 8f)))
        )
    }
}

