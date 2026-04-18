# ═══════════════════════════════════════════
# Project Atlas — ProGuard Rules (Optimized)
# ═══════════════════════════════════════════

# Keep annotations
-keepattributes *Annotation*
-keepattributes Signature

# ═══ Room Database ═══
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-keep class com.atlas.logger.data.local.entity.** { *; }

# ═══ Gson (Backup) ═══
-keep class com.google.gson.** { *; }
-keep class com.atlas.logger.data.backup.AtlasBackup { *; }

# ═══ Hilt ═══
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep @dagger.hilt.android.lifecycle.HiltViewModel class * { *; }
-keep class * extends androidx.lifecycle.ViewModel

# ═══ Compose ═══
-dontwarn androidx.compose.**

# ═══ Kotlin Coroutines ═══
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}

# ═══ Coil Image Loading ═══
-keep class io.coilkt.** { *; }
-dontwarn io.coilkt.**

# ═══ Navigation ═══
-keep class androidx.navigation.** { *; }

# ═══ General ═══
-dontwarn com.atlas.logger.**
-keep class com.atlas.logger.domain.model.** { *; }

# ═══ Cleanup ═══
# Removed aggressive repackaging to avoid build errors

