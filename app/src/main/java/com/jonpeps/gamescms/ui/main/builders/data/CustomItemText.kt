package com.jonpeps.gamescms.ui.main.builders.data

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.TextUnit

data class CustomItemText(
    val text: String,
    val fontSize: TextUnit,
    val color: Color,
    val fontStyle: FontStyle
)