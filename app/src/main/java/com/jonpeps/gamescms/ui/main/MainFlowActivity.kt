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
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import com.jonpeps.gamescms.R
import com.jonpeps.gamescms.data.DataConstants.Companion.MAIN_MENU_PROJECTS_ITEM
import com.jonpeps.gamescms.data.DataConstants.Companion.MAIN_MENU_TEMPLATES_ITEM
import com.jonpeps.gamescms.ui.applevel.CustomColours
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: ScreenFlowViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Surface {
                val customColours = CustomColours(isSystemInDarkTheme())
                MainContent(viewModel, customColours, {
                    Navigation(viewModel,
                        entryProvider {
                            entry<ScreenFlow.Start> {
                                ShowStartScreen(viewModel, customColours)
                            }
                            entry<ScreenFlow.Screen> {
                                when (it.screenName) {
                                    MAIN_MENU_PROJECTS_ITEM ->
                                        OnProjectsListSelected(customColours)
                                    MAIN_MENU_TEMPLATES_ITEM ->
                                        OnTableTemplatesListSelected(customColours)
                                    else -> {}
                                }
                            }
                            entry<ScreenFlow.Finish> {
                                viewModel.onFinish()
                            }
                        })
                })
            }
        }
    }

    @Composable
    fun ShowStartScreen(viewModel: ScreenFlowViewModel, customColours: CustomColours) {
        Box(modifier = Modifier.fillMaxHeight().fillMaxWidth().background(customColours.background)) {
            CommonStringListView(
                listOf(MAIN_MENU_PROJECTS_ITEM, MAIN_MENU_TEMPLATES_ITEM), { text ->
                    if (text == MAIN_MENU_PROJECTS_ITEM) {
                        viewModel.navigateTo(ScreenFlow.Screen(MAIN_MENU_PROJECTS_ITEM))
                    } else {
                        viewModel.navigateTo(ScreenFlow.Screen(MAIN_MENU_TEMPLATES_ITEM))
                    }
                }, customColours)
        }
    }

    @Composable
    fun Navigation(viewModel: ScreenFlowViewModel,
                   entryProvider: (key: ScreenFlow) -> NavEntry<ScreenFlow>) {
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
            entryProvider = entryProvider,
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

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MainContent(viewModel: ScreenFlowViewModel,
                    customColours: CustomColours,
                    content: @Composable () -> Unit) {
        val expanded = remember { mutableStateOf(false) }
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = customColours.navBar,
                        titleContentColor = customColours.primary
                    ),
                    title = {
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            viewModel.popBackStack()
                        }) {
                            Icon(
                                tint = customColours.primary,
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = applicationContext.getString(R.string.back_arrow_content_desc)
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            expanded.value = !expanded.value
                             }) {
                            Icon(
                                tint = customColours.primary,
                                imageVector = Icons.Filled.MoreVert,
                                contentDescription = applicationContext.getString(R.string.kebab_menu_content_desc)
                            )
                            DropdownMenu(
                                expanded = expanded.value,
                                onDismissRequest = { expanded.value = false }
                            ) {
                                // TODO
                            }
                        }
                    },
                    scrollBehavior = scrollBehavior,
                )
            },
        ) { innerPadding ->
            Box(modifier = Modifier
                .padding(innerPadding)
                .background(customColours.background)
                .fillMaxWidth()
                .fillMaxHeight()) {
                    content()
            }
        }
    }

    @Composable
    fun OnProjectsListSelected(customColours: CustomColours) {
        ShowProjectsList(
            applicationContext,
            customColours,
            { _ ->

            },
            { header, value -> @Composable {
                    BasicNoEscapeError(header, value)
                }
            }
        )
    }

    @Composable
    fun OnTableTemplatesListSelected(customColours: CustomColours) {
        ShowTableTemplatesList(
            applicationContext,
            customColours,
            { _ ->

            },
            { header, value -> @Composable {
                BasicNoEscapeError(header, value)
                }
            }
        )
    }
}