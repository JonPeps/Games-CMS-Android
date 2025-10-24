package com.jonpeps.gamescms.ui.main.builders.core

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider

data class Screen(val screenName: String)
data class ScreenFlowEntryItem(val screen: Screen, val content: @Composable () -> Unit)

class ScreenFlowBuilder private constructor() {
    data class Builder(val screenFlowEntryItems: MutableList<ScreenFlowEntryItem> = mutableListOf()) {
        fun add(screenName: String, content: @Composable () -> Unit)
        = apply { screenFlowEntryItems.add(ScreenFlowEntryItem(Screen(screenName), content)) }

        fun build(): (Screen) -> NavEntry<Screen> {
            return entryProvider {
                entry<Screen> { screen ->
                    screenFlowEntryItems.find { it.screen == screen }?.content?.invoke()
                }
            }
        }
    }
}