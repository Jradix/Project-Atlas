package com.atlas.logger.ui.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.*
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.atlas.logger.ui.dashboard.DashboardScreen
import com.atlas.logger.ui.history.HistoryScreen
import com.atlas.logger.ui.library.ExerciseLibraryScreen
import com.atlas.logger.ui.settings.SettingsScreen
import com.atlas.logger.ui.summary.WorkoutSummaryScreen
import com.atlas.logger.ui.workout.ActiveWorkoutScreen
import com.atlas.logger.util.AppLocale
import com.atlas.logger.util.WeightUnit

/**
 * Navigation routes for the app.
 */
object AtlasRoutes {
    const val DASHBOARD = "dashboard"
    const val WORKOUT = "workout"
    const val LIBRARY = "library"
    const val LIBRARY_PICKER = "library_picker/{workoutId}"
    const val SETTINGS = "settings"
    const val SUMMARY = "summary/{sessionId}"
    const val HISTORY = "history"

    fun libraryPicker(workoutId: Long) = "library_picker/$workoutId"
    fun summary(sessionId: Long) = "summary/$sessionId"
}

@Composable
fun AtlasNavHost(
    navController: NavHostController,
    locale: AppLocale,
    weightUnit: WeightUnit,
    onLocaleChange: (AppLocale) -> Unit,
    onWeightUnitChange: (WeightUnit) -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = AtlasRoutes.DASHBOARD,
        enterTransition = { fadeIn(animationSpec = tween(200)) },
        exitTransition = { fadeOut(animationSpec = tween(200)) },
    ) {
        var tempExcludedIds by mutableStateOf<List<Long>>(emptyList())

        composable(AtlasRoutes.DASHBOARD) {
            DashboardScreen(
                locale = locale,
                weightUnit = weightUnit,
                onLocaleChange = onLocaleChange,
                onWeightUnitChange = onWeightUnitChange,
                onStartWorkout = {
                    navController.navigate(AtlasRoutes.WORKOUT) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onViewSession = { sessionId ->
                    navController.navigate(AtlasRoutes.summary(sessionId))
                },
                onOpenSettings = {
                    navController.navigate(AtlasRoutes.SETTINGS)
                },
                onViewAllHistory = {
                    navController.navigate(AtlasRoutes.HISTORY)
                }
            )
        }

        composable(AtlasRoutes.WORKOUT) { backStackEntry ->
            // Listen for exercise selection result from library picker
            val selectedExerciseId = backStackEntry.savedStateHandle
                .getStateFlow<Long>("selected_exercise_id", 0L)
                .collectAsState()

            ActiveWorkoutScreen(
                locale = locale,
                weightUnit = weightUnit,
                selectedExerciseId = selectedExerciseId.value,
                onClearSelectedExercise = {
                    backStackEntry.savedStateHandle["selected_exercise_id"] = 0L
                },
                onAddExercise = { workoutId, excluded ->
                    tempExcludedIds = excluded
                    navController.navigate(AtlasRoutes.libraryPicker(workoutId))
                },
                onFinishWorkout = { sessionId ->
                    navController.navigate(AtlasRoutes.summary(sessionId)) {
                        popUpTo(AtlasRoutes.DASHBOARD)
                    }
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(AtlasRoutes.LIBRARY) {
            ExerciseLibraryScreen(
                locale = locale,
                weightUnit = weightUnit,
                isPickerMode = false,
                onExerciseSelected = { },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = AtlasRoutes.LIBRARY_PICKER,
            arguments = listOf(navArgument("workoutId") { type = NavType.LongType })
        ) {
            ExerciseLibraryScreen(
                locale = locale,
                weightUnit = weightUnit,
                isPickerMode = true,
                excludedIds = tempExcludedIds,
                onExerciseSelected = { exerciseId ->
                    // Pass result back to the workout screen
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("selected_exercise_id", exerciseId)
                    tempExcludedIds = emptyList()
                    navController.popBackStack()
                },
                onBack = { 
                    tempExcludedIds = emptyList()
                    navController.popBackStack() 
                }
            )
        }

        composable(AtlasRoutes.SETTINGS) {
            SettingsScreen(
                locale = locale,
                weightUnit = weightUnit,
                onLocaleChange = onLocaleChange,
                onWeightUnitChange = onWeightUnitChange,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = AtlasRoutes.SUMMARY,
            arguments = listOf(navArgument("sessionId") { type = NavType.LongType })
        ) { backStackEntry ->
            val sessionId = backStackEntry.arguments?.getLong("sessionId") ?: 0L
            WorkoutSummaryScreen(
                sessionId = sessionId,
                locale = locale,
                weightUnit = weightUnit,
                onFinish = {
                    navController.navigate(AtlasRoutes.DASHBOARD) {
                        popUpTo(AtlasRoutes.DASHBOARD) { inclusive = true }
                    }
                },
                onStartWorkout = {
                    navController.navigate(AtlasRoutes.WORKOUT) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(AtlasRoutes.HISTORY) {
            HistoryScreen(
                locale = locale,
                weightUnit = weightUnit,
                onBack = { navController.popBackStack() },
                onViewSession = { sessionId ->
                    navController.navigate(AtlasRoutes.summary(sessionId))
                }
            )
        }
    }
}
