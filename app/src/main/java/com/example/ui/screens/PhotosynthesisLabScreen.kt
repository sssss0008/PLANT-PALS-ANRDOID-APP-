package com.example.ui.screens

import androidx.activity.compose.BackHandler
import com.example.ui.components.ScaffoldWithTopBar
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.PlantPalsViewModel

@Composable
fun PhotosynthesisLabScreen(
    viewModel: PlantPalsViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler {
        viewModel.navigateTo(AppScreen.HOME)
    }

    val hasSunlight by viewModel.hasSunlight.collectAsState()
    val hasWater by viewModel.hasWater.collectAsState()
    val hasAir by viewModel.hasAir.collectAsState()
    val isCooking by viewModel.isCooking.collectAsState()
    val isDone by viewModel.isKitchenDone.collectAsState()
    val poppedBubbles by viewModel.poppedBubbles.collectAsState()

    val infiniteTransition = rememberInfiniteTransition(label = "float_bubble")
    val bubbleFloat1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -18f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "b1"
    )
    val bubbleFloat2 by infiniteTransition.animateFloat(
        initialValue = -12f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "b2"
    )

    ScaffoldWithTopBar(
        title = "Photosynthesis Kitchen",
        onBack = { viewModel.navigateTo(AppScreen.HOME) },
        onSpeak = {
            viewModel.speak("Welcome to the Plant Kitchen! Plants make their own food through photosynthesis. Add sunlight, water, and air into the leaf blender to cook sweet sugar and clean oxygen!")
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Science Story Intro
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "👨‍🍳", fontSize = 32.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Meet Chef Chlorophyll!",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2E7D32)
                        )
                        Text(
                            text = "Leaves are nature's solar kitchens! Can you add the 3 secret ingredients to cook sweet plant food?",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF1B5E20)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Magic Recipe Board
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "The Secret Plant Recipe",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RecipeBadge(emoji = "☀️", label = "Sunlight", isReady = hasSunlight)
                        Text("+", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.Gray)
                        RecipeBadge(emoji = "💧", label = "Water", isReady = hasWater)
                        Text("+", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.Gray)
                        RecipeBadge(emoji = "🌬️", label = "Air (CO2)", isReady = hasAir)
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text("⬇ Makes ⬇", fontSize = 13.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = if (isDone) Color(0xFFFFF9C4) else Color(0xFFF5F5F5)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "🍬", fontSize = 18.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Glucose (Sugar)",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = if (isDone) Color(0xFFF57F17) else Color.Gray
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = if (isDone) Color(0xFFE1F5FE) else Color(0xFFF5F5F5)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "🫧", fontSize = 18.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Fresh Oxygen",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = if (isDone) Color(0xFF0288D1) else Color.Gray
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Ingredient Toggles (Big Kid Touch Targets)
            Text(
                text = "Tap Each Ingredient To Add:",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IngredientCard(
                    title = "Sunlight",
                    emoji = "☀️",
                    description = "Solar warmth",
                    isActive = hasSunlight,
                    activeColor = Color(0xFFFFA000),
                    onClick = { viewModel.toggleSunlight() },
                    modifier = Modifier.weight(1f),
                    testTag = "btn_add_sunlight"
                )

                IngredientCard(
                    title = "Water",
                    emoji = "💧",
                    description = "From roots",
                    isActive = hasWater,
                    activeColor = Color(0xFF29B6F6),
                    onClick = { viewModel.toggleWater() },
                    modifier = Modifier.weight(1f),
                    testTag = "btn_add_water"
                )

                IngredientCard(
                    title = "Air",
                    emoji = "🌬️",
                    description = "CO2 breeze",
                    isActive = hasAir,
                    activeColor = Color(0xFF81C784),
                    onClick = { viewModel.toggleAir() },
                    modifier = Modifier.weight(1f),
                    testTag = "btn_add_air"
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Cook Button / Loading / Celebration
            if (isCooking) {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Chef Chlorophyll is mixing the sunbeams...",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            } else if (isDone) {
                // Done Celebration Card & Oxygen Bubble Pop Mini-Game!
                Card(
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE1F5FE)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "🎉", fontSize = 28.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Delicious Photosynthesis!",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF01579B)
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "The leaf baked yummy sugar for itself, and released fresh Oxygen bubbles into the sky! Tap the bubbles to pop them!",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF0277BD)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Floating Oxygen Bubbles to Tap!
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OxygenBubble(
                                label = "O₂",
                                floatOffset = bubbleFloat1,
                                onPop = {
                                    viewModel.popOxygenBubble()
                                    viewModel.speak("Pop! Fresh oxygen for you to breathe!")
                                },
                                testTag = "bubble_1"
                            )
                            OxygenBubble(
                                label = "🫧",
                                floatOffset = bubbleFloat2,
                                onPop = {
                                    viewModel.popOxygenBubble()
                                    viewModel.speak("Pop! +1 Star!")
                                },
                                testTag = "bubble_2"
                            )
                            OxygenBubble(
                                label = "O₂",
                                floatOffset = bubbleFloat1 * 0.7f,
                                onPop = {
                                    viewModel.popOxygenBubble()
                                    viewModel.speak("Pop! Clean mountain air!")
                                },
                                testTag = "bubble_3"
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Bubbles Popped: $poppedBubbles (+ $poppedBubbles Stars ⭐)",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF01579B),
                            fontSize = 14.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = { viewModel.resetPhotosynthesisKitchen() },
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0288D1)),
                            modifier = Modifier.testTag("btn_cook_again")
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = "Cook Again")
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Cook Another Batch! 👨‍🍳")
                        }
                    }
                }
            } else {
                Button(
                    onClick = { viewModel.cookPhotosynthesis() },
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    enabled = hasSunlight && hasWater && hasAir,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("btn_start_cooking")
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = "Cook")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (hasSunlight && hasWater && hasAir) "Cook Plant Food Magic! ✨" else "Add All 3 Ingredients First!",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun RecipeBadge(emoji: String, label: String, isReady: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(if (isReady) Color(0xFFC8E6C9) else Color(0xFFEEEEEE)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = emoji, fontSize = 24.sp)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (isReady) FontWeight.Bold else FontWeight.Normal,
            color = if (isReady) Color(0xFF2E7D32) else Color.Gray
        )
    }
}

@Composable
fun IngredientCard(
    title: String,
    emoji: String,
    description: String,
    isActive: Boolean,
    activeColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String
) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = if (isActive) activeColor.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surface,
        shadowElevation = if (isActive) 3.dp else 1.dp,
        modifier = modifier
            .testTag(testTag)
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = emoji, fontSize = 32.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(text = description, fontSize = 11.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(6.dp))

            Surface(
                shape = RoundedCornerShape(10.dp),
                color = if (isActive) activeColor else Color(0xFFE0E0E0)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isActive) {
                        Icon(Icons.Default.Check, contentDescription = "Active", tint = Color.White, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(3.dp))
                    }
                    Text(
                        text = if (isActive) "Added!" else "Tap to add",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isActive) Color.White else Color.DarkGray
                    )
                }
            }
        }
    }
}

@Composable
fun OxygenBubble(
    label: String,
    floatOffset: Float,
    onPop: () -> Unit,
    testTag: String
) {
    Box(
        modifier = Modifier
            .offset(y = floatOffset.dp)
            .size(60.dp)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = listOf(Color(0xFFE1F5FE), Color(0xFF81D4FA))
                )
            )
            .testTag(testTag)
            .clickable(onClick = onPop),
        contentAlignment = Alignment.Center
    ) {
        Text(text = label, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF0277BD))
    }
}
