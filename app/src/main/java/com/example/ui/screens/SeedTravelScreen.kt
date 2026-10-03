package com.example.ui.screens

import androidx.activity.compose.BackHandler
import com.example.ui.components.ScaffoldWithTopBar
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SeedDispersalMode
import com.example.data.model.SeedTravelAdventure
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.PlantPalsViewModel
import kotlinx.coroutines.delay

@Composable
fun SeedTravelScreen(
    viewModel: PlantPalsViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler {
        viewModel.navigateTo(AppScreen.HOME)
    }

    val activeFlight by viewModel.activeSeedFlight.collectAsState()

    ScaffoldWithTopBar(
        title = "Seed Flight Simulator",
        onBack = { viewModel.navigateTo(AppScreen.HOME) },
        onSpeak = {
            viewModel.speak("Welcome to Seed Flight Simulator! Plants can't walk, so their seeds travel by wind parachutes, ocean canoes, animal hitchhikers, and popping explosions! Tap any adventure to launch!")
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            // Header Explanation
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F7FA)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "🚀", fontSize = 32.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "How Do Seeds Travel?",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF006064)
                        )
                        Text(
                            text = "Plants stay rooted, but their baby seeds go on incredible journeys around the world!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF00838F)
                        )
                    }
                }
            }

            // Interactive Flight Simulation Stage if Active
            AnimatedVisibility(visible = activeFlight != null) {
                activeFlight?.let { flight ->
                    SeedFlightStage(
                        flight = flight,
                        onDismiss = { viewModel.dismissSeedFlight() }
                    )
                }
            }

            Text(
                text = "Choose A Seed Vehicle:",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
            )

            // Adventures List
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(viewModel.seedAdventures) { adventure ->
                    SeedAdventureCard(
                        adventure = adventure,
                        onLaunch = { viewModel.launchSeedTravel(adventure) },
                        onSpeak = {
                            viewModel.speak("${adventure.plantName}! Travel method: ${adventure.mode.label}. ${adventure.funStory}")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SeedFlightStage(
    flight: SeedTravelAdventure,
    onDismiss: () -> Unit
) {
    val travelX = remember { Animatable(0f) }
    val rotateAngle = remember { Animatable(0f) }

    LaunchedEffect(flight.id) {
        travelX.snapTo(0f)
        rotateAngle.snapTo(0f)
        // Animate flight across canvas
        travelX.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 2400, easing = LinearEasing)
        )
    }

    val spinTransition = rememberInfiniteTransition(label = "spin")
    val spinning by spinTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "spin_angle"
    )

    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = flight.mode.emoji, fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Flight Active: ${flight.vehicleName}",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E7D32),
                        fontSize = 14.sp
                    )
                }
                IconButton(onClick = onDismiss, modifier = Modifier.size(24.dp)) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Flight animation track
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.horizontalGradient(
                            when (flight.mode) {
                                SeedDispersalMode.WATER -> listOf(Color(0xFF81D4FA), Color(0xFF0288D1))
                                SeedDispersalMode.EXPLOSION -> listOf(Color(0xFFFFCC80), Color(0xFFFF7043))
                                SeedDispersalMode.ANIMAL -> listOf(Color(0xFFD7CCC8), Color(0xFFA1887F))
                                else -> listOf(Color(0xFFE1F5FE), Color(0xFFC8E6C9))
                            }
                        )
                    )
            ) {
                // Moving Seed
                val offsetX = (travelX.value * 260).dp
                Box(
                    modifier = Modifier
                        .offset(x = offsetX, y = 20.dp)
                        .rotate(if (flight.id == "maple") spinning else 0f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = flight.seedEmoji, fontSize = 38.sp)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = flight.funStory,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF1B5E20)
            )
        }
    }
}

@Composable
fun SeedAdventureCard(
    adventure: SeedTravelAdventure,
    onLaunch: () -> Unit,
    onSpeak: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("seed_card_${adventure.id}")
            .clickable(onClick = onLaunch)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFFF9C4)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = adventure.seedEmoji, fontSize = 28.sp)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = adventure.plantName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${adventure.mode.emoji} ${adventure.vehicleName}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
            }

            IconButton(onClick = onSpeak, modifier = Modifier.testTag("btn_speak_seed_${adventure.id}")) {
                Icon(Icons.Default.VolumeUp, contentDescription = "Read aloud", tint = MaterialTheme.colorScheme.primary)
            }

            Button(
                onClick = onLaunch,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                modifier = Modifier.testTag("btn_launch_${adventure.id}")
            ) {
                Text(text = "Launch! 🚀", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
