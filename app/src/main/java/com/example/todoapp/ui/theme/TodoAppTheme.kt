package com.example.todoapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0XFFBB86FC),
    secondary = Color(0xFF03DAC5)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0XFF6200EE),
    secondary = Color(0xFF03DAC5)
)
@Composable
fun TodoAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
)
{
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        shapes = Shapes(
            medium = RoundedCornerShape(4.dp)
        ),
        content = content
    )
}

