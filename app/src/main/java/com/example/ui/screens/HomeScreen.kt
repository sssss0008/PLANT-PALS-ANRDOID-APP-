package com.example.ui.screens

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
import androidx.compose.material.icons.filled.Biotech
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.Yard
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.PlantPalsViewModel

data class AdventureItem(
    val screen: AppScreen,
    val title: String,
    val subtitle: String,
    val emoji: String,
    val icon: ImageVector,
    val gradientColors: List<Color>,
    val testTag: String
)

@Composable
fun HomeScreen(
    viewModel: PlantPalsViewModel,
    modifier: Modifier = Modifier
) {
    val userProgress by viewModel.userProgress.collectAsState()
    val badges by viewModel.badges.collectAsState()
    val grownPlants by viewModel.grownPlants.collectAsState()

    val totalStars = userProgress?.stars ?: 20
    val unlockedCount = badges.count { it.unlockedAt != null }

    val adventures = listOf(
        AdventureItem(
            screen = AppScreen.ANATOMY,
            title = "Plant Parts & Microscope 🔬",
            subtitle = "Tap whole plants & zoom 1000x into cells & stomata!",
            emoji = "🌸",
            icon = Icons.Default.Eco,
            gradientColors = listOf(Color(0xFF81C784), Color(0xFF2E7D32)),
            testTag = "btn_anatomy"
        ),
        AdventureItem(
            screen = AppScreen.GROWTH_LAB,
            title = "Plant Growth Lab & Doctor 🩺",
            subtitle = "Change weather, heal thirsty plants & watch seeds bloom!",
            emoji = "🌱",
            icon = Icons.Default.WbSunny,
            gradientColors = listOf(Color(0xFFFFB74D), Color(0xFFF57C00)),
            testTag = "btn_growth_lab"
        ),
        AdventureItem(
            screen = AppScreen.SEED_TRAVEL,
            title = "Seed Flight Simulator 🚀",
            subtitle = "Launch seeds across wind parachutes, rivers & explosions!",
            emoji = "🌾",
            icon = Icons.Default.FlightTakeoff,
            gradientColors = listOf(Color(0xFF26C6DA), Color(0xFF00838F)),
            testTag = "btn_seed_travel"
        ),
        AdventureItem(
            screen = AppScreen.PHOTOSYNTHESIS,
            title = "Photosynthesis Kitchen",
            subtitle = "Mix sunshine, water & air to make plant sugar & pop oxygen!",
            emoji = "☀️",
            icon = Icons.Default.Psychology,
            gradientColors = listOf(Color(0xFF4FC3F7), Color(0xFF0288D1)),
            testTag = "btn_photosynthesis"
        ),
        AdventureItem(
            screen = AppScreen.ENCYCLOPEDIA,
            title = "Plant Kingdom Book",
            subtitle = "Explore 12 wild trees, flowers & strange carnivorous plants!",
            emoji = "📚",
            icon = Icons.Default.MenuBook,
            gradientColors = listOf(Color(0xFFBA68C8), Color(0xFF7B1FA2)),
            testTag = "btn_encyclopedia"
        ),
        AdventureItem(
            screen = AppScreen.QUIZ,
            title = "Plant Detective Quiz",
            subtitle = "Test your plant knowledge and win star rewards!",
            emoji = "⭐",
            icon = Icons.Default.Star,
            gradientColors = listOf(Color(0xFFFF8A65), Color(0xFFD84315)),
            testTag = "btn_quiz"
        ),
        AdventureItem(
            screen = AppScreen.GARDEN,
            title = "My Botanical Garden",
            subtitle = "Water your grown plants and admire your badges!",
            emoji = "🏡",
            icon = Icons.Default.Yard,
            gradientColors = listOf(Color(0xFF4DB6AC), Color(0xFF00695C)),
            testTag = "btn_garden"
        )
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Welcome Header & Stars
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "🌿 Plant Pals Pro", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.primary)
                        Text(
                            text = "Junior Botanist Academy & Science Lab",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFFFFF9C4),
                        shadowElevation = 2.dp
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Stars",
                                tint = Color(0xFFFFA000),
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "$totalStars",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Color(0xFF5D4037)
                            )
                        }
                    }
                }
            }
        }

        // Daily Plant Wonder Fact Card
        item {
            val dailyFact = "Did you know? Under a microscope, tiny green chloroplasts inside leaves dance in circles to catch the best sunbeams!"
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "💡", fontSize = 28.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Microscopic Wonder Fact",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2E7D32)
                        )
                        Text(
                            text = dailyFact,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF1B5E20)
                        )
                    }
                    IconButton(
                        onClick = { viewModel.speak(dailyFact) },
                        modifier = Modifier.testTag("btn_speak_fact")
                    ) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = "Read fact aloud",
                            tint = Color(0xFF2E7D32)
                        )
                    }
                }
            }
        }

        // Quick Stats row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    title = "Grown Plants",
                    value = "${grownPlants.size}",
                    emoji = "🌻",
                    color = Color(0xFFFFF3E0),
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Badges Won",
                    value = "$unlockedCount / ${badges.size}",
                    emoji = "🏆",
                    color = Color(0xFFF3E5F5),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Title for Adventures
        item {
            Text(
                text = "🌱 Choose Your Science Adventure:",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        // Adventure Cards
        items(adventures) { item ->
            AdventureCard(
                item = item,
                onClick = { viewModel.navigateTo(item.screen) }
            )
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    emoji: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = emoji, fontSize = 28.sp)
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(text = value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(text = title, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
        }
    }
}

@Composable
fun AdventureCard(
    item: AdventureItem,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(22.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag(item.testTag)
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.horizontalGradient(item.gradientColors))
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.25f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = item.emoji, fontSize = 32.sp)
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = item.subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }

                Text(
                    text = "➔",
                    fontSize = 22.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}
