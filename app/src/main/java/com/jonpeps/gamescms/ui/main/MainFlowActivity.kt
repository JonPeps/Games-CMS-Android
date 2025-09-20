package com.jonpeps.gamescms.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import com.jonpeps.gamescms.data.DataConstants.Companion.MAIN_MENU_PROJECTS_ITEM
import com.jonpeps.gamescms.data.DataConstants.Companion.MAIN_MENU_TEMPLATES_ITEM
import com.jonpeps.gamescms.ui.applevel.DarkColors
import com.jonpeps.gamescms.ui.applevel.LightColors
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: ScreenFlowViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val useDarkTheme = isSystemInDarkTheme()
            val colors = if (useDarkTheme) {
                DarkColors
            } else {
                LightColors
            }

            Surface {
                MaterialTheme(
                    colorScheme = colors,
                ) {
                    SetupNavigation(viewModel, colors)
                }
            }
        }
    }

    @Composable
    fun ShowStartScreen(viewModel: ScreenFlowViewModel, backgroundColour: Color) {
        Box(modifier = Modifier.fillMaxHeight().fillMaxWidth().background(backgroundColour)) {
            CommonStringListView(
                listOf(MAIN_MENU_PROJECTS_ITEM, MAIN_MENU_TEMPLATES_ITEM), { text ->
                    if (text == MAIN_MENU_PROJECTS_ITEM) {
                        viewModel.navigateTo(ScreenFlow.Screen(MAIN_MENU_PROJECTS_ITEM))
                    } else {
                        viewModel.navigateTo(ScreenFlow.Screen(MAIN_MENU_TEMPLATES_ITEM))
                    }
                })
        }
    }

    @Composable
    fun SetupNavigation(viewModel: ScreenFlowViewModel, colourScheme: ColorScheme) {
        val backStack = viewModel.backStack

        BackHandler(enabled = backStack.size > 1) {
            viewModel.popBackStack()
        }

        NavDisplay(backStack = backStack,
            onBack = {
                if (!viewModel.popBackStack()) {
                    finish()
                }
            }, entryDecorators = listOf(
               rememberSceneSetupNavEntryDecorator(),
               rememberSavedStateNavEntryDecorator(),
               rememberViewModelStoreNavEntryDecorator()),
            entryProvider = { key ->
                when (key) {
                    ScreenFlow.Start -> NavEntry(ScreenFlow.Start) {
                        ShowStartScreen(viewModel, colourScheme.scrim)
                    }
                    is ScreenFlow.Screen -> NavEntry(key) {
                        when (key.screenName) {
                            MAIN_MENU_PROJECTS_ITEM -> OnProjectsListSelected(viewModel, colourScheme)
                        }
                    }
                    ScreenFlow.Finish -> NavEntry(ScreenFlow.Finish) {
                        finish()
                    }
                }
            },
            transitionSpec = {
                slideInHorizontally(initialOffsetX = { it }) togetherWith
                        slideOutHorizontally(targetOffsetX = { -it })
            },
            popTransitionSpec = {
                slideInHorizontally(initialOffsetX = { -it }) togetherWith
                        slideOutHorizontally(targetOffsetX = { it })
            },
            predictivePopTransitionSpec = {
                slideInHorizontally(initialOffsetX = { -it }) togetherWith
                        slideOutHorizontally(targetOffsetX = { it })
            }
        )
    }

    @Composable
    fun OnProjectsListSelected(viewModel: ScreenFlowViewModel, colourScheme: ColorScheme) {
        ShowProjectsList(
            applicationContext,
            colourScheme.scrim,
            { _ ->
                viewModel.navigateTo(ScreenFlow.Screen(MAIN_MENU_PROJECTS_ITEM))
            },
            { header, value -> @Composable {
                BasicNoEscapeError(header, value)
            }
            }
        )
    }
}