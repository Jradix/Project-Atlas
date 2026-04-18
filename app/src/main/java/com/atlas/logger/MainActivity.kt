package com.atlas.logger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.atlas.logger.ui.navigation.AtlasNavHost
import com.atlas.logger.ui.navigation.AtlasRoutes
import com.atlas.logger.ui.navigation.BottomNavBar
import com.atlas.logger.ui.theme.AtlasTheme
import com.atlas.logger.util.AppLocale
import com.atlas.logger.util.PreferencesManager
import com.atlas.logger.util.WeightUnit
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var preferencesManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val locale by preferencesManager.language.collectAsState(initial = AppLocale.ENGLISH)
            val weightUnit by preferencesManager.weightUnit.collectAsState(initial = WeightUnit.KG)
            val scope = rememberCoroutineScope()

            AtlasTheme {
                val navController = rememberNavController()
                val currentBackStack by navController.currentBackStackEntryAsState()
                val currentRoute = currentBackStack?.destination?.route

                // Determine if bottom nav should be visible
                val showBottomNav = currentRoute in listOf(
                    AtlasRoutes.DASHBOARD,
                    AtlasRoutes.WORKOUT,
                    AtlasRoutes.LIBRARY,
                    AtlasRoutes.LIBRARY_PICKER // Show even when picking to allow easy exit
                )

                Scaffold(
                    containerColor = com.atlas.logger.ui.theme.AtlasBackground,
                    bottomBar = {
                        if (showBottomNav) {
                            BottomNavBar(
                                modifier = Modifier.zIndex(1f),
                                currentRoute = currentRoute,
                                locale = locale,
                                onNavigate = { route ->
                                    navController.navigate(route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        AtlasNavHost(
                            navController = navController,
                            locale = locale,
                            weightUnit = weightUnit,
                            onLocaleChange = { newLocale ->
                                scope.launch { preferencesManager.setLanguage(newLocale) }
                            },
                            onWeightUnitChange = { newUnit ->
                                scope.launch { preferencesManager.setWeightUnit(newUnit) }
                            }
                        )
                    }
                }
            }
        }
    }
}
