package com.atlas.logger.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// ═══════════════════════════════════════════════════════
// Atlas Theme — Dark Mode Professional
// Always dark. No light mode variant.
// ═══════════════════════════════════════════════════════

private val AtlasColorScheme = darkColorScheme(
    primary = AtlasPrimary,
    onPrimary = AtlasTextOnPrimary,
    primaryContainer = AtlasPrimaryDim,
    secondary = AtlasSecondary,
    onSecondary = AtlasTextPrimary,
    secondaryContainer = AtlasSecondaryDim,
    background = AtlasBackground,
    onBackground = AtlasTextPrimary,
    surface = AtlasSurface,
    onSurface = AtlasTextPrimary,
    surfaceVariant = AtlasSurfaceVariant,
    onSurfaceVariant = AtlasTextSecondary,
    outline = AtlasBorder,
    error = AtlasError,
    onError = AtlasTextPrimary,
)

@Composable
fun AtlasTheme(
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = AtlasBackground.toArgb()
            window.navigationBarColor = AtlasSurfaceVariant.toArgb()
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = false
                isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = AtlasColorScheme,
        typography = AtlasTypography,
        shapes = AtlasShapes,
        content = content
    )
}
