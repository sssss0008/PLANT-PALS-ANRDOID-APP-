package com.example.ui.screens

import androidx.activity.compose.BackHandler
import com.example.ui.components.ScaffoldWithTopBar
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import com.example.data.model.PlantCategory
import com.example.data.model.PlantSpecies
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.PlantPalsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantEncyclopediaScreen(
    viewModel: PlantPalsViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler {
        viewModel.navigateTo(AppScreen.HOME)
    }

    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val selectedSpecies by viewModel.selectedSpecies.collectAsState()

    val filteredSpecies = viewModel.allSpecies.filter {
        selectedCategory == null || it.category == selectedCategory
    }

    ScaffoldWithTopBar(
        title = "Plant Kingdom Book",
        onBack = { viewModel.navigateTo(AppScreen.HOME) },
        onSpeak = {
            viewModel.speak("Welcome to the Plant Kingdom! Explore amazing flowers, mighty trees, yummy fruits, and wild plants from around the world!")
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Category Filter Chips
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    val isAll = selectedCategory == null
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = if (isAll) MaterialTheme.colorScheme.primary else Color(0xFFF1F8E9),
                        modifier = Modifier
                            .testTag("filter_all")
                            .clickable { viewModel.filterCategory(null) }
                    ) {
                        Text(
                            text = "🌱 All Plants (${viewModel.allSpecies.size})",
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            fontWeight = if (isAll) FontWeight.Bold else FontWeight.Medium,
                            color = if (isAll) Color.White else Color(0xFF2E7D32)
                        )
                    }
                }

                items(PlantCategory.values()) { cat ->
                    val isSelected = selectedCategory == cat
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = if (isSelected) MaterialTheme.colorScheme.primary else Color(0xFFF1F8E9),
                        modifier = Modifier
                            .testTag("filter_${cat.name}")
                            .clickable { viewModel.filterCategory(cat) }
                    ) {
                        Text(
                            text = "${cat.emoji} ${cat.displayName}",
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else Color(0xFF2E7D32)
                        )
                    }
                }
            }

            // Plant Species Cards List
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(top = 8.dp, bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredSpecies) { species ->
                    PlantSpeciesCard(
                        species = species,
                        onCardClick = { viewModel.selectSpecies(species) },
                        onSpeakClick = {
                            viewModel.speak("${species.name}! ${species.simpleDescription} Superpower: ${species.superpower}")
                        }
                    )
                }
            }
        }

        // Detail Bottom Sheet
        selectedSpecies?.let { species ->
            ModalBottomSheet(
                onDismissRequest = { viewModel.selectSpecies(null) },
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                PlantSpeciesDetailSheet(
                    species = species,
                    onClose = { viewModel.selectSpecies(null) },
                    onSpeak = {
                        viewModel.speak("${species.name}! ${species.simpleDescription}. Superpower: ${species.superpower}. Habitat: ${species.habitat}. Fun fact: ${species.funFact}")
                    }
                )
            }
        }
    }
}

@Composable
fun PlantSpeciesCard(
    species: PlantSpecies,
    onCardClick: () -> Unit,
    onSpeakClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("card_plant_${species.id}")
            .clickable(onClick = onCardClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(species.color.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = species.emoji, fontSize = 30.sp)
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = species.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = species.habitat,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = species.simpleDescription,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2
                )
            }

            IconButton(onClick = onSpeakClick, modifier = Modifier.testTag("btn_speak_${species.id}")) {
                Icon(
                    imageVector = Icons.Default.VolumeUp,
                    contentDescription = "Read aloud",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun PlantSpeciesDetailSheet(
    species: PlantSpecies,
    onClose: () -> Unit,
    onSpeak: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .padding(bottom = 24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(species.color.copy(alpha = 0.25f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = species.emoji, fontSize = 34.sp)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = species.name,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = species.category.displayName,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Row {
                IconButton(onClick = onSpeak, modifier = Modifier.testTag("btn_sheet_speak")) {
                    Icon(Icons.Default.VolumeUp, contentDescription = "Read aloud", tint = MaterialTheme.colorScheme.primary)
                }
                IconButton(onClick = onClose, modifier = Modifier.testTag("btn_sheet_close")) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = species.simpleDescription,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Superpower Box
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFFFFF3E0),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(Icons.Default.FlashOn, contentDescription = "Superpower", tint = Color(0xFFF57C00))
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(text = "Plant Superpower!", fontWeight = FontWeight.Bold, color = Color(0xFFF57C00), fontSize = 13.sp)
                    Text(text = species.superpower, style = MaterialTheme.typography.bodyMedium, color = Color(0xFF4E342E))
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Habitat & Growth
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFFE8F5E9),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(Icons.Default.Home, contentDescription = "Habitat", tint = Color(0xFF2E7D32))
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(text = "Home & Growth Habit", fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32), fontSize = 13.sp)
                    Text(text = "Habitat: ${species.habitat}", style = MaterialTheme.typography.bodyMedium, color = Color(0xFF1B5E20))
                    Text(text = species.howItGrows, style = MaterialTheme.typography.bodyMedium, color = Color(0xFF1B5E20))
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Fun Fact Box
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFFEDE7F6),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(Icons.Default.Lightbulb, contentDescription = "Fun Fact", tint = Color(0xFF7B1FA2))
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(text = "Did You Know?", fontWeight = FontWeight.Bold, color = Color(0xFF7B1FA2), fontSize = 13.sp)
                    Text(text = species.funFact, style = MaterialTheme.typography.bodyMedium, color = Color(0xFF4A148C))
                }
            }
        }
    }
}
