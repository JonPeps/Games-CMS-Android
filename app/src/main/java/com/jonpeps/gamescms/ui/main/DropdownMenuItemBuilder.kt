package com.jonpeps.gamescms.ui.main

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.TextUnit

data class CustomMenuItemText(val text: String,
                              val fontSize: TextUnit,
                              val color: Color,
                              val fontStyle: FontStyle)

data class CustomMenuItem(val customMenuItemText: CustomMenuItemText,
                          val enabled: Boolean,
                          val onClick: () -> Unit)

class CustomMenuBuilder private constructor() {
    data class Builder(val customMenuItems: MutableList<CustomMenuItem> = mutableListOf()) {
        fun add(customMenuItemText: CustomMenuItemText, enabled: Boolean, onClick: () -> Unit) =
            apply { customMenuItems.add(CustomMenuItem(customMenuItemText, enabled, onClick)) }

        fun build(): @Composable (ColumnScope.() -> Unit) {
            return ColumnScope@ {
                customMenuItems.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item.customMenuItemText.text,
                                      fontSize = item.customMenuItemText.fontSize,
                                      color = item.customMenuItemText.color,
                                      fontStyle = item.customMenuItemText.fontStyle) },
                        enabled = item.enabled,
                        onClick = item.onClick
                    )
                }
            }
        }
    }
}