package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.data.model.GrowthStage
import com.example.data.model.PlantHealthStatus
import com.example.data.model.PlantPart
import com.example.data.model.PotStyle
import com.example.data.model.WeatherCondition
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun InteractivePlantAnatomyDiagram(
    selectedPartId: String?,
    onPartSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(1100, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(380.dp)
            .pointerInput(Unit) {
                detectTapGestures { tapOffset ->
                    val w = size.width.toFloat()
                    val h = size.height.toFloat()

                    val relX = tapOffset.x / w
                    val relY = tapOffset.y / h

                    when {
                        relY < 0.28f && relX in 0.35f..0.65f -> onPartSelected("flower")
                        (relX in 0.10f..0.45f && relY in 0.26f..0.52f) || (relX in 0.55f..0.90f && relY in 0.28f..0.55f) -> onPartSelected("leaf")
                        relX in 0.62f..0.92f && relY in 0.44f..0.66f -> onPartSelected("fruit")
                        relX in 0.42f..0.58f && relY in 0.28f..0.68f -> onPartSelected("stem")
                        relX in 0.65f..0.95f && relY in 0.70f..0.95f -> onPartSelected("seed")
                        relY >= 0.68f -> onPartSelected("root")
                        else -> {
                            if (relY < 0.4f) onPartSelected("flower") else onPartSelected("stem")
                        }
                    }
                }
            }
    ) {
        val w = size.width
        val h = size.height
        val groundY = h * 0.68f

        // 1. Sky & Sun
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFFE1F5FE), Color(0xFFF1F8E9)),
                startY = 0f,
                endY = groundY
            ),
            topLeft = Offset.Zero,
            size = Size(w, groundY)
        )

        // Friendly Sun in top left corner
        drawCircle(
            color = Color(0xFFFFF9C4),
            radius = 42.dp.toPx(),
            center = Offset(w * 0.12f, h * 0.12f)
        )
        drawCircle(
            color = Color(0xFFFFD54F),
            radius = 26.dp.toPx(),
            center = Offset(w * 0.12f, h * 0.12f)
        )

        // 2. Underground Soil
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFF795548), Color(0xFF4E342E)),
                startY = groundY,
                endY = h
            ),
            topLeft = Offset(0f, groundY),
            size = Size(w, h - groundY)
        )

        // Lush grass line at soil boundary
        drawRect(
            color = Color(0xFF43A047),
            topLeft = Offset(0f, groundY - 6.dp.toPx()),
            size = Size(w, 8.dp.toPx())
        )

        // Soil pebbles
        drawCircle(Color(0xFF8D6E63), 6.dp.toPx(), Offset(w * 0.18f, groundY + 45.dp.toPx()))
        drawCircle(Color(0xFF8D6E63), 8.dp.toPx(), Offset(w * 0.78f, groundY + 65.dp.toPx()))
        drawCircle(Color(0xFF5D4037), 5.dp.toPx(), Offset(w * 0.45f, groundY + 80.dp.toPx()))

        // 3. Roots
        val isRootSelected = selectedPartId == "root"
        val rootColor = if (isRootSelected) Color(0xFFFFD54F) else Color(0xFFFFE082)
        val rootWidth = if (isRootSelected) 8.dp.toPx() else 5.dp.toPx()

        val tapRootPath = Path().apply {
            moveTo(w * 0.5f, groundY)
            quadraticTo(w * 0.48f, groundY + (h - groundY) * 0.5f, w * 0.5f, h * 0.95f)
        }
        drawPath(tapRootPath, rootColor, style = Stroke(width = rootWidth, cap = StrokeCap.Round))

        drawRootBranch(w * 0.5f, groundY + 15.dp.toPx(), w * 0.32f, groundY + 45.dp.toPx(), rootColor, rootWidth * 0.7f)
        drawRootBranch(w * 0.32f, groundY + 45.dp.toPx(), w * 0.22f, groundY + 70.dp.toPx(), rootColor, rootWidth * 0.5f)
        drawRootBranch(w * 0.5f, groundY + 30.dp.toPx(), w * 0.68f, groundY + 55.dp.toPx(), rootColor, rootWidth * 0.7f)
        drawRootBranch(w * 0.68f, groundY + 55.dp.toPx(), w * 0.75f, groundY + 75.dp.toPx(), rootColor, rootWidth * 0.5f)
        drawRootBranch(w * 0.5f, groundY + 60.dp.toPx(), w * 0.38f, groundY + 85.dp.toPx(), rootColor, rootWidth * 0.6f)

        // 4. Baby Seed
        val isSeedSelected = selectedPartId == "seed"
        val seedColor = if (isSeedSelected) Color(0xFFFFD54F) else Color(0xFFD7CCC8)
        val seedCenter = Offset(w * 0.82f, groundY + 38.dp.toPx())
        drawOval(
            color = Color(0xFF5D4037),
            topLeft = Offset(seedCenter.x - 14.dp.toPx(), seedCenter.y - 10.dp.toPx()),
            size = Size(28.dp.toPx(), 20.dp.toPx())
        )
        drawOval(
            color = seedColor,
            topLeft = Offset(seedCenter.x - 12.dp.toPx(), seedCenter.y - 8.dp.toPx()),
            size = Size(24.dp.toPx(), 16.dp.toPx())
        )
        drawPath(
            Path().apply {
                moveTo(seedCenter.x, seedCenter.y - 6.dp.toPx())
                quadraticTo(seedCenter.x + 8.dp.toPx(), seedCenter.y - 18.dp.toPx(), seedCenter.x + 12.dp.toPx(), seedCenter.y - 14.dp.toPx())
            },
            color = Color(0xFF81C784),
            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
        )

        // 5. Stem
        val isStemSelected = selectedPartId == "stem"
        val stemColor = if (isStemSelected) Color(0xFF2E7D32) else Color(0xFF43A047)
        val stemStroke = if (isStemSelected) 16.dp.toPx() else 12.dp.toPx()

        val stemPath = Path().apply {
            moveTo(w * 0.5f, groundY)
            quadraticTo(w * 0.49f, h * 0.42f, w * 0.5f, h * 0.22f)
        }
        drawPath(stemPath, color = stemColor, style = Stroke(width = stemStroke, cap = StrokeCap.Round))
        drawPath(stemPath, color = Color(0xFFA5D6A7), style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round))

        // 6. Leaves
        val isLeafSelected = selectedPartId == "leaf"
        val leafColor = if (isLeafSelected) Color(0xFF66BB6A) else Color(0xFF4CAF50)

        val leftLeafPath = Path().apply {
            moveTo(w * 0.49f, h * 0.46f)
            cubicTo(w * 0.35f, h * 0.38f, w * 0.15f, h * 0.42f, w * 0.18f, h * 0.48f)
            cubicTo(w * 0.22f, h * 0.56f, w * 0.42f, h * 0.53f, w * 0.49f, h * 0.48f)
            close()
        }
        drawPath(leftLeafPath, color = leafColor)
        drawPath(leftLeafPath, color = Color(0xFF2E7D32), style = Stroke(width = 2.dp.toPx()))

        val rightLeafPath = Path().apply {
            moveTo(w * 0.5f, h * 0.36f)
            cubicTo(w * 0.65f, h * 0.28f, w * 0.85f, h * 0.32f, w * 0.82f, h * 0.39f)
            cubicTo(w * 0.78f, h * 0.46f, w * 0.58f, h * 0.43f, w * 0.5f, h * 0.38f)
            close()
        }
        drawPath(rightLeafPath, color = leafColor)
        drawPath(rightLeafPath, color = Color(0xFF2E7D32), style = Stroke(width = 2.dp.toPx()))

        // 7. Fruit
        val isFruitSelected = selectedPartId == "fruit"
        val fruitColor = if (isFruitSelected) Color(0xFFFF5252) else Color(0xFFD32F2F)
        val fruitBranchPath = Path().apply {
            moveTo(w * 0.5f, h * 0.52f)
            quadraticTo(w * 0.68f, h * 0.52f, w * 0.72f, h * 0.56f)
        }
        drawPath(fruitBranchPath, color = Color(0xFF388E3C), style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round))

        val fruitCenter = Offset(w * 0.72f, h * 0.58f)
        drawCircle(color = fruitColor, radius = 18.dp.toPx(), center = fruitCenter)
        drawCircle(color = Color(0x99FFFFFF), radius = 5.dp.toPx(), center = Offset(fruitCenter.x - 6.dp.toPx(), fruitCenter.y - 6.dp.toPx()))

        // 8. Flower
        val isFlowerSelected = selectedPartId == "flower"
        val flowerCenter = Offset(w * 0.5f, h * 0.18f)
        val petalColor = if (isFlowerSelected) Color(0xFFFF4081) else Color(0xFFF06292)
        val petalRadius = 24.dp.toPx()

        for (i in 0 until 8) {
            val angle = i * (Math.PI * 2 / 8)
            val petalOffset = Offset(
                flowerCenter.x + (cos(angle) * petalRadius).toFloat(),
                flowerCenter.y + (sin(angle) * petalRadius).toFloat()
            )
            drawCircle(color = petalColor, radius = 16.dp.toPx(), center = petalOffset)
        }
        drawCircle(color = Color(0xFFFFA000), radius = 20.dp.toPx(), center = flowerCenter)
        drawCircle(color = Color(0xFF3E2723), radius = 2.5.dp.toPx(), center = Offset(flowerCenter.x - 6.dp.toPx(), flowerCenter.y - 3.dp.toPx()))
        drawCircle(color = Color(0xFF3E2723), radius = 2.5.dp.toPx(), center = Offset(flowerCenter.x + 6.dp.toPx(), flowerCenter.y - 3.dp.toPx()))
        drawPath(
            Path().apply {
                moveTo(flowerCenter.x - 7.dp.toPx(), flowerCenter.y + 4.dp.toPx())
                quadraticTo(flowerCenter.x, flowerCenter.y + 9.dp.toPx(), flowerCenter.x + 7.dp.toPx(), flowerCenter.y + 4.dp.toPx())
            },
            color = Color(0xFF3E2723),
            style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
        )

        drawHotspotBadge("🌸", Offset(flowerCenter.x, flowerCenter.y - 32.dp.toPx()), isFlowerSelected, pulseScale)
        drawHotspotBadge("🍃", Offset(w * 0.28f, h * 0.44f), isLeafSelected, pulseScale)
        drawHotspotBadge("🍎", Offset(fruitCenter.x + 22.dp.toPx(), fruitCenter.y), isFruitSelected, pulseScale)
        drawHotspotBadge("🌿", Offset(w * 0.5f, h * 0.32f), isStemSelected, pulseScale)
        drawHotspotBadge("🌱", Offset(seedCenter.x, seedCenter.y - 20.dp.toPx()), isSeedSelected, pulseScale)
        drawHotspotBadge("🪱", Offset(w * 0.44f, groundY + 45.dp.toPx()), isRootSelected, pulseScale)
    }
}

