package com.atlas.logger.ui.library

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.atlas.logger.domain.model.Exercise
import com.atlas.logger.ui.theme.*
import com.atlas.logger.util.*

@Composable
fun ExerciseLibraryScreen(
    locale: AppLocale,
    weightUnit: WeightUnit,
    isPickerMode: Boolean,
    onExerciseSelected: (Long) -> Unit,
    onBack: () -> Unit,
    excludedIds: List<Long> = emptyList(),
    viewModel: ExerciseLibraryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showCreateDialog by remember { mutableStateOf(false) }

    LaunchedEffect(excludedIds) {
        viewModel.setExcludedIds(excludedIds)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AtlasBackground)
    ) {
        // ═══ Top Bar ═══
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(AtlasSurfaceVariant.copy(alpha = 0.6f))
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (isPickerMode) {
                    IconButton(onClick = onBack, modifier = Modifier.size(36.dp)) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = AtlasTextPrimary)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = if (isPickerMode) AtlasStrings.addExercise(locale) else AtlasStrings.theAtlas(locale),
                    style = MaterialTheme.typography.titleLarge,
                    color = if (isPickerMode) AtlasTextPrimary else AtlasPrimary,
                    fontWeight = FontWeight.Black,
                    letterSpacing = if (isPickerMode) 0.sp else (-1).sp
                )
            }

            // Add custom exercise button
            IconButton(onClick = { showCreateDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Exercise", tint = AtlasPrimary)
            }
        }

        // ═══ Search Bar ═══
        OutlinedTextField(
            value = uiState.searchQuery,
            onValueChange = { viewModel.onSearchQueryChange(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            placeholder = {
                Text(
                    text = AtlasStrings.searchExercises(locale),
                    color = AtlasTextSecondary
                )
            },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = null, tint = AtlasTextSecondary)
            },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AtlasPrimary,
                unfocusedBorderColor = AtlasBorder,
                focusedContainerColor = AtlasSurface,
                unfocusedContainerColor = AtlasSurface,
                cursorColor = AtlasPrimary
            ),
            shape = MaterialTheme.shapes.medium
        )

        // ═══ Muscle Filter Chips ═══
        LazyRow(
            modifier = Modifier.padding(vertical = 8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                FilterChip(
                    selected = uiState.selectedMuscleGroup == null,
                    onClick = { viewModel.onMuscleGroupSelected(null) },
                    label = { Text(AtlasStrings.allMuscles(locale)) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = AtlasPrimary,
                        selectedLabelColor = AtlasTextOnPrimary,
                        containerColor = AtlasSurface,
                        labelColor = AtlasTextSecondary
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        borderColor = AtlasBorder,
                        selectedBorderColor = AtlasPrimary,
                        enabled = true,
                        selected = uiState.selectedMuscleGroup == null
                    )
                )
            }

            items(viewModel.muscleGroups) { group ->
                val groupLabel = getMuscleGroupLabel(group, locale)
                FilterChip(
                    selected = uiState.selectedMuscleGroup == group,
                    onClick = { viewModel.onMuscleGroupSelected(group) },
                    label = { Text(groupLabel) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = AtlasPrimary,
                        selectedLabelColor = AtlasTextOnPrimary,
                        containerColor = AtlasSurface,
                        labelColor = AtlasTextSecondary
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        borderColor = AtlasBorder,
                        selectedBorderColor = AtlasPrimary,
                        enabled = true,
                        selected = uiState.selectedMuscleGroup == group
                    )
                )
            }
        }

        // ═══ Exercise Count ═══
        Text(
            text = "${uiState.exercises.size} ${AtlasStrings.exercises(locale)}",
            style = MaterialTheme.typography.labelSmall,
            color = AtlasTextSecondary,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
        )

        // ═══ Exercise List ═══
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val displayExercises = if (isPickerMode) {
                // Filter out exercises already in workout (Logic handled by VM or UI)
                uiState.exercises
            } else {
                uiState.exercises
            }

            items(displayExercises, key = { it.id }) { exercise ->
                val name = if (locale == AppLocale.ARABIC) exercise.nameAr else exercise.name
                val group = if (locale == AppLocale.ARABIC) exercise.muscleGroupAr else exercise.muscleGroup
                
                ExerciseListItem(
                    exercise = exercise,
                    locale = locale,
                    isPickerMode = isPickerMode,
                    onClick = {
                        onExerciseSelected(exercise.id)
                    },
                    onImageClick = { viewModel.onExerciseImageClick(exercise) },
                    onDeleteClick = if (exercise.isCustom) {
                        { viewModel.deleteExercise(exercise) }
                    } else null
                )
            }
        }
    }

    // ═══ Create Exercise Dialog ═══
    if (showCreateDialog) {
        CreateExerciseDialog(
            locale = locale,
            onDismiss = { showCreateDialog = false },
            onConfirm = { name, nameAr, muscleGroup, muscleGroupAr, imageUri ->
                viewModel.createExercise(name, nameAr, muscleGroup, muscleGroupAr, imageUri)
                showCreateDialog = false
            }
        )
    }

    // ═══ Image Picker for existing exercises ═══
    val imagePickExercise = uiState.imagePickExercise
    if (imagePickExercise != null) {
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            if (uri != null) {
                viewModel.updateExerciseImage(imagePickExercise.id, uri.toString())
            }
            viewModel.clearImagePick()
        }
        LaunchedEffect(imagePickExercise) {
            launcher.launch("image/*")
        }
    }
}

