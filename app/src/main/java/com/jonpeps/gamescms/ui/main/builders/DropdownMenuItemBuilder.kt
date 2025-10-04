package com.jonpeps.gamescms.ui.main.builders

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.jonpeps.gamescms.ui.main.builders.data.CustomItemText

data class CustomMenuItem(val customMenuItemText: CustomItemText,
                          val enabled: Boolean,
                          val onClick: () -> Unit)

class DropdownMenuItemBuilder private constructor() {
    data class Builder(val customMenuItems: MutableList<CustomMenuItem> = mutableListOf()) {
        fun add(customMenuItemText: CustomItemText, enabled: Boolean, onClick: () -> Unit) =
            apply { customMenuItems.add(CustomMenuItem(customMenuItemText, enabled, onClick)) }

        @Composable
        fun Build() {
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