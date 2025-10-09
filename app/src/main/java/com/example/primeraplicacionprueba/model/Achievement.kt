package com.example.primeraplicacionprueba.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val icon: ImageVector,
    val isUnlocked: Boolean,
    val progress: Float = 1f, // 0f a 1f
    val colorGradient: List<Color>
)