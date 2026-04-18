package com.atlas.logger.data.local

import com.atlas.logger.data.local.entity.ExerciseEntity

/**
 * Preloads 100 core exercises on first app install.
 * Each exercise has English and Arabic names + muscle group translations.
 */
object ExercisePreloader {

    fun getExercises(): List<ExerciseEntity> = listOf(
        // ═══════════════════════════════════════
        // CHEST (صدر)
        // ═══════════════════════════════════════
        ExerciseEntity(name = "Barbell Bench Press", nameAr = "ضغط الصدر بالبار", muscleGroup = "Chest", muscleGroupAr = "صدر", imageUri = "android.resource://com.atlas.logger/drawable/bench_press_icon"),
        ExerciseEntity(name = "Incline Barbell Press", nameAr = "ضغط صدر علوي بالبار", muscleGroup = "Chest", muscleGroupAr = "صدر", imageUri = "android.resource://com.atlas.logger/drawable/incline_barbell_press_icon"),
        ExerciseEntity(name = "Decline Barbell Press", nameAr = "ضغط صدر سفلي بالبار", muscleGroup = "Chest", muscleGroupAr = "صدر", imageUri = "android.resource://com.atlas.logger/drawable/decline_barbell_press_icon"),
        ExerciseEntity(name = "Dumbbell Bench Press", nameAr = "ضغط الصدر بالدمبل", muscleGroup = "Chest", muscleGroupAr = "صدر", imageUri = "android.resource://com.atlas.logger/drawable/dumbbell_bench_press_icon"),
        ExerciseEntity(name = "Incline Dumbbell Press", nameAr = "ضغط صدر علوي بالدمبل", muscleGroup = "Chest", muscleGroupAr = "صدر", imageUri = "android.resource://com.atlas.logger/drawable/incline_dumbbell_press_icon"),
        ExerciseEntity(name = "Dumbbell Fly", nameAr = "تفتيح بالدمبل", muscleGroup = "Chest", muscleGroupAr = "صدر", imageUri = "android.resource://com.atlas.logger/drawable/dumbbell_fly_icon"),
        ExerciseEntity(name = "Incline Dumbbell Fly", nameAr = "تفتيح علوي بالدمبل", muscleGroup = "Chest", muscleGroupAr = "صدر", imageUri = "android.resource://com.atlas.logger/drawable/incline_dumbbell_fly_icon"),
        ExerciseEntity(name = "Cable Crossover", nameAr = "تقاطع بالكابل", muscleGroup = "Chest", muscleGroupAr = "صدر", imageUri = "android.resource://com.atlas.logger/drawable/cable_crossover_icon"),
        ExerciseEntity(name = "Chest Dip", nameAr = "متوازي للصدر", muscleGroup = "Chest", muscleGroupAr = "صدر", imageUri = "android.resource://com.atlas.logger/drawable/chest_dip_icon"),
        ExerciseEntity(name = "Machine Chest Press", nameAr = "ضغط الصدر بالآلة", muscleGroup = "Chest", muscleGroupAr = "صدر", imageUri = "android.resource://com.atlas.logger/drawable/machine_chest_press_icon"),
        ExerciseEntity(name = "Pec Deck", nameAr = "بيك ديك", muscleGroup = "Chest", muscleGroupAr = "صدر", imageUri = "android.resource://com.atlas.logger/drawable/pec_deck_icon"),
        ExerciseEntity(name = "Push-Up", nameAr = "ضغط أرضي", muscleGroup = "Chest", muscleGroupAr = "صدر", imageUri = "android.resource://com.atlas.logger/drawable/pushup_icon"),
        ExerciseEntity(name = "Landmine Press", nameAr = "ضغط لاندماين", muscleGroup = "Chest", muscleGroupAr = "صدر", imageUri = "android.resource://com.atlas.logger/drawable/landmine_press_icon"),

        // ═══════════════════════════════════════
        // BACK (ظهر)
        // ═══════════════════════════════════════
        ExerciseEntity(name = "Conventional Deadlift", nameAr = "الرفعة الميتة التقليدية", muscleGroup = "Back", muscleGroupAr = "ظهر", imageUri = "android.resource://com.atlas.logger/drawable/deadlift_icon"),
        ExerciseEntity(name = "Barbell Row", nameAr = "تجديف بالبار", muscleGroup = "Back", muscleGroupAr = "ظهر", imageUri = "android.resource://com.atlas.logger/drawable/barbell_row_icon"),
        ExerciseEntity(name = "Dumbbell Row", nameAr = "تجديف بالدمبل", muscleGroup = "Back", muscleGroupAr = "ظهر", imageUri = "android.resource://com.atlas.logger/drawable/dumbbell_row_icon"),
        ExerciseEntity(name = "Pull-Up", nameAr = "سحب علوي (عقلة)", muscleGroup = "Back", muscleGroupAr = "ظهر", imageUri = "android.resource://com.atlas.logger/drawable/pullup_icon"),
        ExerciseEntity(name = "Chin-Up", nameAr = "سحب بقبضة معكوسة", muscleGroup = "Back", muscleGroupAr = "ظهر", imageUri = "android.resource://com.atlas.logger/drawable/chinup_icon"),
        ExerciseEntity(name = "Lat Pulldown", nameAr = "سحب ظهر علوي", muscleGroup = "Back", muscleGroupAr = "ظهر", imageUri = "android.resource://com.atlas.logger/drawable/lat_pulldown_icon"),
        ExerciseEntity(name = "Seated Cable Row", nameAr = "تجديف بالكابل جالس", muscleGroup = "Back", muscleGroupAr = "ظهر", imageUri = "android.resource://com.atlas.logger/drawable/seated_cable_row_icon"),
        ExerciseEntity(name = "T-Bar Row", nameAr = "تجديف تي بار", muscleGroup = "Back", muscleGroupAr = "ظهر", imageUri = "android.resource://com.atlas.logger/drawable/tbar_row_icon"),
        ExerciseEntity(name = "Cable Pullover", nameAr = "سحب خلفي بالكابل", muscleGroup = "Back", muscleGroupAr = "ظهر", imageUri = "android.resource://com.atlas.logger/drawable/cable_pullover_icon"),
        ExerciseEntity(name = "Straight Arm Pulldown", nameAr = "سحب بذراع مستقيم", muscleGroup = "Back", muscleGroupAr = "ظهر", imageUri = "android.resource://com.atlas.logger/drawable/straight_arm_pulldown_icon"),
        ExerciseEntity(name = "Rack Pull", nameAr = "رفعة ميتة من الرف", muscleGroup = "Back", muscleGroupAr = "ظهر", imageUri = "android.resource://com.atlas.logger/drawable/rack_pull_icon"),
        ExerciseEntity(name = "Meadows Row", nameAr = "تجديف ميدوز", muscleGroup = "Back", muscleGroupAr = "ظهر", imageUri = "android.resource://com.atlas.logger/drawable/meadows_row_icon"),
        ExerciseEntity(name = "Machine Row", nameAr = "تجديف بالآلة", muscleGroup = "Back", muscleGroupAr = "ظهر", imageUri = "android.resource://com.atlas.logger/drawable/machine_row_icon"),
        ExerciseEntity(name = "Hyperextension", nameAr = "تمديد الظهر", muscleGroup = "Back", muscleGroupAr = "ظهر", imageUri = "android.resource://com.atlas.logger/drawable/hyperextension_icon"),
        ExerciseEntity(name = "Muscle-Up", nameAr = "مسل أب (عقلة متقدمة)", muscleGroup = "Back", muscleGroupAr = "ظهر", imageUri = "android.resource://com.atlas.logger/drawable/muscle_up_icon"),

        // ═══════════════════════════════════════
        // LEGS (أرجل)
        // ═══════════════════════════════════════
        ExerciseEntity(name = "Barbell Back Squat", nameAr = "القرفصاء بالبار", muscleGroup = "Legs", muscleGroupAr = "أرجل", imageUri = "android.resource://com.atlas.logger/drawable/squat_icon"),
        ExerciseEntity(name = "Front Squat", nameAr = "قرفصاء أمامية", muscleGroup = "Legs", muscleGroupAr = "أرجل", imageUri = "android.resource://com.atlas.logger/drawable/front_squat_icon"),
        ExerciseEntity(name = "Leg Press", nameAr = "مكبس الأرجل", muscleGroup = "Legs", muscleGroupAr = "أرجل", imageUri = "android.resource://com.atlas.logger/drawable/leg_press_icon"),
        ExerciseEntity(name = "Romanian Deadlift", nameAr = "الرفعة الميتة الرومانية", muscleGroup = "Legs", muscleGroupAr = "أرجل", imageUri = "android.resource://com.atlas.logger/drawable/romanian_deadlift_icon"),
        ExerciseEntity(name = "Leg Extension", nameAr = "تمديد الأرجل", muscleGroup = "Legs", muscleGroupAr = "أرجل", imageUri = "android.resource://com.atlas.logger/drawable/leg_extension_icon"),
        ExerciseEntity(name = "Leg Curl", nameAr = "ثني الأرجل", muscleGroup = "Legs", muscleGroupAr = "أرجل", imageUri = "android.resource://com.atlas.logger/drawable/leg_curl_icon"),
        ExerciseEntity(name = "Bulgarian Split Squat", nameAr = "قرفصاء بلغارية", muscleGroup = "Legs", muscleGroupAr = "أرجل", imageUri = "android.resource://com.atlas.logger/drawable/bulgarian_split_squat_icon"),
        ExerciseEntity(name = "Walking Lunge", nameAr = "اندفاع بالمشي", muscleGroup = "Legs", muscleGroupAr = "أرجل", imageUri = "android.resource://com.atlas.logger/drawable/walking_lunge_icon"),
        ExerciseEntity(name = "Hack Squat", nameAr = "قرفصاء هاك", muscleGroup = "Legs", muscleGroupAr = "أرجل", imageUri = "android.resource://com.atlas.logger/drawable/hack_squat_icon"),
        ExerciseEntity(name = "Goblet Squat", nameAr = "قرفصاء بالدمبل", muscleGroup = "Legs", muscleGroupAr = "أرجل", imageUri = "android.resource://com.atlas.logger/drawable/goblet_squat_icon"),
        ExerciseEntity(name = "Hip Thrust", nameAr = "دفع الورك", muscleGroup = "Legs", muscleGroupAr = "أرجل", imageUri = "android.resource://com.atlas.logger/drawable/hip_thrust_icon"),
        ExerciseEntity(name = "Sumo Deadlift", nameAr = "رفعة ميتة بوضع سومو", muscleGroup = "Legs", muscleGroupAr = "أرجل", imageUri = "android.resource://com.atlas.logger/drawable/sumo_deadlift_icon"),
        ExerciseEntity(name = "Standing Calf Raise", nameAr = "رفع السمانة واقف", muscleGroup = "Legs", muscleGroupAr = "أرجل", imageUri = "android.resource://com.atlas.logger/drawable/standing_calf_raise_icon"),
        ExerciseEntity(name = "Seated Calf Raise", nameAr = "رفع السمانة جالس", muscleGroup = "Legs", muscleGroupAr = "أرجل", imageUri = "android.resource://com.atlas.logger/drawable/seated_calf_raise_icon"),
        ExerciseEntity(name = "Smith Machine Squat", nameAr = "قرفصاء سميث", muscleGroup = "Legs", muscleGroupAr = "أرجل", imageUri = "android.resource://com.atlas.logger/drawable/smith_machine_squat_icon"),
        ExerciseEntity(name = "Leg Press Calf Raise", nameAr = "رفع سمانة بمكبس الأرجل", muscleGroup = "Legs", muscleGroupAr = "أرجل", imageUri = "android.resource://com.atlas.logger/drawable/leg_press_calf_raise_icon"),
        ExerciseEntity(name = "Glute Kickback", nameAr = "ركل خلفي للأرداف", muscleGroup = "Legs", muscleGroupAr = "أرجل", imageUri = "android.resource://com.atlas.logger/drawable/glute_kickback_icon"),
        ExerciseEntity(name = "Pistol Squat", nameAr = "قرفصاء المسدس", muscleGroup = "Legs", muscleGroupAr = "أرجل", imageUri = "android.resource://com.atlas.logger/drawable/pistol_squat_icon"),

        // ═══════════════════════════════════════
        // SHOULDERS (أكتاف)
        // ═══════════════════════════════════════
        ExerciseEntity(name = "Overhead Press", nameAr = "ضغط كتف علوي بالبار", muscleGroup = "Shoulders", muscleGroupAr = "أكتاف", imageUri = "android.resource://com.atlas.logger/drawable/overhead_press_icon"),
        ExerciseEntity(name = "Dumbbell Shoulder Press", nameAr = "ضغط كتف بالدمبل", muscleGroup = "Shoulders", muscleGroupAr = "أكتاف", imageUri = "android.resource://com.atlas.logger/drawable/dumbbell_shoulder_press_icon"),
        ExerciseEntity(name = "Arnold Press", nameAr = "ضغط أرنولد", muscleGroup = "Shoulders", muscleGroupAr = "أكتاف", imageUri = "android.resource://com.atlas.logger/drawable/arnold_press_icon"),
        ExerciseEntity(name = "Lateral Raise", nameAr = "رفع جانبي", muscleGroup = "Shoulders", muscleGroupAr = "أكتاف", imageUri = "android.resource://com.atlas.logger/drawable/lateral_raise_icon"),
        ExerciseEntity(name = "Front Raise", nameAr = "رفع أمامي", muscleGroup = "Shoulders", muscleGroupAr = "أكتاف", imageUri = "android.resource://com.atlas.logger/drawable/front_raise_icon"),
        ExerciseEntity(name = "Rear Delt Fly", nameAr = "تفتيح خلفي للكتف", muscleGroup = "Shoulders", muscleGroupAr = "أكتاف", imageUri = "android.resource://com.atlas.logger/drawable/rear_delt_fly_icon"),
        ExerciseEntity(name = "Face Pull", nameAr = "سحب للوجه", muscleGroup = "Shoulders", muscleGroupAr = "أكتاف", imageUri = "android.resource://com.atlas.logger/drawable/face_pull_icon"),
        ExerciseEntity(name = "Upright Row", nameAr = "تجديف عمودي", muscleGroup = "Shoulders", muscleGroupAr = "أكتاف", imageUri = "android.resource://com.atlas.logger/drawable/upright_row_icon"),
        ExerciseEntity(name = "Machine Shoulder Press", nameAr = "ضغط كتف بالآلة", muscleGroup = "Shoulders", muscleGroupAr = "أكتاف", imageUri = "android.resource://com.atlas.logger/drawable/machine_shoulder_press_icon"),
        ExerciseEntity(name = "Cable Lateral Raise", nameAr = "رفع جانبي بالكابل", muscleGroup = "Shoulders", muscleGroupAr = "أكتاف", imageUri = "android.resource://com.atlas.logger/drawable/cable_lateral_raise_icon"),
        ExerciseEntity(name = "Dumbbell Shrug", nameAr = "هز الأكتاف بالدمبل", muscleGroup = "Shoulders", muscleGroupAr = "أكتاف", imageUri = "android.resource://com.atlas.logger/drawable/dumbbell_shrug_icon"),
        ExerciseEntity(name = "Barbell Shrug", nameAr = "هز الأكتاف بالبار", muscleGroup = "Shoulders", muscleGroupAr = "أكتاف", imageUri = "android.resource://com.atlas.logger/drawable/barbell_shrug_icon"),
        ExerciseEntity(name = "Turkish Get-Up", nameAr = "نهوض تركي", muscleGroup = "Shoulders", muscleGroupAr = "أكتاف", imageUri = "android.resource://com.atlas.logger/drawable/turkish_getup_icon"),

        // ═══════════════════════════════════════
        // ARMS — BICEPS (باي)
        // ═══════════════════════════════════════
        ExerciseEntity(name = "Barbell Curl", nameAr = "ثني البار للباي", muscleGroup = "Arms", muscleGroupAr = "ذراع", imageUri = "android.resource://com.atlas.logger/drawable/barbell_curl_icon"),
        ExerciseEntity(name = "Dumbbell Curl", nameAr = "ثني الدمبل للباي", muscleGroup = "Arms", muscleGroupAr = "ذراع", imageUri = "android.resource://com.atlas.logger/drawable/dumbbell_curl_icon"),
        ExerciseEntity(name = "Hammer Curl", nameAr = "ثني هامر", muscleGroup = "Arms", muscleGroupAr = "ذراع", imageUri = "android.resource://com.atlas.logger/drawable/hammer_curl_icon"),
        ExerciseEntity(name = "Preacher Curl", nameAr = "ثني على المسند", muscleGroup = "Arms", muscleGroupAr = "ذراع", imageUri = "android.resource://com.atlas.logger/drawable/preacher_curl_icon"),
        ExerciseEntity(name = "Concentration Curl", nameAr = "ثني تركيز", muscleGroup = "Arms", muscleGroupAr = "ذراع", imageUri = "android.resource://com.atlas.logger/drawable/concentration_curl_icon"),
        ExerciseEntity(name = "Cable Curl", nameAr = "ثني بالكابل", muscleGroup = "Arms", muscleGroupAr = "ذراع", imageUri = "android.resource://com.atlas.logger/drawable/cable_curl_icon"),
        ExerciseEntity(name = "Incline Dumbbell Curl", nameAr = "ثني بالدمبل مائل", muscleGroup = "Arms", muscleGroupAr = "ذراع", imageUri = "android.resource://com.atlas.logger/drawable/incline_dumbbell_curl_icon"),
        ExerciseEntity(name = "EZ-Bar Curl", nameAr = "ثني بالبار المتعرج", muscleGroup = "Arms", muscleGroupAr = "ذراع", imageUri = "android.resource://com.atlas.logger/drawable/ez_bar_curl_icon"),

        // ═══════════════════════════════════════
        // ARMS — TRICEPS (تراي)
        // ═══════════════════════════════════════
        ExerciseEntity(name = "Close-Grip Bench Press", nameAr = "ضغط بقبضة ضيقة", muscleGroup = "Arms", muscleGroupAr = "ذراع", imageUri = "android.resource://com.atlas.logger/drawable/close_grip_bench_press_icon"),
        ExerciseEntity(name = "Tricep Pushdown", nameAr = "دفع تراي للأسفل", muscleGroup = "Arms", muscleGroupAr = "ذراع", imageUri = "android.resource://com.atlas.logger/drawable/tricep_pushdown_icon"),
        ExerciseEntity(name = "Overhead Tricep Extension", nameAr = "تمديد تراي خلف الرأس", muscleGroup = "Arms", muscleGroupAr = "ذراع", imageUri = "android.resource://com.atlas.logger/drawable/overhead_tricep_extension_icon"),
        ExerciseEntity(name = "Skull Crusher", nameAr = "سكل كراشر", muscleGroup = "Arms", muscleGroupAr = "ذراع", imageUri = "android.resource://com.atlas.logger/drawable/skull_crusher_icon"),
        ExerciseEntity(name = "Tricep Dip", nameAr = "متوازي للتراي", muscleGroup = "Arms", muscleGroupAr = "ذراع", imageUri = "android.resource://com.atlas.logger/drawable/tricep_dip_icon"),
        ExerciseEntity(name = "Diamond Push-Up", nameAr = "ضغط ماسي", muscleGroup = "Arms", muscleGroupAr = "ذراع", imageUri = "android.resource://com.atlas.logger/drawable/diamond_pushup_icon"),
        ExerciseEntity(name = "Cable Overhead Extension", nameAr = "تمديد تراي بالكابل", muscleGroup = "Arms", muscleGroupAr = "ذراع", imageUri = "android.resource://com.atlas.logger/drawable/cable_overhead_extension_icon"),
        ExerciseEntity(name = "Tricep Kickback", nameAr = "ركل خلفي للتراي", muscleGroup = "Arms", muscleGroupAr = "ذراع", imageUri = "android.resource://com.atlas.logger/drawable/tricep_kickback_icon"),

        // ═══════════════════════════════════════
        // ARMS — FOREARMS (ساعد)
        // ═══════════════════════════════════════
        ExerciseEntity(name = "Wrist Curl", nameAr = "ثني الرسغ", muscleGroup = "Arms", muscleGroupAr = "ذراع", imageUri = "android.resource://com.atlas.logger/drawable/wrist_curl_icon"),
        ExerciseEntity(name = "Reverse Wrist Curl", nameAr = "ثني الرسغ المعكوس", muscleGroup = "Arms", muscleGroupAr = "ذراع", imageUri = "android.resource://com.atlas.logger/drawable/reverse_wrist_curl_icon"),

        // ═══════════════════════════════════════
        // CORE (بطن)
        // ═══════════════════════════════════════
        ExerciseEntity(name = "Crunch", nameAr = "كرانش", muscleGroup = "Core", muscleGroupAr = "بطن", imageUri = "android.resource://com.atlas.logger/drawable/crunch_icon"),
        ExerciseEntity(name = "Hanging Leg Raise", nameAr = "رفع الأرجل معلقاً", muscleGroup = "Core", muscleGroupAr = "بطن", imageUri = "android.resource://com.atlas.logger/drawable/hanging_leg_raise_icon"),
        ExerciseEntity(name = "Cable Crunch", nameAr = "كرانش بالكابل", muscleGroup = "Core", muscleGroupAr = "بطن", imageUri = "android.resource://com.atlas.logger/drawable/cable_crunch_icon"),
        ExerciseEntity(name = "Plank", nameAr = "بلانك", muscleGroup = "Core", muscleGroupAr = "بطن", imageUri = "android.resource://com.atlas.logger/drawable/plank_icon"),
        ExerciseEntity(name = "Ab Wheel Rollout", nameAr = "عجلة البطن", muscleGroup = "Core", muscleGroupAr = "بطن", imageUri = "android.resource://com.atlas.logger/drawable/ab_wheel_rollout_icon"),
        ExerciseEntity(name = "Russian Twist", nameAr = "لف روسي", muscleGroup = "Core", muscleGroupAr = "بطن", imageUri = "android.resource://com.atlas.logger/drawable/russian_twist_icon"),
        ExerciseEntity(name = "Leg Raise", nameAr = "رفع الأرجل", muscleGroup = "Core", muscleGroupAr = "بطن", imageUri = "android.resource://com.atlas.logger/drawable/leg_raise_icon"),
        ExerciseEntity(name = "Decline Sit-Up", nameAr = "رفع بطن مائل", muscleGroup = "Core", muscleGroupAr = "بطن", imageUri = "android.resource://com.atlas.logger/drawable/decline_situp_icon"),
        ExerciseEntity(name = "Mountain Climber", nameAr = "تسلق الجبل", muscleGroup = "Core", muscleGroupAr = "بطن", imageUri = "android.resource://com.atlas.logger/drawable/mountain_climber_icon"),
        ExerciseEntity(name = "Side Plank", nameAr = "بلانك جانبي", muscleGroup = "Core", muscleGroupAr = "بطن", imageUri = "android.resource://com.atlas.logger/drawable/side_plank_icon"),
        ExerciseEntity(name = "Dead Bug", nameAr = "الحشرة الميتة", muscleGroup = "Core", muscleGroupAr = "بطن", imageUri = "android.resource://com.atlas.logger/drawable/dead_bug_icon"),
        ExerciseEntity(name = "Pallof Press", nameAr = "ضغط بالوف", muscleGroup = "Core", muscleGroupAr = "بطن", imageUri = "android.resource://com.atlas.logger/drawable/pallof_press_icon"),
        ExerciseEntity(name = "Dragon Flag", nameAr = "دراجون فلاج", muscleGroup = "Core", muscleGroupAr = "بطن", imageUri = "android.resource://com.atlas.logger/drawable/dragon_flag_icon"),

        // ═══════════════════════════════════════
        // CARDIO (كارديو)
        // ═══════════════════════════════════════
        ExerciseEntity(name = "Treadmill Run", nameAr = "جري على السير", muscleGroup = "Cardio", muscleGroupAr = "كارديو", imageUri = "android.resource://com.atlas.logger/drawable/treadmill_icon"),
        ExerciseEntity(name = "Stationary Bike", nameAr = "دراجة ثابتة", muscleGroup = "Cardio", muscleGroupAr = "كارديو", imageUri = "android.resource://com.atlas.logger/drawable/stationary_bike_icon"),
        ExerciseEntity(name = "Rowing Machine", nameAr = "آلة التجديف", muscleGroup = "Cardio", muscleGroupAr = "كارديو", imageUri = "android.resource://com.atlas.logger/drawable/rowing_machine_icon"),
        ExerciseEntity(name = "Stair Climber", nameAr = "آلة الدرج", muscleGroup = "Cardio", muscleGroupAr = "كارديو", imageUri = "android.resource://com.atlas.logger/drawable/stair_climber_icon"),
        ExerciseEntity(name = "Elliptical", nameAr = "الإهليلجي", muscleGroup = "Cardio", muscleGroupAr = "كارديو", imageUri = "android.resource://com.atlas.logger/drawable/elliptical_icon"),
        ExerciseEntity(name = "Jump Rope", nameAr = "نط الحبل", muscleGroup = "Cardio", muscleGroupAr = "كارديو", imageUri = "android.resource://com.atlas.logger/drawable/jump_rope_icon"),
        ExerciseEntity(name = "Battle Ropes", nameAr = "حبال المعركة", muscleGroup = "Cardio", muscleGroupAr = "كارديو", imageUri = "android.resource://com.atlas.logger/drawable/battle_ropes_icon"),
        ExerciseEntity(name = "Box Jump", nameAr = "قفز على الصندوق", muscleGroup = "Cardio", muscleGroupAr = "كارديو", imageUri = "android.resource://com.atlas.logger/drawable/box_jump_icon"),
        ExerciseEntity(name = "Burpee", nameAr = "بيربي", muscleGroup = "Cardio", muscleGroupAr = "كارديو", imageUri = "android.resource://com.atlas.logger/drawable/burpee_icon"),
        ExerciseEntity(name = "Kettlebell Swing", nameAr = "أرجحة كيتل بل", muscleGroup = "Cardio", muscleGroupAr = "كارديو", imageUri = "android.resource://com.atlas.logger/drawable/kettlebell_swing_icon"),
    )
}
