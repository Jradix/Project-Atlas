package com.atlas.logger.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// ═══════════════════════════════════════════════════════
// Shapes — "Pro/Industrial" Look
// Max corner radius: 12dp (strict, per spec)
// ═══════════════════════════════════════════════════════

val AtlasShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(10.dp),
    large = RoundedCornerShape(12.dp),
    extraLarge = RoundedCornerShape(12.dp),  // Capped at 12dp
)
