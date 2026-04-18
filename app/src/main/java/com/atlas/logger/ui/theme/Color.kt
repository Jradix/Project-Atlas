package com.atlas.logger.ui.theme

import androidx.compose.ui.graphics.Color

// ═══════════════════════════════════════════════════════
// Atlas Logger — "Dark Mode Professional" Color Palette
// OLED-optimized: #0D0D0D base saves battery
// ═══════════════════════════════════════════════════════

// Core Backgrounds
val AtlasBackground = Color(0xFF0D0D0D)     // Pure Black (OLED)
val AtlasSurface = Color(0xFF1A1A1A)         // Deep Grey (cards, modals)
val AtlasSurfaceVariant = Color(0xFF131313)  // Slightly lighter than background
val AtlasBorder = Color(0xFF2A2A2A)          // Subtle borders

// Accent Colors
val AtlasPrimary = Color(0xFF00E676)         // Neon Green — "Completed" / "Success" / "Record Broken"
val AtlasPrimaryDim = Color(0xFF00C853)      // Darker green for pressed states
val AtlasSecondary = Color(0xFF2979FF)       // Electric Blue — "In Progress" / "Active"
val AtlasSecondaryDim = Color(0xFF1565C0)    // Darker blue for pressed states

// Semantic Colors
val AtlasSuccess = AtlasPrimary
val AtlasActive = AtlasSecondary
val AtlasError = Color(0xFFEF5350)           // Red for destructive actions
val AtlasWarning = Color(0xFFFFB74D)         // Orange for warnings

// Text Colors
val AtlasTextPrimary = Color(0xFFFFFFFF)     // Pure White
val AtlasTextSecondary = Color(0xFF888888)   // Medium Grey
val AtlasTextTertiary = Color(0xFFA3A3A3)    // Light Grey (labels)
val AtlasTextOnPrimary = Color(0xFF0D0D0D)   // Dark text on neon green

// Gradient helpers
val AtlasPrimaryGlow = Color(0x6600E676)     // 40% opacity green glow
val AtlasSecondaryGlow = Color(0x662979FF)   // 40% opacity blue glow
