package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.Yard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.ConfettiExplosion
import com.example.ui.screens.BotanicalGardenScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.PhotosynthesisLabScreen
import com.example.ui.screens.PlantAnatomyScreen
import com.example.ui.screens.PlantEncyclopediaScreen
import com.example.ui.screens.PlantGrowthLabScreen
import com.example.ui.screens.PlantQuizScreen
import com.example.ui.screens.SeedTravelScreen
import com.example.ui.theme.PlantPalsTheme
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.PlantPalsViewModel

data class NavItem(
    val screen: AppScreen,
    val label: String,
    val icon: ImageVector,
    val testTag: String
)

class MainActivity : ComponentActivity() {

    private val viewModel: PlantPalsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            PlantPalsTheme {
                val currentScreen by viewModel.currentScreen.collectAsState()
                val showConfetti by viewModel.showConfetti.collectAsState()

                val navItems = listOf(
                    NavItem(AppScreen.HOME, "Home", Icons.Default.Home, "nav_home"),
                    NavItem(AppScreen.ANATOMY, "Anatomy", Icons.Default.Eco, "nav_anatomy"),
                    NavItem(AppScreen.GROWTH_LAB, "Grow", Icons.Default.WbSunny, "nav_grow"),
                    NavItem(AppScreen.SEED_TRAVEL, "Seeds", Icons.Default.FlightTakeoff, "nav_seeds"),
                    NavItem(AppScreen.GARDEN, "Garden", Icons.Default.Yard, "nav_garden")
                )

                Box(modifier = Modifier.fillMaxSize()) {
                    Scaffold(
                        bottomBar = {
                            NavigationBar(
                                containerColor = MaterialTheme.colorScheme.surface,
                                tonalElevation = 8.dp
                            ) {
                                navItems.forEach { item ->
                                    val isSelected = currentScreen == item.screen
                                    NavigationBarItem(
                                        selected = isSelected,
                                        onClick = { viewModel.navigateTo(item.screen) },
                                        icon = {
                                            Icon(
                                                imageVector = item.icon,
                                                contentDescription = item.label,
                                                modifier = Modifier.testTag(item.testTag)
                                            )
                                        },
                                        label = {
                                            Text(
                                                text = item.label,
                                                fontSize = 11.sp,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                            )
                                        },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = MaterialTheme.colorScheme.primary,
                                            selectedTextColor = MaterialTheme.colorScheme.primary,
                                            indicatorColor = Color(0xFFC8E6C9),
                                            unselectedIconColor = Color.Gray,
                                            unselectedTextColor = Color.Gray
                                        )
                                    )
                                }
                            }
                        }
                    ) { innerPadding ->
                        AnimatedContent(
                            targetState = currentScreen,
                            transitionSpec = { fadeIn() togetherWith fadeOut() },
                            label = "screen_transition",
                            modifier = Modifier.fillMaxSize()
                        ) { screen ->
                            when (screen) {
                                AppScreen.HOME -> HomeScreen(viewModel = viewModel, modifier = Modifier.padding(innerPadding))
                                AppScreen.ANATOMY -> PlantAnatomyScreen(viewModel = viewModel, modifier = Modifier.padding(innerPadding))
                                AppScreen.GROWTH_LAB -> PlantGrowthLabScreen(viewModel = viewModel, modifier = Modifier.padding(innerPadding))
                                AppScreen.PHOTOSYNTHESIS -> PhotosynthesisLabScreen(viewModel = viewModel, modifier = Modifier.padding(innerPadding))
                                AppScreen.SEED_TRAVEL -> SeedTravelScreen(viewModel = viewModel, modifier = Modifier.padding(innerPadding))
                                AppScreen.ENCYCLOPEDIA -> PlantEncyclopediaScreen(viewModel = viewModel, modifier = Modifier.padding(innerPadding))
                                AppScreen.QUIZ -> PlantQuizScreen(viewModel = viewModel, modifier = Modifier.padding(innerPadding))
                                AppScreen.GARDEN -> BotanicalGardenScreen(viewModel = viewModel, modifier = Modifier.padding(innerPadding))
                            }
                        }
                    }

                    // Confetti Particle Explosion Overlay
                    ConfettiExplosion(
                        isActive = showConfetti,
                        onFinished = { viewModel.dismissConfetti() }
                    )
                }
            }
        }
    }
}
