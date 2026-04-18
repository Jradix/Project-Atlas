package com.atlas.logger.util

/**
 * Runtime string resources for EN and AR.
 * Avoids Android's strings.xml system to enable instant language switching
 * without activity recreation.
 */
object AtlasStrings {

    // ═══════════════════════════════════════════════════════
    // Navigation
    // ═══════════════════════════════════════════════════════
    fun dashboard(l: AppLocale) = when(l) { AppLocale.ENGLISH -> "DASHBOARD"; AppLocale.ARABIC -> "الرئيسية" }
    fun workout(l: AppLocale) = when(l) { AppLocale.ENGLISH -> "WORKOUT"; AppLocale.ARABIC -> "التمرين" }
    fun library(l: AppLocale) = when(l) { AppLocale.ENGLISH -> "LIBRARY"; AppLocale.ARABIC -> "المكتبة" }

    // ═══════════════════════════════════════════════════════
    // Dashboard
    // ═══════════════════════════════════════════════════════
    fun welcome(l: AppLocale) = when(l) { AppLocale.ENGLISH -> "Welcome, Champion"; AppLocale.ARABIC -> "أهلاً بك، أيها البطل" }
    fun welcomeBack(l: AppLocale) = when(l) { AppLocale.ENGLISH -> "Welcome back, Champion"; AppLocale.ARABIC -> "أهلاً بك مجدداً، أيها البطل" }
    fun readyForCycle(l: AppLocale) = when(l) { AppLocale.ENGLISH -> "Ready for your next cycle."; AppLocale.ARABIC -> "مستعد لدورتك القادمة" }
    fun volumePulse(l: AppLocale) = when(l) { AppLocale.ENGLISH -> "VOLUME PULSE"; AppLocale.ARABIC -> "الحجم الأسبوعي" }
    fun pastWeeks(l: AppLocale, n: Int = 4) = when(l) { AppLocale.ENGLISH -> "PAST $n WEEKS"; AppLocale.ARABIC -> "آخر $n أسابيع" }
    fun recentActivity(l: AppLocale) = when(l) { AppLocale.ENGLISH -> "RECENT ACTIVITY"; AppLocale.ARABIC -> "النشاط الأخير" }
    fun viewAll(l: AppLocale) = when(l) { AppLocale.ENGLISH -> "View All"; AppLocale.ARABIC -> "عرض الكل" }
    fun startWorkout(l: AppLocale) = when(l) { AppLocale.ENGLISH -> "START WORKOUT"; AppLocale.ARABIC -> "ابدأ التمرين" }
    fun today(l: AppLocale) = when(l) { AppLocale.ENGLISH -> "TODAY"; AppLocale.ARABIC -> "اليوم" }
    fun yesterday(l: AppLocale) = when(l) { AppLocale.ENGLISH -> "YESTERDAY"; AppLocale.ARABIC -> "أمس" }
    fun kgTotal(l: AppLocale) = when(l) { AppLocale.ENGLISH -> "KG Total"; AppLocale.ARABIC -> "كجم إجمالي" }

    // ═══════════════════════════════════════════════════════
    // Active Workout
    // ═══════════════════════════════════════════════════════
    fun activeSession(l: AppLocale) = when(l) { AppLocale.ENGLISH -> "ACTIVE SESSION"; AppLocale.ARABIC -> "جلسة نشطة" }
    fun setLabel(l: AppLocale) = when(l) { AppLocale.ENGLISH -> "SET"; AppLocale.ARABIC -> "مجموعة" }
    fun previous(l: AppLocale) = when(l) { AppLocale.ENGLISH -> "PREVIOUS"; AppLocale.ARABIC -> "السابق" }
    fun reps(l: AppLocale) = when(l) { AppLocale.ENGLISH -> "REPS"; AppLocale.ARABIC -> "تكرارات" }
    fun addSet(l: AppLocale) = when(l) { AppLocale.ENGLISH -> "+ ADD SET"; AppLocale.ARABIC -> "+ إضافة مجموعة" }
    fun finish(l: AppLocale) = when(l) { AppLocale.ENGLISH -> "FINISH"; AppLocale.ARABIC -> "إنهاء" }
    fun finishWorkout(l: AppLocale) = when(l) { AppLocale.ENGLISH -> "FINISH WORKOUT"; AppLocale.ARABIC -> "إنهاء التمرين" }
    fun last(l: AppLocale) = when(l) { AppLocale.ENGLISH -> "Last"; AppLocale.ARABIC -> "الأداء الأخير" }
    fun addExercise(l: AppLocale) = when(l) { AppLocale.ENGLISH -> "ADD EXERCISE"; AppLocale.ARABIC -> "إضافة تمرين" }
    fun workoutTime(l: AppLocale) = when(l) { AppLocale.ENGLISH -> "Workout Time"; AppLocale.ARABIC -> "وقت التمرين" }

