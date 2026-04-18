package com.atlas.logger.ui.workout

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.atlas.logger.domain.model.WorkoutExercise
import com.atlas.logger.domain.model.WorkoutSet
import com.atlas.logger.ui.theme.*
import com.atlas.logger.util.*

@Composable
fun ActiveWorkoutScreen(
    locale: AppLocale,
    weightUnit: WeightUnit,
    selectedExerciseId: Long = 0L,
    onClearSelectedExercise: () -> Unit = {},
    onAddExercise: (Long, List<Long>) -> Unit,
    onFinishWorkout: (Long) -> Unit,
    onBack: () -> Unit,
    viewModel: ActiveWorkoutViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val elapsedSeconds by viewModel.elapsedSeconds.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val haptic = remember { HapticHelper(context) }

    // Start session logic is now strictly handled by ViewModel init
    // to prevent race conditions with pre-existing sessions from intents

    // Handle exercise selection from library picker
    LaunchedEffect(selectedExerciseId) {
        if (selectedExerciseId > 0L) {
            viewModel.addExerciseToWorkout(selectedExerciseId)
            onClearSelectedExercise()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AtlasBackground)
            .statusBarsPadding()
    ) {
        var showDiscardDialog by remember { mutableStateOf(false) }

        if (showDiscardDialog) {
            AlertDialog(
                onDismissRequest = { showDiscardDialog = false },
                title = { Text(if (locale == AppLocale.ARABIC) "إلغاء التمرين؟" else "Cancel Workout?") },
                text = { Text(if (locale == AppLocale.ARABIC) "سيتم حذف جميع البيانات المسجلة لهذه الجلسة." else "This will delete all recorded data for this session.") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            scope.launch {
                                viewModel.discardWorkout()
                                onBack()
                            }
                            showDiscardDialog = false
                        },
                        colors = ButtonDefaults.textButtonColors(contentColor = AtlasError)
                    ) {
                        Text(if (locale == AppLocale.ARABIC) "إلغاء وحذف" else "DISCARD")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDiscardDialog = false }) {
                        Text(if (locale == AppLocale.ARABIC) "تراجع" else "KEEP", color = AtlasTextSecondary)
                    }
                },
                containerColor = AtlasBackground,
                titleContentColor = AtlasTextPrimary,
                textContentColor = AtlasTextSecondary
            )
        }

        // Fixed Top Bar
        WorkoutTopBar(
            locale = locale,
            onBack = onBack,
            onFinish = {
                haptic.onWorkoutCompleted()
                scope.launch {
                    val sessionId = viewModel.finishWorkout()
                    onFinishWorkout(sessionId)
                }
            },
            onDiscard = { showDiscardDialog = true },
            showFinish = uiState.exercises.isNotEmpty()
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .imePadding(),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            // ═══ Timer Display ═══
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(bottom = 4.dp)
                    ) {
                        Text(
                            text = formatTimer(elapsedSeconds),
                            style = MaterialTheme.typography.displayLarge,
                            color = AtlasTextPrimary.copy(alpha = 0.8f),
                            fontWeight = FontWeight.Black,
                            letterSpacing = (-2).sp
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        IconButton(
                            onClick = { viewModel.toggleWorkoutTimer() },
                            modifier = Modifier
                                .size(48.dp)
                                .background(AtlasSurfaceVariant, androidx.compose.foundation.shape.CircleShape)
                        ) {
                            Icon(
                                imageVector = if (uiState.isTimerRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = "Play/Pause Timer",
                                tint = AtlasPrimary,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                    Text(
                        text = if (uiState.isTimerRunning) AtlasStrings.activeSession(locale) else if (locale == AppLocale.ARABIC) "مؤقت التمرين متوقف" else "Timer Paused",
                        style = MaterialTheme.typography.labelMedium,
                        color = AtlasTextSecondary,
                        letterSpacing = 3.sp
                    )
                }
            }

            // ═══ Empty state if no exercises ═══
            if (uiState.exercises.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 24.dp)
                            .height(120.dp)
                            .background(AtlasSurface, MaterialTheme.shapes.large)
                            .border(1.dp, AtlasBorder.copy(alpha = 0.3f), MaterialTheme.shapes.large),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.FitnessCenter, contentDescription = null, tint = AtlasTextSecondary.copy(alpha = 0.3f), modifier = Modifier.size(28.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = if (locale == AppLocale.ARABIC) "أضف تمارين للبدء" else "Add exercises to get started",
                                style = MaterialTheme.typography.bodyMedium,
                                color = AtlasTextSecondary.copy(alpha = 0.5f)
                            )
                        }
                    }
                }
            }

            // ═══ Exercise Cards ═══
            items(uiState.exercises, key = { it.exercise.id }) { workoutExercise ->
                ExerciseCard(
                    workoutExercise = workoutExercise,
                    locale = locale,
                    weightUnit = weightUnit,
                    onSetUpdated = { set -> viewModel.updateSet(set) },
                    onSetToggle = { set ->
                        if (set.isCompleted) {
                            viewModel.uncompleteSet(set)
                        } else {
                            viewModel.completeSet(set)
                            haptic.onSetCompleted()
                        }
                    },
                    onAddSet = { viewModel.addSetToExercise(workoutExercise.exercise.id) },
                    onDeleteSet = { set -> viewModel.deleteSet(set) },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            // ═══ Add Exercise Button ═══
            item {
                OutlinedButton(
                    onClick = { 
                        val existingIds = uiState.exercises.map { it.exercise.id }
                        onAddExercise(uiState.sessionId, existingIds) 
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .height(52.dp),
                    border = BorderStroke(1.dp, AtlasSecondary.copy(alpha = 0.5f)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = AtlasSecondary)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = AtlasStrings.addExercise(locale),
                        style = MaterialTheme.typography.labelLarge,
                        letterSpacing = 1.sp
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // ═══ Cancel Workout Button (Bottom Center) ═══
            if (uiState.exercises.isNotEmpty()) {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        OutlinedButton(
                            onClick = { showDiscardDialog = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            border = BorderStroke(1.dp, AtlasError.copy(alpha = 0.5f)),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = AtlasError.copy(alpha = 0.8f))
                        ) {
                            Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (locale == AppLocale.ARABIC) "إلغاء التمرين" else "CANCEL WORKOUT",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun WorkoutTopBar(
    locale: AppLocale, 
    onBack: () -> Unit, 
    onFinish: () -> Unit, 
    onDiscard: () -> Unit,
    showFinish: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(AtlasSurfaceVariant.copy(alpha = 0.6f))
            .statusBarsPadding()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack, modifier = Modifier.size(36.dp)) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = AtlasTextPrimary)
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        Spacer(modifier = Modifier.weight(1f))
        
        if (showFinish) {
            TextButton(
                onClick = onFinish,
                colors = ButtonDefaults.textButtonColors(contentColor = AtlasPrimary)
            ) {
                Text(
                    text = if (locale == AppLocale.ARABIC) "إنهاء" else "FINISH",
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
            }
        } else {
            Spacer(modifier = Modifier.size(36.dp))
        }
    }
}

@Composable
private fun ExerciseCard(
    workoutExercise: WorkoutExercise,
    locale: AppLocale,
    weightUnit: WeightUnit,
    onSetUpdated: (WorkoutSet) -> Unit,
    onSetToggle: (WorkoutSet) -> Unit,
    onAddSet: () -> Unit,
    onDeleteSet: (WorkoutSet) -> Unit,
    modifier: Modifier = Modifier
) {
    val exercise = workoutExercise.exercise
    val name = if (locale == AppLocale.ARABIC) exercise.nameAr else exercise.name
    val unitLabel = UnitConverter.unitLabel(weightUnit)

    // Color per muscle group
    val groupColor = when (exercise.muscleGroup) {
        "Chest" -> Color(0xFFFF6B6B)
        "Back" -> Color(0xFF4ECDC4)
        "Legs" -> Color(0xFFFFE66D)
        "Shoulders" -> Color(0xFFA8E6CF)
        "Arms" -> Color(0xFFFF8A5C)
        "Core" -> Color(0xFF6C5CE7)
        "Cardio" -> Color(0xFFFF4757)
        else -> AtlasSecondary
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(AtlasSurface, MaterialTheme.shapes.large)
            .border(1.dp, AtlasBorder, MaterialTheme.shapes.large)
            .padding(16.dp)
    ) {
        // Exercise header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Color indicator
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(groupColor, androidx.compose.foundation.shape.CircleShape)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium,
                color = AtlasTextPrimary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Table header
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(AtlasStrings.setLabel(locale), style = MaterialTheme.typography.labelSmall, color = AtlasTextSecondary, modifier = Modifier.width(40.dp), textAlign = TextAlign.Center)
            Text(AtlasStrings.previous(locale), style = MaterialTheme.typography.labelSmall, color = AtlasTextSecondary, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
            Text(unitLabel, style = MaterialTheme.typography.labelSmall, color = AtlasTextSecondary, modifier = Modifier.width(70.dp), textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.width(6.dp))
            Text(AtlasStrings.reps(locale), style = MaterialTheme.typography.labelSmall, color = AtlasTextSecondary, modifier = Modifier.width(70.dp), textAlign = TextAlign.Center)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Set rows
        workoutExercise.sets.forEachIndexed { index, set ->
            key(set.id) {
                SetRow(
                    set = set,
                    setNumber = index + 1,
                    weightUnit = weightUnit,
                    onWeightChange = { w ->
                        val kg = UnitConverter.toKgForStorage(w, weightUnit)
                        onSetUpdated(set.copy(weight = kg))
                    },
                    onRepsChange = { r -> onSetUpdated(set.copy(reps = r)) },
                    onToggle = { onSetToggle(set) },
                    onDelete = { onDeleteSet(set) }
                )
                Spacer(modifier = Modifier.height(6.dp))
            }
        }

        // Add Set button
        TextButton(
            onClick = onAddSet,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = AtlasStrings.addSet(locale),
                style = MaterialTheme.typography.labelMedium,
                color = AtlasSecondary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun SetRow(
    set: WorkoutSet,
    setNumber: Int,
    weightUnit: WeightUnit,
    onWeightChange: (Double) -> Unit,
    onRepsChange: (Int) -> Unit,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    // Local unit override for this row
    var localUnit by remember { mutableStateOf(weightUnit) }
    
    var weightText by remember(set.id, set.weight, localUnit) {
        val display = if (localUnit == WeightUnit.LBS) UnitConverter.kgToLbs(set.weight) else set.weight
        mutableStateOf(if (display > 0) UnitConverter.formatWeight(set.weight, localUnit) else "")
    }
    var repsText by remember(set.id, set.reps) {
        mutableStateOf(if (set.reps > 0) set.reps.toString() else "")
    }

    val isFilled = weightText.isNotEmpty() && repsText.isNotEmpty()
    val bgColor = if (isFilled) AtlasSurfaceVariant else Color.Transparent
    val borderColor = if (isFilled) AtlasBorder.copy(alpha = 0.5f) else AtlasBorder.copy(alpha = 0.2f)
    val focusManager = androidx.compose.ui.platform.LocalFocusManager.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(bgColor, MaterialTheme.shapes.small)
            .border(1.dp, borderColor, MaterialTheme.shapes.small)
            .padding(vertical = 2.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Set number
        Text(
            text = setNumber.toString(),
            style = MaterialTheme.typography.labelLarge,
            color = if (isFilled) AtlasTextPrimary else AtlasTextSecondary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(36.dp),
            textAlign = TextAlign.Center
        )

        // Previous
        Text(
            text = "—",
            style = MaterialTheme.typography.bodySmall,
            color = AtlasTextSecondary,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )

        // Weight input
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.width(110.dp) // Extra width for unit toggle
        ) {
            OutlinedTextField(
                value = weightText,
                onValueChange = { newVal ->
                    weightText = newVal
                    newVal.toDoubleOrNull()?.let { 
                        // Convert to KG for storage if currently in LBS mode
                        val kgValue = if (localUnit == WeightUnit.LBS) it * 0.45359237 else it
                        onWeightChange(kgValue)
                    }
                },
                modifier = Modifier.weight(1f).height(45.dp),
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    textAlign = TextAlign.Center,
                    color = AtlasTextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = androidx.compose.ui.text.input.ImeAction.Done),
                keyboardActions = androidx.compose.foundation.text.KeyboardActions(onDone = { focusManager.clearFocus() }),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AtlasTextSecondary,
                    unfocusedBorderColor = AtlasBorder,
                    cursorColor = AtlasTextPrimary
                )
            )
            
            // Local Unit Toggle
            Text(
                text = if (localUnit == WeightUnit.KG) "KG" else "LB",
                style = MaterialTheme.typography.labelSmall,
                color = AtlasSecondary,
                fontWeight = FontWeight.Black,
                modifier = Modifier
                    .padding(start = 4.dp)
                    .clickable { localUnit = if (localUnit == WeightUnit.KG) WeightUnit.LBS else WeightUnit.KG }
                    .padding(4.dp)
            )
        }

        Spacer(modifier = Modifier.width(6.dp))

        // Reps input
        OutlinedTextField(
            value = repsText,
            onValueChange = { newVal ->
                repsText = newVal
                newVal.toIntOrNull()?.let { onRepsChange(it) }
            },
            modifier = Modifier.width(65.dp).height(45.dp),
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                textAlign = TextAlign.Center,
                color = AtlasTextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = androidx.compose.ui.text.input.ImeAction.Done),
            keyboardActions = androidx.compose.foundation.text.KeyboardActions(onDone = { focusManager.clearFocus() }),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AtlasTextSecondary,
                unfocusedBorderColor = AtlasBorder,
                cursorColor = AtlasTextPrimary
            )
        )

        Spacer(modifier = Modifier.width(4.dp))

        // Delete button
        IconButton(
            onClick = onDelete,
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                Icons.Default.Delete,
                contentDescription = null,
                tint = AtlasError.copy(alpha = 0.6f),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

private fun formatTimer(totalSeconds: Int): String {
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%02d:%02d".format(minutes, seconds)
}
