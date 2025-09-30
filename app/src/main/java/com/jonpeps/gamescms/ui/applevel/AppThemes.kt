package com.jonpeps.gamescms.ui.applevel

import androidx.compose.ui.graphics.Color

class CustomColours(isDark: Boolean) {
    val background: Color = if (isDark)
        Color(0xFF000000)
            else Color((0xFF3A3A3A))

    val primary: Color = if (isDark)
        Color(0xFFFFFFFF)
            else Color(0xFF000000)

    val spinner: Color = if (isDark)
        Color(0xFFFFFFFF)
            else Color(0xFF000000)

    val secondary: Color = if (isDark)
        Color(0xFF000000)
            else Color(0xFFFFFFFF)
}