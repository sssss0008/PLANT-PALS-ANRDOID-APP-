package com.example.ui.screens

import androidx.activity.compose.BackHandler
import com.example.ui.components.ScaffoldWithTopBar
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.Healing
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.GrowthStage
import com.example.data.model.PlantHealthStatus
import com.example.data.model.PotStyle
import com.example.data.model.WeatherCondition
import com.example.ui.components.GrowthStageCanvas
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.PlantPalsViewModel

data class GrowthPlantOption(val id: String, val name: String, val emoji: String)

@Composable
fun PlantGrowthLabScreen(
    viewModel: PlantPalsViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler {
        viewModel.navigateTo(AppScreen.HOME)
    }

    val plantType by viewModel.selectedPlantType.collectAsState()
    val growthStage by viewModel.growthStage.collectAsState()
    val soilMoisture by viewModel.soilMoisture.collectAsState()
    val sunshineLevel by viewModel.sunshineLevel.collectAsState()
    val labMessage by viewModel.labMessage.collectAsState()
    val isHarvested by viewModel.isHarvested.collectAsState()
    val currentWeather by viewModel.currentWeather.collectAsState()
    val currentHealth by viewModel.currentHealth.collectAsState()
    val potStyle by viewModel.selectedPotStyle.collectAsState()

    val plantOptions = listOf(
        GrowthPlantOption("SUNFLOWER", "Sunflower", "🌻"),
        GrowthPlantOption("STRAWBERRY", "Strawberry", "🍓"),
        GrowthPlantOption("CACTUS", "Cactus", "🌵"),
        GrowthPlantOption("ROSE", "Rose", "🌹")
    )

    ScaffoldWithTopBar(
        title = "Plant Growth Lab",
        onBack = { viewModel.navigateTo(AppScreen.HOME) },
        onSpeak = {
            viewModel.speak("Welcome to the Growth Lab! Choose a seed, water the dirt, control weather conditions, check on plant health with the Doctor tools, and watch your plant bloom!")
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Plant Selector Row
            Text(
                text = "Pick A Plant To Grow:",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(6.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(plantOptions) { opt ->
                    val isSelected = opt.id == plantType
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                        shadowElevation = if (isSelected) 3.dp else 1.dp,
                        modifier = Modifier
                            .testTag("opt_plant_${opt.id}")
                            .clickable { viewModel.selectPlantTypeToGrow(opt.id) }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(text = opt.emoji, fontSize = 22.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = opt.name,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Weather Station Selector
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🌤️ Weather Station:",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(WeatherCondition.values()) { weather ->
                    val isSelected = weather == currentWeather
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (isSelected) Color(0xFF0288D1) else MaterialTheme.colorScheme.surface,
                        shadowElevation = if (isSelected) 2.dp else 0.dp,
                        modifier = Modifier
                            .testTag("weather_${weather.name}")
                            .clickable { viewModel.setWeather(weather) }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = weather.emoji, fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = weather.label,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Main Growth Canvas Card
            Card(
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                        .background(Color(0xFFF1F8E9)),
                    contentAlignment = Alignment.Center
                ) {
                    GrowthStageCanvas(
                        stage = growthStage,
                        plantType = plantType,
                        weather = currentWeather,
                        health = currentHealth,
                        potStyle = potStyle,
                        onTapBug = { viewModel.curePlant() },
                        modifier = Modifier.fillMaxSize()
                    )

                    // Top Badge Overlay
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = Color.White.copy(alpha = 0.9f),
                        shadowElevation = 2.dp,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 10.dp)
                    ) {
                        Text(
                            text = "Stage ${growthStage.stageNumber}: ${growthStage.title} • ${currentHealth.emoji}",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 5.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Plant Doctor Bar if Sick or Button to Checkup
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (currentHealth != PlantHealthStatus.HEALTHY) Color(0xFFFFEBEE) else Color(0xFFF1F8E9)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.MedicalServices,
                        contentDescription = "Doctor",
                        tint = if (currentHealth != PlantHealthStatus.HEALTHY) Color(0xFFE53935) else Color(0xFF2E7D32)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Plant Health: ${currentHealth.label}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = if (currentHealth != PlantHealthStatus.HEALTHY) Color(0xFFC62828) else Color(0xFF2E7D32)
                        )
                        Text(
                            text = currentHealth.prescription,
                            fontSize = 11.sp,
                            color = Color(0xFF424242)
                        )
                    }
                    if (currentHealth != PlantHealthStatus.HEALTHY) {
                        Button(
                            onClick = { viewModel.curePlant() },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF43A047)),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("btn_cure_plant")
                        ) {
                            Icon(Icons.Default.Healing, contentDescription = "Cure", modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "Heal! 🩺", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Button(
                            onClick = { viewModel.triggerRandomCheckup() },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            modifier = Modifier.testTag("btn_health_checkup")
                        ) {
                            Text(text = "Checkup 🩺", fontSize = 11.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Pot Style Customization Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "🎨 Decorate Pot:", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    PotStyle.values().forEach { style ->
                        val isSelected = style == potStyle
                        Surface(
                            shape = CircleShape,
                            color = style.potColor,
                            shadowElevation = if (isSelected) 3.dp else 1.dp,
                            modifier = Modifier
                                .size(32.dp)
                                .testTag("pot_${style.id}")
                                .clickable { viewModel.selectPotStyle(style) }
                        ) {
                            if (isSelected) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(text = "✓", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Plant Care Meters
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.WaterDrop, contentDescription = "Water", tint = Color(0xFF29B6F6))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Soil Moisture", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                        Text(text = "${soilMoisture.toInt()}%", fontWeight = FontWeight.Bold, color = Color(0xFF0288D1))
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { soilMoisture / 100f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = Color(0xFF29B6F6),
                        trackColor = Color(0xFFE1F5FE)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.WbSunny, contentDescription = "Sun", tint = Color(0xFFFFA000))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Sunshine Level", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                        Text(text = "${sunshineLevel.toInt()}%", fontWeight = FontWeight.Bold, color = Color(0xFFF57C00))
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { sunshineLevel / 100f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = Color(0xFFFFA000),
                        trackColor = Color(0xFFFFF8E1)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Interactive Care Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = { viewModel.waterPlantInLab() },
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF29B6F6)),
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .testTag("btn_water_lab")
                ) {
                    Icon(Icons.Default.WaterDrop, contentDescription = "Water plant")
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Water Soil 💧", fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = { viewModel.addSunshineInLab() },
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA000)),
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .testTag("btn_sun_lab")
                ) {
                    Icon(Icons.Default.WbSunny, contentDescription = "Sunshine")
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Add Sun ☀️", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (growthStage == GrowthStage.HARVEST) {
                Button(
                    onClick = { viewModel.harvestCurrentPlant() },
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF43A047)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("btn_harvest_plant")
                ) {
                    Icon(Icons.Default.Celebration, contentDescription = "Harvest")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isHarvested) "Plant Harvested! (Check Garden)" else "Harvest To Botanical Garden! 🏆",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            } else {
                Button(
                    onClick = { viewModel.advanceGrowthStage() },
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("btn_grow_next_stage")
                ) {
                    Text(text = "Help Plant Grow Next! 🌱", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { viewModel.resetGrowthLab() },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.outlinedButtonColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("btn_reset_lab")
            ) {
                Icon(Icons.Default.Refresh, contentDescription = "Restart Seed")
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "Plant A New Seed 🌱")
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
