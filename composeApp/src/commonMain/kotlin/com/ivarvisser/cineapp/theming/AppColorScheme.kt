package com.ivarvisser.cineapp.theming

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val LightColorScheme = lightColorScheme(
    primary = Color(0xFF2B2B2B),          // Mud: Primary
    onPrimary = Color(0xFFFFFFFF),

    secondary = Color(0xFFD4AF37),       // Mud: Secondary
    onSecondary = Color(0xFF1A1A1A),

    tertiary = Color(0xFF5A4A7A),        // Mud: Tertiary
    onTertiary = Color(0xFFFFFFFF),

    background = Color(0xFFF4F4F4),      // Mud: Background
    onBackground = Color(0xFF1A1A1A),

    surface = Color(0xFFFFFFFF),         // Mud: Surface
    onSurface = Color(0xFF1A1A1A),

    surfaceVariant = Color(0xFFD6D6D6),  // Mud: Divider-ish

    error = Color(0xFFC62828),           // Mud: Error
    onError = Color(0xFFFFFFFF),

    outline = Color(0xFFD6D6D6)
)

val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFFB6F17),         // Mud: Primary (orange)
    onPrimary = Color(0xFFFFFFFF),

    secondary = Color(0xFF7927FE),      // Mud: Secondary (purple)
    onSecondary = Color(0xFFFFFFFF),

    tertiary = Color(0xFF2B2B2B),       // Mud: Tertiary (anthracite)
    onTertiary = Color(0xFFFFFFFF),

    background = Color(0xFF111424),     // Mud: Background
    onBackground = Color(0xFFFFFFFF),

    surface = Color(0xFF080A11),        // Mud: Surface
    onSurface = Color(0xFFFFFFFF),

    surfaceVariant = Color(0xFF2C2C2C), // Mud: Divider-ish

    error = Color(0xFFEF5350), onError = Color(0xFFFFFFFF),

    outline = Color(0xFF2C2C2C)
)