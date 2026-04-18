package com.atlas.logger.ui.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.atlas.logger.domain.model.WorkoutSession
import com.atlas.logger.ui.theme.*
import com.atlas.logger.util.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Delete
import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton

@Composable
fun RecentActivityCard(
    session: WorkoutSession,
    locale: AppLocale,
    weightUnit: WeightUnit,
    onClick: () -> Unit,
    onDelete: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val isRtl = locale == AppLocale.ARABIC

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(AtlasSurface, MaterialTheme.shapes.large)
            .border(1.dp, AtlasBorder, MaterialTheme.shapes.large)
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Column {
            // Top row: Name + Date badge + Delete icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = if (isRtl) Arrangement.End else Arrangement.Start
                ) {
                    if (!isRtl) {
                        Text(
                            text = if (locale == AppLocale.ARABIC) session.nameAr.ifEmpty { session.name } else session.name.ifEmpty { "Workout" },
                            style = MaterialTheme.typography.titleMedium,
                            color = AtlasTextPrimary,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .background(AtlasSurfaceVariant, MaterialTheme.shapes.small)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = formatRelativeDate(session.date, locale),
                            style = MaterialTheme.typography.labelSmall,
                            color = AtlasTextSecondary
                        )
                    }

                    if (isRtl) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = session.nameAr.ifEmpty { session.name },
                            style = MaterialTheme.typography.titleMedium,
                            color = AtlasTextPrimary,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                    }
                }

                // Delete Button
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = AtlasTextSecondary.copy(alpha = 0.4f),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Bottom row: Stats chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left side: Stats chips
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Volume badge
                    if (session.totalVolume > 0) {
                        Box(
                            modifier = Modifier
                                .background(AtlasPrimary.copy(alpha = 0.15f), MaterialTheme.shapes.small)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "${UnitConverter.formatVolume(session.totalVolume, weightUnit)} ${UnitConverter.unitLabel(weightUnit)}",
                                style = MaterialTheme.typography.labelSmall,
                                color = AtlasPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Duration
                    if (session.durationSeconds > 0) {
                        Box(
                            modifier = Modifier
                                .background(AtlasSurfaceVariant, MaterialTheme.shapes.small)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "${session.durationSeconds / 60} ${AtlasStrings.min(locale)}",
                                style = MaterialTheme.typography.labelSmall,
                                color = AtlasTextSecondary
                            )
                        }
                    }
                }

            }
        }
    }
}

private fun formatRelativeDate(timestamp: Long, locale: AppLocale): String {
    val now = System.currentTimeMillis()
    val diffDays = TimeUnit.MILLISECONDS.toDays(now - timestamp)
    val javaLocale = if (locale == AppLocale.ARABIC) Locale("ar") else Locale.ENGLISH

    return when {
        diffDays == 0L -> AtlasStrings.today(locale)
        diffDays == 1L -> AtlasStrings.yesterday(locale)
        diffDays < 7L -> {
            // Show day name for this week
            val format = SimpleDateFormat("EEEE", javaLocale)
            format.format(Date(timestamp)).uppercase()
        }
        else -> {
            // Show month + day for older
            val format = SimpleDateFormat("dd MMM", javaLocale)
            format.format(Date(timestamp)).uppercase()
        }
    }
}
