package com.atlas.logger.ui.history

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.atlas.logger.domain.model.WorkoutSession
import com.atlas.logger.ui.theme.*
import com.atlas.logger.util.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HistoryScreen(
    locale: AppLocale,
    weightUnit: WeightUnit,
    onBack: () -> Unit,
    onViewSession: (Long) -> Unit,
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AtlasBackground)
    ) {
        // ═══ Top Bar ═══
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(AtlasSurfaceVariant.copy(alpha = 0.6f))
                .statusBarsPadding()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack, modifier = Modifier.size(36.dp)) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = AtlasTextPrimary)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (locale == AppLocale.ARABIC) "السجل الكامل" else "WORKOUT HISTORY",
                style = MaterialTheme.typography.titleLarge,
                color = AtlasTextPrimary,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }

        // ═══ Sessions grouped by month/day ═══
        if (uiState.sessions.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = AtlasStrings.noWorkoutsYet(locale),
                    style = MaterialTheme.typography.bodyLarge,
                    color = AtlasTextSecondary.copy(alpha = 0.5f)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Group sessions by date string
                val grouped = uiState.sessions.groupBy { session ->
                    val loc = if (locale == AppLocale.ARABIC) Locale("ar") else Locale.ENGLISH
                    SimpleDateFormat("EEEE, dd MMMM yyyy", loc).format(Date(session.date))
                }

                grouped.forEach { (dateLabel, sessions) ->
                    // Date header
                    item {
                        Text(
                            text = dateLabel.uppercase(),
                            style = MaterialTheme.typography.labelMedium,
                            color = AtlasSecondary,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    // Session cards for that date
                    items(sessions, key = { it.id }) { session ->
                        HistorySessionCard(
                            session = session,
                            locale = locale,
                            weightUnit = weightUnit,
                            onClick = {
                                onViewSession(session.id)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HistorySessionCard(
    session: WorkoutSession,
    locale: AppLocale,
    weightUnit: WeightUnit,
    onClick: () -> Unit
) {
    val name = if (locale == AppLocale.ARABIC) session.nameAr.ifEmpty { session.name } else session.name.ifEmpty { "Workout" }
    val timeFormat = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
    val timeStr = timeFormat.format(Date(session.date))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(AtlasSurface, MaterialTheme.shapes.large)
            .border(1.dp, AtlasBorder, MaterialTheme.shapes.large)
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Time indicator
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(56.dp)
        ) {
            Text(
                text = timeStr,
                style = MaterialTheme.typography.labelSmall,
                color = AtlasTextSecondary
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleSmall,
                color = AtlasTextPrimary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (session.totalVolume > 0) {
                    Text(
                        text = "${UnitConverter.formatVolume(session.totalVolume, weightUnit)} ${UnitConverter.unitLabel(weightUnit)}",
                        style = MaterialTheme.typography.labelSmall,
                        color = AtlasPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
                if (session.durationSeconds > 0) {
                    Text(
                        text = "${session.durationSeconds / 60} ${AtlasStrings.min(locale)}",
                        style = MaterialTheme.typography.labelSmall,
                        color = AtlasTextSecondary
                    )
                }
            }
        }

        Icon(
            Icons.Default.ChevronRight,
            contentDescription = "View Workflow",
            tint = AtlasTextSecondary.copy(alpha = 0.3f),
            modifier = Modifier.size(24.dp)
        )
    }
}
