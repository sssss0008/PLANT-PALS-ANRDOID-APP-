package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

private val LightColorScheme = lightColorScheme(
    primary = ForestGreenPrimary,
    onPrimary = ForestGreenOnPrimary,
    primaryContainer = LeafGreenContainer,
    onPrimaryContainer = LeafGreenOnContainer,
    secondary = SunnyAmberSecondary,
    onSecondary = Color.White,
    secondaryContainer = SunnyAmberContainer,
    onSecondaryContainer = SunnyAmberOnContainer,
    tertiary = SproutTealTertiary,
    onTertiary = Color.White,
    background = MeadowLightBg,
    onBackground = Color(0xFF1E2E20),
    surface = MeadowSurface,
    onSurface = Color(0xFF1E2E20),
    surfaceVariant = MeadowSurfaceVariant,
    onSurfaceVariant = Color(0xFF2C3E2D)
)

private val DarkColorScheme = darkColorScheme(
    primary = SproutGreenDark,
    onPrimary = Color(0xFF00390F),
    primaryContainer = Color(0xFF1B4D20),
    onPrimaryContainer = Color(0xFFC8E6C9),
    secondary = SunnyYellowDark,
    onSecondary = Color(0xFF3E2723),
    secondaryContainer = Color(0xFF5D4037),
    onSecondaryContainer = Color(0xFFFFECB3),
    tertiary = TealDark,
    background = DarkBg,
    onBackground = Color(0xFFE8F5E9),
    surface = DarkSurface,
    onSurface = Color(0xFFE8F5E9)
)

val KidFriendlyShapes = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(20.dp),
    large = RoundedCornerShape(28.dp),
    extraLarge = RoundedCornerShape(36.dp)
)

@Composable
fun PlantPalsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep cheerful custom green & sunny theme for kids
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = KidFriendlyShapes,
        content = content
    )
}
