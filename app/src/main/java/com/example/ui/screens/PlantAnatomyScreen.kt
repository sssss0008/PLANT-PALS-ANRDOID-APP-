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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Biotech
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import com.example.ui.components.InteractivePlantAnatomyDiagram
import com.example.ui.components.PlantMicroscopeCanvas
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.PlantPalsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantAnatomyScreen(
    viewModel: PlantPalsViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler {
        viewModel.navigateTo(AppScreen.HOME)
    }

    val selectedPartId by viewModel.selectedPartId.collectAsState()
    val exploredParts by viewModel.exploredPartIds.collectAsState()
    val isMicroscopeMode by viewModel.isMicroscopeMode.collectAsState()
    val currentPart = viewModel.plantParts.find { it.id == selectedPartId } ?: viewModel.plantParts.first()

    ScaffoldWithTopBar(
        title = if (isMicroscopeMode) "🔬 Plant Microscope" else "Plant Parts Explorer",
        onBack = { viewModel.navigateTo(AppScreen.HOME) },
        onSpeak = {
            if (isMicroscopeMode) {
                viewModel.speak("Microscope Lens View! Look closely inside the cells of ${currentPart.name}! ${currentPart.microscopeTitle}. ${currentPart.microscopeExplanation}")
            } else {
                viewModel.speak("Plant Parts Explorer! Tap any part of the plant or the buttons below to discover its secret superpowers!")
            }
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Mode Toggle Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Microscope Toggle Button
                Button(
                    onClick = { viewModel.toggleMicroscopeMode() },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isMicroscopeMode) Color(0xFF0077B6) else Color(0xFFE8F5E9),
                        contentColor = if (isMicroscopeMode) Color.White else Color(0xFF2E7D32)
                    ),
                    modifier = Modifier.testTag("btn_toggle_microscope")
                ) {
                    Icon(
                        imageVector = if (isMicroscopeMode) Icons.Default.Visibility else Icons.Default.Biotech,
                        contentDescription = "Toggle view",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isMicroscopeMode) "Whole Plant 🌿" else "Microscope Lens 🔬",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFE8F5E9)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Explored: ${exploredParts.size}/${viewModel.plantParts.size}",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2E7D32),
                            fontSize = 12.sp
                        )
                        if (exploredParts.size >= viewModel.plantParts.size) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Completed badge",
                                tint = Color(0xFF43A047),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            // Visual Stage: Diagram OR Microscope
            Card(
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isMicroscopeMode) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(340.dp)
                            .background(Color(0xFF05160E)),
                        contentAlignment = Alignment.Center
                    ) {
                        PlantMicroscopeCanvas(
                            part = currentPart,
                            modifier = Modifier.fillMaxSize()
                        )

                        // Microscope Header Overlay
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = Color(0xCC0077B6),
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .padding(top = 10.dp)
                        ) {
                            Text(
                                text = "🔬 1000x Zoom: ${currentPart.microscopeTitle}",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                            )
                        }
                    }
                } else {
                    InteractivePlantAnatomyDiagram(
                        selectedPartId = selectedPartId,
                        onPartSelected = { viewModel.selectPlantPart(it) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Part Selection Chips Row
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(viewModel.plantParts) { part ->
                    val isSelected = part.id == selectedPartId
                    val isExplored = exploredParts.contains(part.id)

                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = if (isSelected) MaterialTheme.colorScheme.primary else Color(0xFFF1F8E9),
                        shadowElevation = if (isSelected) 3.dp else 0.dp,
                        modifier = Modifier
                            .testTag("chip_part_${part.id}")
                            .clickable { viewModel.selectPlantPart(part.id) }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                        ) {
                            Text(text = part.emoji, fontSize = 20.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = part.name,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else Color(0xFF2E7D32),
                                fontSize = 14.sp
                            )
                            if (isExplored) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "✓", color = if (isSelected) Color.White else Color(0xFF4CAF50), fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Detail Card for Current Part
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE8F5E9)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = if (isMicroscopeMode) currentPart.microscopeEmoji else currentPart.emoji, fontSize = 26.sp)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = if (isMicroscopeMode) currentPart.microscopeTitle else currentPart.name,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = currentPart.roleTitle,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF5D4037),
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        IconButton(
                            onClick = {
                                if (isMicroscopeMode) {
                                    viewModel.speak("${currentPart.microscopeTitle}! ${currentPart.microscopeExplanation}")
                                } else {
                                    viewModel.speak("${currentPart.name}! ${currentPart.roleTitle}. ${currentPart.kidExplanation}")
                                }
                            },
                            modifier = Modifier.testTag("btn_read_part")
                        ) {
                            Icon(
                                imageVector = Icons.Default.VolumeUp,
                                contentDescription = "Read aloud",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = if (isMicroscopeMode) currentPart.microscopeExplanation else currentPart.kidExplanation,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Fun Fact Box
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFFFFF8E1),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lightbulb,
                                contentDescription = "Fun fact",
                                tint = Color(0xFFF57F17),
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Botanist Secret!",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = Color(0xFFF57F17)
                                )
                                Text(
                                    text = currentPart.funFact,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color(0xFF4E342E)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
