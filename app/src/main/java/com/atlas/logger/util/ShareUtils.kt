package com.atlas.logger.util

import android.content.Context
import android.content.Intent
import android.graphics.*
import android.net.Uri
import androidx.core.content.FileProvider
import com.atlas.logger.ui.summary.SummaryUiState
import com.atlas.logger.util.AppLocale
import com.atlas.logger.util.WeightUnit
import com.atlas.logger.util.UnitConverter
import java.io.File
import java.io.FileOutputStream

object ShareUtils {

    fun shareWorkout(
        context: Context,
        uiState: SummaryUiState,
        locale: AppLocale,
        weightUnit: WeightUnit
    ) {
        val session = uiState.session ?: return

        // 1. Generate text
        val title = if (locale == AppLocale.ARABIC) session.nameAr else session.name
        val volume = if (weightUnit == WeightUnit.LBS) UnitConverter.kgToLbs(session.totalVolume) else session.totalVolume
        val unitLabel = UnitConverter.unitLabel(weightUnit)

        val textBuilder = java.lang.StringBuilder()
        textBuilder.append("🏋️ $title\n")
        textBuilder.append("⏱️ ${formatDuration(session.durationSeconds)} | ")
        textBuilder.append("⚖️ ${"%,.0f".format(volume)} $unitLabel\n\n")

        if (uiState.exercises.isNotEmpty()) {
            textBuilder.append(if (locale == AppLocale.ARABIC) "التمارين:\n" else "Exercises:\n")
            uiState.exercises.forEach { ex ->
                val name = if (locale == AppLocale.ARABIC) ex.exercise.nameAr.ifEmpty { ex.exercise.name } else ex.exercise.name.ifEmpty { "Exercise" }
                val setsStr = if (locale == AppLocale.ARABIC) "${ex.sets.size} مجموعات" else "${ex.sets.size} sets"
                textBuilder.append("• $name: $setsStr\n")
            }
            textBuilder.append("\n")
        }
        textBuilder.append("— Logged via #ProjectAtlas")

        // 2. Generate Image
        val imageUri = generateShareCard(context, uiState, locale, weightUnit)

        // 3. Share Intent
        val intent = Intent(Intent.ACTION_SEND).apply {
            if (imageUri != null) {
                type = "image/png"
                putExtra(Intent.EXTRA_STREAM, imageUri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            } else {
                type = "text/plain"
            }
            putExtra(Intent.EXTRA_TEXT, textBuilder.toString())
        }

        context.startActivity(Intent.createChooser(intent, "Share Workout"))
    }

    private fun formatDuration(seconds: Int): String {
        val h = seconds / 3600
        val m = (seconds % 3600) / 60
        val s = seconds % 60
        return if (h > 0) "%d:%02d:%02d".format(h, m, s) else "%d:%02d".format(m, s)
    }

    private fun generateShareCard(
        context: Context,
        uiState: SummaryUiState,
        locale: AppLocale,
        weightUnit: WeightUnit
    ): Uri? {
        val session = uiState.session ?: return null

        val width = 1080
        val height = 1920
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // Colors
        val bgDark = Color.parseColor("#121212")
        val bgMedium = Color.parseColor("#1E1E1E")
        val primary = Color.parseColor("#00E676") // Atlas Primary
        val textPrimary = Color.WHITE
        val textSecondary = Color.parseColor("#AAAAAA")

        // Draw background
        canvas.drawColor(bgDark)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        
        // Draw Header "PROJECT ATLAS"
        paint.color = primary
        paint.textSize = 50f
        paint.typeface = Typeface.DEFAULT_BOLD
        paint.textAlign = Paint.Align.CENTER
        canvas.drawText("PROJECT ATLAS", width / 2f, 150f, paint)

        // Draw Session Title
        val title = if (locale == AppLocale.ARABIC) session.nameAr else session.name
        paint.color = textPrimary
        paint.textSize = 80f
        canvas.drawText(title.uppercase(), width / 2f, 300f, paint)

        // Draw stats box
        val cx = width / 2f
        val cy = 500f
        val boxWidth = 800f
        
        paint.color = bgMedium
        paint.style = Paint.Style.FILL
        canvas.drawRoundRect(cx - boxWidth/2, cy - 100, cx + boxWidth/2, cy + 150, 30f, 30f, paint)
        
        paint.style = Paint.Style.STROKE
        paint.color = primary
        paint.strokeWidth = 3f
        paint.alpha = 50
        canvas.drawRoundRect(cx - boxWidth/2, cy - 100, cx + boxWidth/2, cy + 150, 30f, 30f, paint)
        paint.alpha = 255

        // Stats texts inside box
        paint.style = Paint.Style.FILL
        paint.textAlign = Paint.Align.CENTER
        
        val volume = if (weightUnit == WeightUnit.LBS) UnitConverter.kgToLbs(session.totalVolume) else session.totalVolume
        val unitLabel = UnitConverter.unitLabel(weightUnit)
        
        val durLabel = if (locale == AppLocale.ARABIC) "المدة" else "DURATION"
        val volLabel = if (locale == AppLocale.ARABIC) "الوزن ($unitLabel)" else "VOLUME ($unitLabel)"

        // Left split (Duration)
        paint.color = textSecondary
        paint.textSize = 35f
        canvas.drawText(durLabel, cx - 200, cy - 20, paint)
        paint.color = textPrimary
        paint.textSize = 65f
        paint.typeface = Typeface.DEFAULT_BOLD
        canvas.drawText(formatDuration(session.durationSeconds), cx - 200, cy + 60, paint)

        // Right split (Volume)
        paint.color = textSecondary
        paint.textSize = 35f
        paint.typeface = Typeface.DEFAULT
        canvas.drawText(volLabel, cx + 200, cy - 20, paint)
        paint.color = primary
        paint.textSize = 65f
        paint.typeface = Typeface.DEFAULT_BOLD
        canvas.drawText("%,.0f".format(volume), cx + 200, cy + 60, paint)

        // Exercises text
        var yPos = 800f
        paint.color = textSecondary
        paint.textSize = 40f
        paint.textAlign = Paint.Align.LEFT
        val exTitle = if (locale == AppLocale.ARABIC) "تفاصيل التمارين" else "EXERCISES"
        canvas.drawText(exTitle, 150f, yPos, paint)

        yPos += 80f
        paint.textSize = 45f

        uiState.exercises.take(10).forEach { ex ->
            val name = if (locale == AppLocale.ARABIC) ex.exercise.nameAr.ifEmpty { ex.exercise.name } else ex.exercise.name.ifEmpty { "Exercise" }
            val setsStr = if (locale == AppLocale.ARABIC) "${ex.sets.size} مجموعات" else "${ex.sets.size} sets"
            
            paint.color = textPrimary
            paint.typeface = Typeface.DEFAULT_BOLD
            canvas.drawText("• $name", 150f, yPos, paint)

            paint.color = textSecondary
            paint.typeface = Typeface.DEFAULT
            paint.textAlign = Paint.Align.RIGHT
            canvas.drawText(setsStr, width - 150f, yPos, paint)
            
            paint.textAlign = Paint.Align.LEFT
            yPos += 80f
        }

        if (uiState.exercises.size > 10) {
            paint.color = textSecondary
            paint.typeface = Typeface.DEFAULT
            canvas.drawText("... and ${uiState.exercises.size - 10} more", 150f, yPos, paint)
        }

        // Save bitmap
        return try {
            val cachePath = File(context.cacheDir, "share_images")
            cachePath.mkdirs()
            val file = File(cachePath, "workout_share.png")
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.close()

            FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
