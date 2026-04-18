package com.atlas.logger.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp

// ═══════════════════════════════════════════════════════
// Google Fonts Provider — loads Inter font dynamically
// ═══════════════════════════════════════════════════════

private val googleFontProvider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = com.atlas.logger.R.array.com_google_android_gms_fonts_certs
)

private val interFont = GoogleFont("Inter")

val InterFontFamily = FontFamily(
    Font(googleFont = interFont, fontProvider = googleFontProvider, weight = FontWeight.Normal),
    Font(googleFont = interFont, fontProvider = googleFontProvider, weight = FontWeight.Medium),
    Font(googleFont = interFont, fontProvider = googleFontProvider, weight = FontWeight.SemiBold),
    Font(googleFont = interFont, fontProvider = googleFontProvider, weight = FontWeight.Bold),
    Font(googleFont = interFont, fontProvider = googleFontProvider, weight = FontWeight.ExtraBold),
    Font(googleFont = interFont, fontProvider = googleFontProvider, weight = FontWeight.Black),
)

// Fallback if Google Fonts not available
val AtlasFontFamily = InterFontFamily

// ═══════════════════════════════════════════════════════
// Typography — Industrial Minimalism
// Tight letter spacing, bold weights for data
// ═══════════════════════════════════════════════════════

val AtlasTypography = Typography(
    // Large display numbers (volume totals, timer)
    displayLarge = TextStyle(
        fontFamily = AtlasFontFamily,
        fontWeight = FontWeight.Black,
        fontSize = 56.sp,
        letterSpacing = (-2).sp,
        lineHeight = 64.sp,
    ),
    displayMedium = TextStyle(
        fontFamily = AtlasFontFamily,
        fontWeight = FontWeight.Black,
        fontSize = 40.sp,
        letterSpacing = (-1.5).sp,
        lineHeight = 48.sp,
    ),
    displaySmall = TextStyle(
        fontFamily = AtlasFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        letterSpacing = (-1).sp,
        lineHeight = 40.sp,
    ),
    // Headlines for section titles
    headlineLarge = TextStyle(
        fontFamily = AtlasFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        letterSpacing = (-0.5).sp,
        lineHeight = 36.sp,
    ),
    headlineMedium = TextStyle(
        fontFamily = AtlasFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        letterSpacing = (-0.25).sp,
        lineHeight = 28.sp,
    ),
    headlineSmall = TextStyle(
        fontFamily = AtlasFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        letterSpacing = 0.sp,
        lineHeight = 24.sp,
    ),
    // Titles for cards, exercise names
    titleLarge = TextStyle(
        fontFamily = AtlasFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        letterSpacing = (-0.25).sp,
        lineHeight = 26.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = AtlasFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        letterSpacing = 0.sp,
        lineHeight = 22.sp,
    ),
    titleSmall = TextStyle(
        fontFamily = AtlasFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        letterSpacing = 0.sp,
        lineHeight = 20.sp,
    ),
    // Body text
    bodyLarge = TextStyle(
        fontFamily = AtlasFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        letterSpacing = 0.sp,
        lineHeight = 22.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = AtlasFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        letterSpacing = 0.sp,
        lineHeight = 20.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = AtlasFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        letterSpacing = 0.sp,
        lineHeight = 16.sp,
    ),
    // Labels (chip text, button labels, table headers)
    labelLarge = TextStyle(
        fontFamily = AtlasFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        letterSpacing = 1.sp,
        lineHeight = 18.sp,
    ),
    labelMedium = TextStyle(
        fontFamily = AtlasFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        letterSpacing = 0.75.sp,
        lineHeight = 16.sp,
    ),
    labelSmall = TextStyle(
        fontFamily = AtlasFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 10.sp,
        letterSpacing = 0.5.sp,
        lineHeight = 14.sp,
    ),
)