private fun DrawScope.drawRootBranch(
    startX: Float,
    startY: Float,
    endX: Float,
    endY: Float,
    color: Color,
    strokeWidth: Float
) {
    val path = Path().apply {
        moveTo(startX, startY)
        quadraticTo((startX + endX) / 2f + 10f, (startY + endY) / 2f, endX, endY)
    }
    drawPath(path, color, style = Stroke(width = strokeWidth, cap = StrokeCap.Round))
}

private fun DrawScope.drawHotspotBadge(
    symbol: String,
    center: Offset,
    isSelected: Boolean,
    pulse: Float
) {
    val radius = if (isSelected) 18.dp.toPx() * pulse else 14.dp.toPx()
    val bg = if (isSelected) Color(0xFFFFD54F) else Color.White
    drawCircle(color = bg, radius = radius, center = center)
    drawCircle(color = Color(0xFF2E7D32), radius = radius, center = center, style = Stroke(width = 2.dp.toPx()))
}

@Composable
fun PlantMicroscopeCanvas(
    part: PlantPart,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "micro_anim")
    val flowRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )
    val poreBreath by infiniteTransition.animateFloat(
        initialValue = 4f,
        targetValue = 18f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breath"
    )

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val center = Offset(w / 2f, h / 2f)
        val outerRadius = (w.coerceAtMost(h) / 2f) - 16.dp.toPx()

        // Microscope Dark Field & Glass Specimen Circle
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFF0D2818), Color(0xFF041208)),
                center = center,
                radius = outerRadius
            ),
            radius = outerRadius,
            center = center
        )

        // Microscope Eyepiece Brass/Cyan Rim
        drawCircle(
            color = Color(0xFF00B4D8),
            radius = outerRadius,
            center = center,
            style = Stroke(width = 8.dp.toPx())
        )
        drawCircle(
            color = Color(0xFF90E0EF),
            radius = outerRadius - 4.dp.toPx(),
            center = center,
            style = Stroke(width = 2.dp.toPx())
        )

        // Specimen specifics
        when (part.id) {
            "leaf" -> {
                // Stomata pore breathing in center
                drawOval(
                    color = Color(0xFF2D6A4F),
                    topLeft = Offset(center.x - 30.dp.toPx(), center.y - 50.dp.toPx()),
                    size = Size(60.dp.toPx(), 100.dp.toPx())
                )
                // Breathing pore slit
                drawOval(
                    color = Color(0xFFD8F3DC),
                    topLeft = Offset(center.x - poreBreath.dp.toPx() / 2f, center.y - 35.dp.toPx()),
                    size = Size(poreBreath.dp.toPx(), 70.dp.toPx())
                )
                // Chloroplast discs floating around
                for (i in 0 until 8) {
                    val angle = (i * (Math.PI * 2 / 8) + Math.toRadians(flowRotation.toDouble())).toFloat()
                    val dist = outerRadius * 0.65f
                    val posX = center.x + cos(angle) * dist
                    val posY = center.y + sin(angle) * dist
                    drawCircle(Color(0xFF52B788), 16.dp.toPx(), Offset(posX, posY))
                    drawCircle(Color(0xFF74C69D), 8.dp.toPx(), Offset(posX - 4.dp.toPx(), posY - 4.dp.toPx()))
                }
            }

            "stem" -> {
                // Twin Xylem & Phloem straws
                val xylemCenter = Offset(center.x - 36.dp.toPx(), center.y)
                val phloemCenter = Offset(center.x + 36.dp.toPx(), center.y)

                // Xylem (Blue Water pipe)
                drawCircle(Color(0xFF023E8A), 42.dp.toPx(), xylemCenter)
                drawCircle(Color(0xFF0077B6), 34.dp.toPx(), xylemCenter)
                drawCircle(Color(0xFF90E0EF), 18.dp.toPx() * (poreBreath / 15f), xylemCenter)

                // Phloem (Golden Sugar pipe)
                drawCircle(Color(0xFFB08968), 42.dp.toPx(), phloemCenter)
                drawCircle(Color(0xFFD4A373), 34.dp.toPx(), phloemCenter)
                drawCircle(Color(0xFFFFD166), 18.dp.toPx() * (poreBreath / 15f), phloemCenter)
            }

            "flower" -> {
                // Golden Pollen Grain with tiny hook spikes
                drawCircle(Color(0xFFFFB703), 48.dp.toPx(), center)
                drawCircle(Color(0xFFFB8500), 40.dp.toPx(), center)
                // Spikes on pollen
                for (i in 0 until 12) {
                    val angle = i * (Math.PI * 2 / 12) + Math.toRadians(flowRotation.toDouble() * 0.5)
                    val p1 = Offset(center.x + (cos(angle) * 44.dp.toPx()).toFloat(), center.y + (sin(angle) * 44.dp.toPx()).toFloat())
                    val p2 = Offset(center.x + (cos(angle) * 62.dp.toPx()).toFloat(), center.y + (sin(angle) * 62.dp.toPx()).toFloat())
                    drawLine(Color(0xFFFFD166), p1, p2, 4.dp.toPx(), StrokeCap.Round)
                }
            }

            "root" -> {
                // Root epidermis with fuzzy hair tubes
                drawRect(
                    color = Color(0xFF6F4E37),
                    topLeft = Offset(center.x - 80.dp.toPx(), center.y - 80.dp.toPx()),
                    size = Size(60.dp.toPx(), 160.dp.toPx())
                )
                // Long translucent root hairs drinking blue water molecules
                for (i in 0 until 5) {
                    val y = center.y - 60.dp.toPx() + (i * 30.dp.toPx())
                    drawLine(
                        Color(0xFFE9ECEF),
                        Offset(center.x - 20.dp.toPx(), y),
                        Offset(center.x + 60.dp.toPx(), y + (sin(flowRotation * 0.05f + i) * 12.dp.toPx())),
                        6.dp.toPx(),
                        StrokeCap.Round
                    )
                    drawCircle(Color(0xFF48CAE4), 6.dp.toPx(), Offset(center.x + 72.dp.toPx(), y))
                }
            }

            else -> {
                // Seed / Fruit embryo cell matrix
                drawCircle(Color(0xFFD4A373), 56.dp.toPx(), center)
                drawCircle(Color(0xFFE9EDC9), 45.dp.toPx(), center)
                // Baby leaf curl
                drawPath(
                    Path().apply {
                        moveTo(center.x, center.y + 20.dp.toPx())
                        quadraticTo(center.x - 25.dp.toPx(), center.y - 20.dp.toPx(), center.x + 10.dp.toPx(), center.y - 25.dp.toPx())
                    },
                    Color(0xFF588157),
                    style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
                )
            }
        }

        // Lens Grid Crosshair
        drawLine(Color(0x33FFFFFF), Offset(center.x - outerRadius, center.y), Offset(center.x + outerRadius, center.y), 1.dp.toPx())
        drawLine(Color(0x33FFFFFF), Offset(center.x, center.y - outerRadius), Offset(center.x, center.y + outerRadius), 1.dp.toPx())
    }
}

