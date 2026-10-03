package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.delay
import kotlin.random.Random

data class ConfettiParticle(
    val xRatio: Float,
    val initialYRatio: Float,
    val speed: Float,
    val size: Float,
    val color: Color,
    val rotationSpeed: Float,
    val shapeType: Int // 0: rect, 1: circle, 2: star
)

@Composable
fun ConfettiExplosion(
    isActive: Boolean,
    onFinished: () -> Unit = {}
) {
    if (!isActive) return

    val colors = listOf(
        Color(0xFFFFD54F),
        Color(0xFF66BB6A),
        Color(0xFF42A5F5),
        Color(0xFFFF7043),
        Color(0xFFEC407A),
        Color(0xFFAB47BC)
    )

    val particles = remember {
        List(45) {
            ConfettiParticle(
                xRatio = Random.nextFloat(),
                initialYRatio = Random.nextFloat() * 0.2f,
                speed = 0.8f + Random.nextFloat() * 0.8f,
                size = 12f + Random.nextFloat() * 14f,
                color = colors[Random.nextInt(colors.size)],
                rotationSpeed = (Random.nextFloat() - 0.5f) * 10f,
                shapeType = Random.nextInt(3)
            )
        }
    }

    val progress = remember { Animatable(0f) }

    LaunchedEffect(isActive) {
        progress.snapTo(0f)
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 2200, easing = LinearEasing)
        )
        delay(200)
        onFinished()
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val currentProgress = progress.value
        val canvasWidth = size.width
        val canvasHeight = size.height

        particles.forEach { p ->
            val y = (p.initialYRatio + (currentProgress * p.speed)) * canvasHeight
            val x = (p.xRatio * canvasWidth) + (kotlin.math.sin(currentProgress * 8f + p.xRatio * 10f) * 30f)

            if (y <= canvasHeight + 40f) {
                when (p.shapeType) {
                    0 -> {
                        drawRect(
                            color = p.color,
                            topLeft = Offset(x, y),
                            size = Size(p.size, p.size * 0.6f)
                        )
                    }
                    1 -> {
                        drawCircle(
                            color = p.color,
                            radius = p.size / 2f,
                            center = Offset(x, y)
                        )
                    }
                    else -> {
                        // Flower petal / oval
                        drawOval(
                            color = p.color,
                            topLeft = Offset(x, y),
                            size = Size(p.size * 1.2f, p.size * 0.7f)
                        )
                    }
                }
            }
        }
    }
}