    // ═══════════════════════════════════════════════════════
    // Exercise Library
    // ═══════════════════════════════════════════════════════
    fun searchExercises(l: AppLocale) = when(l) { AppLocale.ENGLISH -> "Search exercises..."; AppLocale.ARABIC -> "البحث عن التمارين..." }
    fun allMuscles(l: AppLocale) = when(l) { AppLocale.ENGLISH -> "ALL"; AppLocale.ARABIC -> "جميع العضلات" }
    fun theAtlas(l: AppLocale) = "PROJECT ATLAS"
    fun selectExercise(l: AppLocale) = when(l) { AppLocale.ENGLISH -> "Select an exercise to view blueprint."; AppLocale.ARABIC -> "اختر تمريناً لعرض التفاصيل" }

    // Muscle groups
    fun chest(l: AppLocale) = when(l) { AppLocale.ENGLISH -> "CHEST"; AppLocale.ARABIC -> "صدر" }
    fun back(l: AppLocale) = when(l) { AppLocale.ENGLISH -> "BACK"; AppLocale.ARABIC -> "ظهر" }
    fun legs(l: AppLocale) = when(l) { AppLocale.ENGLISH -> "LEGS"; AppLocale.ARABIC -> "أرجل" }
    fun shoulders(l: AppLocale) = when(l) { AppLocale.ENGLISH -> "SHOULDERS"; AppLocale.ARABIC -> "أكتاف" }
    fun arms(l: AppLocale) = when(l) { AppLocale.ENGLISH -> "ARMS"; AppLocale.ARABIC -> "ذراع" }
    fun core(l: AppLocale) = when(l) { AppLocale.ENGLISH -> "CORE"; AppLocale.ARABIC -> "بطن" }
    fun cardio(l: AppLocale) = when(l) { AppLocale.ENGLISH -> "CARDIO"; AppLocale.ARABIC -> "كارديو" }

    // ═══════════════════════════════════════════════════════
    // Workout Summary
    // ═══════════════════════════════════════════════════════
    fun workoutComplete(l: AppLocale) = when(l) { AppLocale.ENGLISH -> "WORKOUT\nCOMPLETE"; AppLocale.ARABIC -> "اكتمل التمرين" }
    fun totalVolume(l: AppLocale) = when(l) { AppLocale.ENGLISH -> "TOTAL VOLUME"; AppLocale.ARABIC -> "إجمالي الحجم" }
    fun duration(l: AppLocale) = when(l) { AppLocale.ENGLISH -> "DURATION"; AppLocale.ARABIC -> "المدة" }
    fun calories(l: AppLocale) = when(l) { AppLocale.ENGLISH -> "CALORIES"; AppLocale.ARABIC -> "السعرات الحرارية" }
    fun newRecords(l: AppLocale) = when(l) { AppLocale.ENGLISH -> "NEW RECORDS"; AppLocale.ARABIC -> "أرقام قياسية جديدة" }
    fun share(l: AppLocale) = when(l) { AppLocale.ENGLISH -> "SHARE"; AppLocale.ARABIC -> "مشاركة" }
    fun weightPR(l: AppLocale) = when(l) { AppLocale.ENGLISH -> "1RM PR"; AppLocale.ARABIC -> "أقصى وزن جديد" }
    fun volumePR(l: AppLocale) = when(l) { AppLocale.ENGLISH -> "VOLUME PR"; AppLocale.ARABIC -> "أقصى حجم جديد" }
    fun lbsMoved(l: AppLocale) = when(l) { AppLocale.ENGLISH -> "LBS MOVED"; AppLocale.ARABIC -> "رطل تم رفعه" }
    fun kgMoved(l: AppLocale) = when(l) { AppLocale.ENGLISH -> "KG MOVED"; AppLocale.ARABIC -> "كجم تم رفعه" }

