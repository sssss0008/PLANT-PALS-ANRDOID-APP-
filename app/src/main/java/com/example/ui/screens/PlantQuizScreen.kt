package com.example.ui.screens

import androidx.activity.compose.BackHandler
import com.example.ui.components.ScaffoldWithTopBar
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Yard
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.PlantPalsViewModel

@Composable
fun PlantQuizScreen(
    viewModel: PlantPalsViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler {
        viewModel.navigateTo(AppScreen.HOME)
    }

    val quizIndex by viewModel.quizIndex.collectAsState()
    val selectedOption by viewModel.selectedQuizOption.collectAsState()
    val isAnswerChecked by viewModel.isQuizAnswerChecked.collectAsState()
    val starsEarned by viewModel.quizStarsEarned.collectAsState()
    val isFinished by viewModel.quizFinished.collectAsState()

    val questions = viewModel.quizQuestions
    val currentQuestion = if (quizIndex < questions.size) questions[quizIndex] else questions.first()

    ScaffoldWithTopBar(
        title = "Plant Detective Quiz",
        onBack = { viewModel.navigateTo(AppScreen.HOME) },
        onSpeak = {
            if (!isFinished) {
                viewModel.speak("${currentQuestion.question}. Option 1: ${currentQuestion.options[0]}. Option 2: ${currentQuestion.options[1]}. Option 3: ${currentQuestion.options[2]}.")
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
            if (isFinished) {
                // Quiz Complete Screen
                Card(
                    shape = RoundedCornerShape(26.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "🏆", fontSize = 64.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Super Plant Detective!",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "You answered all quiz questions and learned so much about plants!",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Star badge summary
                        Surface(
                            shape = RoundedCornerShape(18.dp),
                            color = Color(0xFFFFF9C4),
                            shadowElevation = 2.dp
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Star, contentDescription = "Stars", tint = Color(0xFFFFA000), modifier = Modifier.size(32.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "+$starsEarned Stars Earned!",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    color = Color(0xFF5D4037)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = { viewModel.restartQuiz() },
                            shape = RoundedCornerShape(18.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("btn_play_quiz_again")
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = "Restart")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Play Again! 🌟", fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = { viewModel.navigateTo(AppScreen.GARDEN) },
                            shape = RoundedCornerShape(18.dp),
                            colors = ButtonDefaults.outlinedButtonColors(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("btn_quiz_to_garden")
                        ) {
                            Icon(Icons.Default.Yard, contentDescription = "Garden")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Visit Botanical Garden 🏡", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            } else {
                // Quiz Question View
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Question ${quizIndex + 1} of ${questions.size}",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 15.sp
                    )

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFFFF9C4)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Star, contentDescription = "Stars", tint = Color(0xFFFFA000), modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "$starsEarned", fontWeight = FontWeight.Bold, color = Color(0xFF5D4037))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                LinearProgressIndicator(
                    progress = { (quizIndex + 1).toFloat() / questions.size },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = Color(0xFFE8F5E9)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Question Card
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
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = currentQuestion.question,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.weight(1f),
                                lineHeight = 24.sp
                            )
                            IconButton(
                                onClick = { viewModel.speak(currentQuestion.question) },
                                modifier = Modifier.testTag("btn_speak_question")
                            ) {
                                Icon(Icons.Default.VolumeUp, contentDescription = "Read aloud", tint = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Options
                currentQuestion.options.forEachIndexed { index, optionText ->
                    val isSelected = selectedOption == index
                    val isCorrect = index == currentQuestion.correctIndex
                    val showCheck = isAnswerChecked

                    val containerColor = when {
                        showCheck && isCorrect -> Color(0xFFE8F5E9)
                        showCheck && isSelected && !isCorrect -> Color(0xFFFFEBEE)
                        isSelected -> Color(0xFFE8F5E9)
                        else -> MaterialTheme.colorScheme.surface
                    }

                    val borderColor = when {
                        showCheck && isCorrect -> Color(0xFF43A047)
                        showCheck && isSelected && !isCorrect -> Color(0xFFE53935)
                        else -> Color.Transparent
                    }

                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = containerColor),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp)
                            .border(2.dp, borderColor, RoundedCornerShape(20.dp))
                            .testTag("quiz_opt_$index")
                            .clickable(enabled = !isAnswerChecked) {
                                viewModel.selectQuizOption(index)
                            }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = currentQuestion.optionEmojis[index], fontSize = 28.sp)
                            Spacer(modifier = Modifier.width(14.dp))
                            Text(
                                text = optionText,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.weight(1f)
                            )
                            if (showCheck) {
                                if (isCorrect) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = "Correct", tint = Color(0xFF43A047), modifier = Modifier.size(24.dp))
                                } else if (isSelected) {
                                    Text(text = "❌", fontSize = 18.sp)
                                }
                            }
                        }
                    }
                }

                // Feedback card after selection
                AnimatedVisibility(visible = isAnswerChecked) {
                    val correct = selectedOption == currentQuestion.correctIndex
                    Column(modifier = Modifier.padding(top = 14.dp)) {
                        Card(
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (correct) Color(0xFFE8F5E9) else Color(0xFFFFF8E1)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = if (correct) "🌟" else "💡", fontSize = 28.sp)
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = if (correct) "Hooray! Correct!" else "Helpful Botanist Tip!",
                                        fontWeight = FontWeight.Bold,
                                        color = if (correct) Color(0xFF2E7D32) else Color(0xFFF57F17),
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = if (correct) currentQuestion.praiseMessage else currentQuestion.hint,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = if (correct) Color(0xFF1B5E20) else Color(0xFF4E342E)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = { viewModel.nextQuizQuestion() },
                            shape = RoundedCornerShape(18.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("btn_next_question")
                        ) {
                            Text(
                                text = if (quizIndex < questions.size - 1) "Next Question ➔" else "See My Results! 🏆",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