@Composable
fun GrowthStageCanvas(
    stage: GrowthStage,
    plantType: String,
    weather: WeatherCondition = WeatherCondition.SUNNY,
    health: PlantHealthStatus = PlantHealthStatus.HEALTHY,
    potStyle: PotStyle = PotStyle.TERRACOTTA,
    onTapBug: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "breeze")
    val swayMultiplier = if (weather == WeatherCondition.WINDY) 2.4f else 1.0f
    val sway by infiniteTransition.animateFloat(
        initialValue = -3f * swayMultiplier,
        targetValue = 3f * swayMultiplier,
        animationSpec = infiniteRepeatable(
            animation = tween(if (weather == WeatherCondition.WINDY) 800 else 1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "sway"
    )
    val rainDropY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(700, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rain"
    )

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val potTopY = h * 0.65f

        // Weather Atmospheric Background
        when (weather) {
            WeatherCondition.NIGHT -> {
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF0F172A), Color(0xFF1E293B)),
                        startY = 0f,
                        endY = potTopY
                    ),
                    size = Size(w, potTopY)
                )
                // Moon
                drawCircle(Color(0xFFFFF9C4), 22.dp.toPx(), Offset(w * 0.82f, h * 0.12f))
                drawCircle(Color(0xFF0F172A), 16.dp.toPx(), Offset(w * 0.86f, h * 0.10f))
            }
            WeatherCondition.RAINY -> {
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF90A4AE), Color(0xFFCFD8DC)),
                        startY = 0f,
                        endY = potTopY
                    ),
                    size = Size(w, potTopY)
                )
                // Rain drops falling
                for (i in 0 until 12) {
                    val dropX = (w / 12f) * i + (i * 7f) % (w / 12f)
                    val dropStart = (rainDropY + (i * 0.08f)) % 1f * potTopY
                    drawLine(Color(0xFF4FC3F7), Offset(dropX, dropStart), Offset(dropX - 4.dp.toPx(), dropStart + 16.dp.toPx()), 2.5.dp.toPx(), StrokeCap.Round)
                }
            }
            WeatherCondition.WINDY -> {
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFFE0F7FA), Color(0xFFE8F5E9)),
                        startY = 0f,
                        endY = potTopY
                    ),
                    size = Size(w, potTopY)
                )
            }
            WeatherCondition.SUNNY -> {
                // Clear blue sky
            }
        }

        // Selected Pot Style
        val potColor = potStyle.potColor
        val rimColor = potStyle.rimColor

        val potPath = Path().apply {
            moveTo(w * 0.28f, potTopY)
            lineTo(w * 0.72f, potTopY)
            lineTo(w * 0.66f, h * 0.95f)
            lineTo(w * 0.34f, h * 0.95f)
            close()
        }
        drawPath(potPath, color = potColor)
        drawRoundRect(
            color = rimColor,
            topLeft = Offset(w * 0.25f, potTopY - 10.dp.toPx()),
            size = Size(w * 0.5f, 16.dp.toPx()),
            cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx())
        )
        // Soil
        drawOval(
            color = Color(0xFF5D4037),
            topLeft = Offset(w * 0.3f, potTopY - 6.dp.toPx()),
            size = Size(w * 0.4f, 12.dp.toPx())
        )

        // Cute pot face
        drawCircle(Color(0xFF3E2723), 3.dp.toPx(), Offset(w * 0.44f, potTopY + 36.dp.toPx()))
        drawCircle(Color(0xFF3E2723), 3.dp.toPx(), Offset(w * 0.56f, potTopY + 36.dp.toPx()))
        drawPath(
            Path().apply {
                moveTo(w * 0.46f, potTopY + 44.dp.toPx())
                quadraticTo(w * 0.5f, potTopY + 50.dp.toPx(), w * 0.54f, potTopY + 44.dp.toPx())
            },
            color = Color(0xFF3E2723),
            style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round)
        )

        // Thirsty droop factor
        val droop = if (health == PlantHealthStatus.THIRSTY) 24.dp.toPx() else 0f
        val tint = when (health) {
            PlantHealthStatus.SUNBURNED -> Color(0xFFFFB74D)
            PlantHealthStatus.THIRSTY -> Color(0xFF81C784).copy(alpha = 0.8f)
            else -> Color.Unspecified
        }

        when (stage) {
            GrowthStage.SEED -> {
                val seedOffset = Offset(w * 0.5f, potTopY + 12.dp.toPx())
                drawOval(
                    color = Color(0xFF8D6E63),
                    topLeft = Offset(seedOffset.x - 12.dp.toPx(), seedOffset.y - 8.dp.toPx()),
                    size = Size(24.dp.toPx(), 16.dp.toPx())
                )
                drawPath(
                    Path().apply {
                        moveTo(seedOffset.x, seedOffset.y + 6.dp.toPx())
                        lineTo(seedOffset.x, seedOffset.y + 16.dp.toPx())
                    },
                    color = Color(0xFFFFF9C4),
                    style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                )
            }

            GrowthStage.SPROUT -> {
                val stemPath = Path().apply {
                    moveTo(w * 0.5f, potTopY)
                    quadraticTo(w * 0.5f + sway, potTopY - 25.dp.toPx() + droop * 0.3f, w * 0.5f, potTopY - 50.dp.toPx() + droop)
                }
                drawPath(stemPath, Color(0xFF81C784), style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round))

                drawOval(
                    color = if (tint != Color.Unspecified) tint else Color(0xFF66BB6A),
                    topLeft = Offset(w * 0.5f - 24.dp.toPx(), potTopY - 60.dp.toPx() + droop),
                    size = Size(22.dp.toPx(), 14.dp.toPx())
                )
                drawOval(
                    color = if (tint != Color.Unspecified) tint else Color(0xFF81C784),
                    topLeft = Offset(w * 0.5f + 2.dp.toPx(), potTopY - 60.dp.toPx() + droop),
                    size = Size(22.dp.toPx(), 14.dp.toPx())
                )
            }

            GrowthStage.SEEDLING -> {
                val stemPath = Path().apply {
                    moveTo(w * 0.5f, potTopY)
                    quadraticTo(w * 0.49f + sway, potTopY - 60.dp.toPx() + droop * 0.4f, w * 0.5f, potTopY - 110.dp.toPx() + droop)
                }
                drawPath(stemPath, Color(0xFF43A047), style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round))

                drawOval(
                    color = if (tint != Color.Unspecified) tint else Color(0xFF4CAF50),
                    topLeft = Offset(w * 0.5f - 38.dp.toPx(), potTopY - 75.dp.toPx() + droop),
                    size = Size(36.dp.toPx(), 18.dp.toPx())
                )
                drawOval(
                    color = if (tint != Color.Unspecified) tint else Color(0xFF66BB6A),
                    topLeft = Offset(w * 0.5f + 2.dp.toPx(), potTopY - 85.dp.toPx() + droop),
                    size = Size(36.dp.toPx(), 18.dp.toPx())
                )
                drawCircle(Color(0xFF81C784), 10.dp.toPx(), Offset(w * 0.5f, potTopY - 112.dp.toPx() + droop))
            }

            GrowthStage.BLOOMING, GrowthStage.HARVEST -> {
                val topFlowerY = potTopY - 140.dp.toPx() + droop
                val stemPath = Path().apply {
                    moveTo(w * 0.5f, potTopY)
                    quadraticTo(w * 0.48f + sway, potTopY - 70.dp.toPx() + droop * 0.5f, w * 0.5f, topFlowerY)
                }
                drawPath(stemPath, Color(0xFF2E7D32), style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round))

                drawOval(
                    color = if (tint != Color.Unspecified) tint else Color(0xFF388E3C),
                    topLeft = Offset(w * 0.5f - 52.dp.toPx(), potTopY - 70.dp.toPx() + droop),
                    size = Size(50.dp.toPx(), 24.dp.toPx())
                )
                drawOval(
                    color = if (tint != Color.Unspecified) tint else Color(0xFF43A047),
                    topLeft = Offset(w * 0.5f + 2.dp.toPx(), potTopY - 95.dp.toPx() + droop),
                    size = Size(50.dp.toPx(), 24.dp.toPx())
                )

                // Caterpillar on leaf if health is caterpillar!
                if (health == PlantHealthStatus.CATERPILLAR) {
                    val bugX = w * 0.5f + 20.dp.toPx()
                    val bugY = potTopY - 90.dp.toPx()
                    drawCircle(Color(0xFF8BC34A), 6.dp.toPx(), Offset(bugX, bugY))
                    drawCircle(Color(0xFF7CB342), 6.dp.toPx(), Offset(bugX + 9.dp.toPx(), bugY))
                    drawCircle(Color(0xFF689F38), 6.dp.toPx(), Offset(bugX + 18.dp.toPx(), bugY))
                    // Cute eye
                    drawCircle(Color.Black, 1.5.dp.toPx(), Offset(bugX + 20.dp.toPx(), bugY - 2.dp.toPx()))
                }

                when (plantType) {
                    "SUNFLOWER" -> {
                        val center = Offset(w * 0.5f, topFlowerY)
                        // If night, petals fold close
                        val petalDistance = if (weather == WeatherCondition.NIGHT) 18.dp.toPx() else 32.dp.toPx()
                        for (i in 0 until 12) {
                            val angle = i * (Math.PI * 2 / 12)
                            val petalPos = Offset(
                                center.x + (cos(angle) * petalDistance).toFloat(),
                                center.y + (sin(angle) * petalDistance).toFloat()
                            )
                            drawCircle(Color(0xFFFFD54F), 18.dp.toPx(), petalPos)
                        }
                        drawCircle(Color(0xFF5D4037), 26.dp.toPx(), center)
                        drawCircle(Color(0xFFFFE082), 3.5.dp.toPx(), Offset(center.x - 8.dp.toPx(), center.y - 4.dp.toPx()))
                        drawCircle(Color(0xFFFFE082), 3.5.dp.toPx(), Offset(center.x + 8.dp.toPx(), center.y - 4.dp.toPx()))
                        drawPath(
                            Path().apply {
                                moveTo(center.x - 8.dp.toPx(), center.y + 6.dp.toPx())
                                quadraticTo(center.x, center.y + 12.dp.toPx(), center.x + 8.dp.toPx(), center.y + 6.dp.toPx())
                            },
                            Color(0xFFFFE082),
                            style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round)
                        )
                    }

                    "STRAWBERRY" -> {
                        val berry1 = Offset(w * 0.38f, potTopY - 50.dp.toPx())
                        val berry2 = Offset(w * 0.64f, potTopY - 45.dp.toPx())
                        drawStrawberry(berry1)
                        drawStrawberry(berry2)
                        drawCircle(Color(0xFFFFF9C4), 14.dp.toPx(), Offset(w * 0.5f, topFlowerY))
                        drawCircle(Color(0xFFFFA000), 7.dp.toPx(), Offset(w * 0.5f, topFlowerY))
                    }

                    "CACTUS" -> {
                        val cactusCenter = Offset(w * 0.5f, potTopY - 70.dp.toPx())
                        drawRoundRect(
                            color = Color(0xFF43A047),
                            topLeft = Offset(cactusCenter.x - 30.dp.toPx(), cactusCenter.y - 60.dp.toPx()),
                            size = Size(60.dp.toPx(), 120.dp.toPx()),
                            cornerRadius = CornerRadius(30.dp.toPx(), 30.dp.toPx())
                        )
                        drawLine(Color(0xFFFFF9C4), Offset(cactusCenter.x - 32.dp.toPx(), cactusCenter.y), Offset(cactusCenter.x - 42.dp.toPx(), cactusCenter.y - 4.dp.toPx()), 2.dp.toPx())
                        drawLine(Color(0xFFFFF9C4), Offset(cactusCenter.x + 32.dp.toPx(), cactusCenter.y - 20.dp.toPx()), Offset(cactusCenter.x + 42.dp.toPx(), cactusCenter.y - 24.dp.toPx()), 2.dp.toPx())
                        drawCircle(Color(0xFFEC407A), 16.dp.toPx(), Offset(cactusCenter.x, cactusCenter.y - 62.dp.toPx()))
                    }

                    else -> {
                        val center = Offset(w * 0.5f, topFlowerY)
                        drawCircle(Color(0xFFEC407A), 32.dp.toPx(), center)
                        drawCircle(Color(0xFFF48FB1), 22.dp.toPx(), center)
                        drawCircle(Color(0xFFFFEB3B), 12.dp.toPx(), center)
                    }
                }
            }
        }
    }
}

private fun DrawScope.drawStrawberry(center: Offset) {
    drawOval(
        color = Color(0xFFE53935),
        topLeft = Offset(center.x - 14.dp.toPx(), center.y - 12.dp.toPx()),
        size = Size(28.dp.toPx(), 30.dp.toPx())
    )
    drawCircle(Color(0xFF43A047), 6.dp.toPx(), Offset(center.x, center.y - 12.dp.toPx()))
    drawCircle(Color(0xFFFFEB3B), 1.5.dp.toPx(), Offset(center.x - 5.dp.toPx(), center.y))
    drawCircle(Color(0xFFFFEB3B), 1.5.dp.toPx(), Offset(center.x + 5.dp.toPx(), center.y + 4.dp.toPx()))
    drawCircle(Color(0xFFFFEB3B), 1.5.dp.toPx(), Offset(center.x, center.y + 8.dp.toPx()))
}