@Composable
private fun ExerciseListItem(
    exercise: Exercise,
    locale: AppLocale,
    isPickerMode: Boolean,
    onClick: () -> Unit,
    onImageClick: () -> Unit,
    onDeleteClick: (() -> Unit)? = null
) {
    val name = if (locale == AppLocale.ARABIC) exercise.nameAr else exercise.name
    val group = if (locale == AppLocale.ARABIC) exercise.muscleGroupAr else exercise.muscleGroup

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

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(AtlasSurface, MaterialTheme.shapes.large)
            .border(1.dp, AtlasBorder, MaterialTheme.shapes.large)
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Exercise image or colored icon
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(MaterialTheme.shapes.medium)
                .background(groupColor.copy(alpha = 0.15f))
                .clickable { onImageClick() },
            contentAlignment = Alignment.Center
        ) {
            if (exercise.imageUri != null) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(exercise.imageUri)
                        .build(),
                    contentDescription = name,
                    modifier = Modifier.fillMaxSize().clip(MaterialTheme.shapes.medium),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    Icons.Default.FitnessCenter,
                    contentDescription = null,
                    tint = groupColor,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleSmall,
                color = AtlasTextPrimary,
                fontWeight = FontWeight.Bold
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(groupColor, androidx.compose.foundation.shape.CircleShape)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = group.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = AtlasTextSecondary,
                    letterSpacing = 1.sp
                )
            }
        }

        // Action Buttons
        if (onDeleteClick != null) {
            var showConfirm by remember { mutableStateOf(false) }
            
            if (showConfirm) {
                IconButton(
                    onClick = {
                        onDeleteClick()
                        showConfirm = false
                    },
                    modifier = Modifier.size(32.dp).background(AtlasError.copy(alpha=0.15f), MaterialTheme.shapes.small)
                ) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = "Confirm Delete",
                        tint = AtlasError,
                        modifier = Modifier.size(18.dp)
                    )
                }
            } else {
                IconButton(
                    onClick = { showConfirm = true },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = AtlasTextSecondary.copy(alpha = 0.5f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        // Only show add button in picker mode
        if (isPickerMode) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(AtlasPrimary.copy(alpha = 0.15f), MaterialTheme.shapes.small),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add",
                    tint = AtlasPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
private fun CreateExerciseDialog(
    locale: AppLocale,
    onDismiss: () -> Unit,
    onConfirm: (name: String, nameAr: String, muscleGroup: String, muscleGroupAr: String, imageUri: String?) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var selectedGroup by remember { mutableStateOf("Chest") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    val muscleGroups = listOf(
        "Chest" to "صدر",
        "Back" to "ظهر",
        "Legs" to "أرجل",
        "Shoulders" to "أكتاف",
        "Arms" to "ذراع",
        "Core" to "بطن",
        "Cardio" to "كارديو"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = AtlasBackground,
        tonalElevation = 0.dp,
        title = {
            Text(
                text = if (locale == AppLocale.ARABIC) "إضافة تمرين جديد" else "Create Exercise",
                color = AtlasTextPrimary,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                // Exercise Name (Single Field)
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(if (locale == AppLocale.ARABIC) "اسم التمرين" else "Exercise Name", color = AtlasTextSecondary) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AtlasTextSecondary,
                        unfocusedBorderColor = AtlasBorder,
                        cursorColor = AtlasTextPrimary,
                        focusedTextColor = AtlasTextPrimary,
                        unfocusedTextColor = AtlasTextPrimary
                    )
                )

                // Muscle Group dropdown
                Text(
                    text = if (locale == AppLocale.ARABIC) "مجموعة العضلات" else "Muscle Group",
                    style = MaterialTheme.typography.labelMedium,
                    color = AtlasTextSecondary
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(muscleGroups.size) { index ->
                        val (en, ar) = muscleGroups[index]
                        FilterChip(
                            selected = selectedGroup == en,
                            onClick = { selectedGroup = en },
                            label = { Text(if (locale == AppLocale.ARABIC) ar else en, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = AtlasSurfaceVariant,
                                selectedLabelColor = AtlasTextPrimary,
                                containerColor = AtlasSurface,
                                labelColor = AtlasTextSecondary
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                selectedBorderColor = AtlasBorder,
                                borderColor = AtlasBorder.copy(alpha = 0.5f),
                                enabled = true,
                                selected = selectedGroup == en
                            )
                        )
                    }
                }

                // Image picker button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AtlasSurface, MaterialTheme.shapes.medium)
                        .border(1.dp, AtlasBorder, MaterialTheme.shapes.medium)
                        .clickable { imageLauncher.launch("image/*") }
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.CameraAlt,
                        contentDescription = null,
                        tint = AtlasTextSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = if (imageUri != null) {
                            if (locale == AppLocale.ARABIC) "✓ تم اختيار صورة" else "✓ Image selected"
                        } else {
                            if (locale == AppLocale.ARABIC) "إضافة صورة (اختياري)" else "Add Photo (optional)"
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (imageUri != null) AtlasTextPrimary else AtlasTextSecondary
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (name.isNotBlank()) {
                        val groupAr = muscleGroups.find { it.first == selectedGroup }?.second ?: selectedGroup
                        onConfirm(name, name, selectedGroup, groupAr, imageUri?.toString())
                    }
                },
                enabled = name.isNotBlank(),
                colors = ButtonDefaults.textButtonColors(contentColor = AtlasTextPrimary)
            ) {
                Text(AtlasStrings.confirm(locale))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(AtlasStrings.cancel(locale), color = AtlasTextSecondary)
            }
        }
    )
}

private fun getMuscleGroupLabel(group: String, locale: AppLocale): String {
    return when (group) {
        "Chest" -> AtlasStrings.chest(locale)
        "Back" -> AtlasStrings.back(locale)
        "Legs" -> AtlasStrings.legs(locale)
        "Shoulders" -> AtlasStrings.shoulders(locale)
        "Arms" -> AtlasStrings.arms(locale)
        "Core" -> AtlasStrings.core(locale)
        "Cardio" -> AtlasStrings.cardio(locale)
        else -> group
    }
}
