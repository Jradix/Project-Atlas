package com.atlas.logger.ui.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.atlas.logger.ui.theme.*
import com.atlas.logger.util.AppLocale
import com.atlas.logger.util.AtlasStrings

data class NavItem(
    val route: String,
    val labelEn: String,
    val labelAr: String,
    val icon: ImageVector
)

@Composable
fun BottomNavBar(
    currentRoute: String?,
    locale: AppLocale,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        NavItem(AtlasRoutes.DASHBOARD, "DASHBOARD", "الرئيسية", Icons.Default.GridView),
        NavItem(AtlasRoutes.WORKOUT, "WORKOUT", "التمرين", Icons.Default.FitnessCenter),
        NavItem(AtlasRoutes.LIBRARY, "LIBRARY", "المكتبة", Icons.Default.MenuBook),
    )

    val isRtl = locale == AppLocale.ARABIC
    val displayItems = if (isRtl) items.reversed() else items

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color.Transparent, AtlasSurfaceVariant),
                    startY = 0f,
                    endY = 40f
                )
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(AtlasSurfaceVariant.copy(alpha = 0.95f))
                .padding(vertical = 12.dp, horizontal = 8.dp)
                .navigationBarsPadding(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            displayItems.forEach { item ->
                val isSelected = currentRoute == item.route
                val tint by animateColorAsState(
                    targetValue = if (isSelected) AtlasPrimary else AtlasTextSecondary.copy(alpha = 0.4f),
                    animationSpec = tween(200),
                    label = "navColor"
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onNavigate(item.route) },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.labelEn,
                        tint = tint,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (locale == AppLocale.ARABIC) item.labelAr else item.labelEn,
                        color = tint,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 1.sp
                    )
                }
            }
        }
    }
}
