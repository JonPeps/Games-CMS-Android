package com.jonpeps.gamescms.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import com.jonpeps.gamescms.R
import com.jonpeps.gamescms.data.DataConstants
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

                    val entryProvider = ScreenFlowBuilder.Builder()
                        .add(Screen(DataConstants.KnownScreens.START),
                            {
                                ShowStartScreen(viewModel, customColours)
                            }
                        ).add(Screen(DataConstants.KnownScreens.PROJECTS),
                            {
                                OnProjectsListSelected(applicationContext, customColours) {
                                    // TODO
                                }
                            }
                        ).add(Screen(DataConstants.KnownScreens.TABLE_TEMPLATES),
                            {
                                OnTableTemplatesListSelected(applicationContext, customColours) {
                                    // TODO
                                    }
                                }
                        ).build()

                    Navigation(viewModel,
                        entryProvider = entryProvider
                    ) {
                        if (!viewModel.popBackStack()) {
                            finish()
                        }
                    }
                })
            }
        }
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
}