    // ═══════════════════════════════════════════════════════
    // Settings
    // ═══════════════════════════════════════════════════════
    fun settings(l: AppLocale) = when(l) { AppLocale.ENGLISH -> "SETTINGS"; AppLocale.ARABIC -> "الإعدادات" }
    fun language(l: AppLocale) = when(l) { AppLocale.ENGLISH -> "Language"; AppLocale.ARABIC -> "اللغة" }
    fun imagePromptTitle(l: AppLocale) = when(l) { AppLocale.ENGLISH -> "Image Prompt"; AppLocale.ARABIC -> "برومبت الصور" }
    fun measurementUnit(l: AppLocale) = when(l) { AppLocale.ENGLISH -> "Measurement Unit"; AppLocale.ARABIC -> "وحدة القياس" }
    fun exportData(l: AppLocale) = when(l) { AppLocale.ENGLISH -> "Export Data (JSON)"; AppLocale.ARABIC -> "تصدير البيانات (JSON)" }
    fun importData(l: AppLocale) = when(l) { AppLocale.ENGLISH -> "Import Data (JSON)"; AppLocale.ARABIC -> "استيراد البيانات (JSON)" }
    fun exportSuccess(l: AppLocale) = when(l) { AppLocale.ENGLISH -> "Data exported to Downloads"; AppLocale.ARABIC -> "تم تصدير البيانات إلى التنزيلات" }
    fun importSuccess(l: AppLocale) = when(l) { AppLocale.ENGLISH -> "Data imported successfully"; AppLocale.ARABIC -> "تم استيراد البيانات بنجاح" }
    fun error(l: AppLocale) = when(l) { AppLocale.ENGLISH -> "Error"; AppLocale.ARABIC -> "خطأ" }
    fun about(l: AppLocale) = when(l) { AppLocale.ENGLISH -> "About"; AppLocale.ARABIC -> "حول" }
    fun version(l: AppLocale) = when(l) { AppLocale.ENGLISH -> "Version"; AppLocale.ARABIC -> "الإصدار" }
    fun seconds(l: AppLocale) = when(l) { AppLocale.ENGLISH -> "seconds"; AppLocale.ARABIC -> "ثانية" }
    fun deleteSessionTitle(l: AppLocale) = when(l) { AppLocale.ENGLISH -> "Delete Session?"; AppLocale.ARABIC -> "حذف الجلسة؟" }
    fun deleteSessionConfirm(l: AppLocale) = when(l) { AppLocale.ENGLISH -> "Are you sure you want to delete this workout?"; AppLocale.ARABIC -> "هل أنت متأكد من حذف هذا التمرين؟" }
    fun clearHistoryTitle(l: AppLocale) = when(l) { AppLocale.ENGLISH -> "Clear History"; AppLocale.ARABIC -> "مسح السجل" }
    fun clearHistoryConfirm(l: AppLocale) = when(l) { AppLocale.ENGLISH -> "Clear all workout history? This cannot be undone."; AppLocale.ARABIC -> "مسح كافة سجل التمارين؟ لا يمكن التراجع عن هذا." }
    fun clearHistoryAction(l: AppLocale) = when(l) { AppLocale.ENGLISH -> "CLEAR HISTORY"; AppLocale.ARABIC -> "مسح السجل" }

    // ═══════════════════════════════════════════════════════
    // General
    // ═══════════════════════════════════════════════════════
    fun projectAtlas(l: AppLocale) = "PROJECT ATLAS"
    fun topLift(l: AppLocale) = when(l) { AppLocale.ENGLISH -> "TOP LIFT"; AppLocale.ARABIC -> "أعلى وزن" }
    fun exercises(l: AppLocale) = when(l) { AppLocale.ENGLISH -> "Exercises"; AppLocale.ARABIC -> "تمارين" }
    fun min(l: AppLocale) = when(l) { AppLocale.ENGLISH -> "Min"; AppLocale.ARABIC -> "دقيقة" }
    fun noWorkoutsYet(l: AppLocale) = when(l) { AppLocale.ENGLISH -> "No workouts yet.\nStart your first session"; AppLocale.ARABIC -> "لا توجد تمارين بعد.\nابدأ جلستك الأولى" }
    fun cancel(l: AppLocale) = when(l) { AppLocale.ENGLISH -> "Cancel"; AppLocale.ARABIC -> "إلغاء" }
    fun confirm(l: AppLocale) = when(l) { AppLocale.ENGLISH -> "Confirm"; AppLocale.ARABIC -> "تأكيد" }
    fun delete(l: AppLocale) = when(l) { AppLocale.ENGLISH -> "Delete"; AppLocale.ARABIC -> "حذف" }
    fun developedBy(l: AppLocale) = "Developed by Rady.GFX"
    fun resumeWorkout(l: AppLocale) = when(l) { AppLocale.ENGLISH -> "Resume Workout"; AppLocale.ARABIC -> "متابعة التمرين" }
    fun timerPaused(l: AppLocale) = when(l) { AppLocale.ENGLISH -> "Timer Paused"; AppLocale.ARABIC -> "مؤقت التمرين متوقف" }

    fun imagePromptText() = "A minimalist, modern, beautiful, vector-style flat icon of a person doing [EXERCISE_NAME]. Dark background, very sleek, fitness app style, cyan and neon green highlights. 1:1 aspect ratio square. 1024x1024 resolution. No text."
}
