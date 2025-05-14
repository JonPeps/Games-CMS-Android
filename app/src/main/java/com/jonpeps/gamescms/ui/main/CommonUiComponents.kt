package com.jonpeps.gamescms.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jonpeps.gamescms.ui.applevel.DarkColors
import com.jonpeps.gamescms.ui.applevel.LightColors

@Composable
fun CommonLoadingScreen(useDarkTheme: Boolean = isSystemInDarkTheme()) {
    val colors = if (useDarkTheme) {
        DarkColors
    } else {
        LightColors
    }
    val backgroundColor
            = Color(colors.background.red,
        colors.background.green,
        colors.background.blue,
        alpha = 0.75f)
    Box(contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(64.dp),
            color = colors.onBackground,
            strokeWidth = 5.dp
        )
    }
}