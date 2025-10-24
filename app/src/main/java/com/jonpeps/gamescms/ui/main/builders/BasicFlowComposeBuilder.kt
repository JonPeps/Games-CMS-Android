package com.jonpeps.gamescms.ui.main.builders

import android.content.Context
import androidx.compose.runtime.Composable
import com.jonpeps.gamescms.ui.applevel.CustomColours
import com.jonpeps.gamescms.ui.main.builders.data.CustomItemText
import com.jonpeps.gamescms.ui.viewmodels.IScreenFlowViewModel

class BasicFlowComposeBuilder private constructor() {
    data class Builder(val context: Context,
                       val viewModel: IScreenFlowViewModel<Screen>,
                       val customColours: CustomColours,
                       val customMenuItems: MutableList<CustomMenuItem> = mutableListOf())
         {

        private var showBackIcon = false
        private var showMenuItems = true
        private lateinit var onBack: () -> Unit
        val screenFlowBuilder = ScreenFlowBuilder.Builder()

        fun addDropdownMenuItem(
            customMenuItemText: CustomItemText,
            enabled: Boolean,
            onClick: () -> Unit
        ) = apply { customMenuItems.add(CustomMenuItem(customMenuItemText, enabled, onClick)) }

        fun addScreenItem(screenName: String, content: @Composable () -> Unit) = apply {
            screenFlowBuilder.add(screenName, content)
        }

        fun showMenuItems(show: Boolean) = apply {
            showMenuItems = show
        }

        fun showBackIcon(show: Boolean) = apply {
            showBackIcon = show
        }

        fun onBack(onBack: () -> Unit) = apply {
            this.onBack = onBack
        }

        @Composable
        fun Build() {
            val mainFlowWithNavBarBuilder =
                NavBarBuilder.Builder(context, customColours)

            if (showBackIcon) {
                mainFlowWithNavBarBuilder
                    .showBackIcon(true)
                    .onIconBack {
                        if (!viewModel.popBackStack()) {
                            onBack()
                        }
                }
            }
            if (showMenuItems) {
                customMenuItems.forEach {
                    mainFlowWithNavBarBuilder.addDropdownMenuItem(
                        it.customMenuItemText,
                        it.enabled?:true,
                        it.onClick)
                }
                mainFlowWithNavBarBuilder.showMenu(true)
            }
            mainFlowWithNavBarBuilder.setContent {
                NavBuilder
                    .Builder(context, viewModel)
                    .setOnBack {
                        if (!viewModel.popBackStack()) {
                            onBack()
                        }
                    }
                    .navContent(screenFlowBuilder.build()).Build()
            }.Build()
        }
    }
}