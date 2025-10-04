package com.jonpeps.gamescms.ui.main.activities

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavEntry
import com.jonpeps.gamescms.data.DataConstants.Companion.MAIN_DIR
import com.jonpeps.gamescms.data.DataConstants.Companion.PROJECTS_DIR
import com.jonpeps.gamescms.data.DataConstants.Companion.PROJECT_LIST_CACHE_NAME
import com.jonpeps.gamescms.data.DataConstants.Companion.TEMPLATES_DIR
import com.jonpeps.gamescms.data.DataConstants.Companion.TEMPLATES_LIST_CACHE_NAME
import com.jonpeps.gamescms.data.DataConstants.Debug.Companion.DEBUG_PROJECTS_LIST
import com.jonpeps.gamescms.data.DataConstants.Debug.Companion.DEBUG_TEMPLATES_LIST
import com.jonpeps.gamescms.data.DataConstants.KnownScreens.Companion.PROJECTS
import com.jonpeps.gamescms.data.DataConstants.KnownScreens.Companion.START
import com.jonpeps.gamescms.data.DataConstants.KnownScreens.Companion.TABLE_TEMPLATES
import com.jonpeps.gamescms.ui.applevel.CustomColours
import com.jonpeps.gamescms.ui.main.composables.BasicNoEscapeError
import com.jonpeps.gamescms.ui.main.composables.CommonStringListView
import com.jonpeps.gamescms.ui.main.builders.BasicStrListBuilder
import com.jonpeps.gamescms.ui.main.builders.DropdownMenuItemBuilder
import com.jonpeps.gamescms.ui.main.builders.MainFlowWithNavBarBuilder
import com.jonpeps.gamescms.ui.main.builders.NavBuilder
import com.jonpeps.gamescms.ui.main.builders.Screen
import com.jonpeps.gamescms.ui.main.builders.ScreenFlowBuilder
import com.jonpeps.gamescms.ui.main.builders.data.CustomItemText
import com.jonpeps.gamescms.ui.viewmodels.ScreenFlowViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: ScreenFlowViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface {
                BuildMainContent(CustomColours(isSystemInDarkTheme()))
            }
        }
    }

    @Composable
    private fun BuildProjectsList(context: Context, customColours: CustomColours) {
        return BasicStrListBuilder
            .Builder(context, customColours)
            .setStoragePath(context.getExternalFilesDir(null)?.absolutePath + MAIN_DIR + PROJECTS_DIR)
            .setCachedName(PROJECT_LIST_CACHE_NAME)
            .setDebugFilename(DEBUG_PROJECTS_LIST)
            .setOnClick {
                // TODO
            }
            .setOnError { header, value -> @Composable { BasicNoEscapeError(header, value) }
            }.Build()
    }

    @Composable
    private fun BuildTemplatesList(context: Context, customColours: CustomColours) {
        return BasicStrListBuilder
            .Builder(context, customColours)
            .setStoragePath(context.getExternalFilesDir(null)?.absolutePath + MAIN_DIR + TEMPLATES_DIR)
            .setCachedName(TEMPLATES_LIST_CACHE_NAME)
            .setDebugFilename(DEBUG_TEMPLATES_LIST)
            .setOnClick {
                // TODO
            }
            .setOnError { header, value -> @Composable { BasicNoEscapeError(header, value) }
            }.Build()
    }

    @Composable
    private fun buildNavScreenEntries(customColours: CustomColours): (Screen) -> NavEntry<Screen> {
        return ScreenFlowBuilder
            .Builder()
            .add(START, { ShowStartScreen(viewModel, customColours) })
            .add(PROJECTS, { BuildProjectsList(applicationContext, customColours)} )
            .add(TABLE_TEMPLATES, { BuildTemplatesList(applicationContext, customColours) })
            .build()
    }

    @Composable
    private fun buildMainNavigation(navScreenEntries: (Screen) -> NavEntry<Screen>): NavBuilder.Builder {
        return NavBuilder.Builder(viewModel)
            .setOnBack {
            if(!viewModel.popBackStack()) {
                finish() }
            }.navContent {
                navScreenEntries(it)
            }
    }

    @Composable
    private fun BuildMainContent(customColours: CustomColours) {
        val menuItems = DropdownMenuItemBuilder.Builder()
            .add(
                CustomItemText(
                    "Item 1",
                    20.sp, customColours.primary, FontStyle.Normal
                ),
                true,
                onClick = {
                }
            )
        MainFlowWithNavBarBuilder
            .Builder(applicationContext, viewModel, customColours)
            .setNavBarTitle(
                CustomItemText(
                    "Main Screen",
                    20.sp, customColours.primary, FontStyle.Normal
                )
            )
            .onIconBack {
                viewModel.popBackStack()
            }.menuItems {
                menuItems.Build()
            }.setContent {
                buildMainNavigation(buildNavScreenEntries(customColours)).Build()
            }.Build()
    }

    @Composable
    private fun ShowStartScreen(viewModel: ScreenFlowViewModel, customColours: CustomColours) {
        Box(modifier = Modifier.fillMaxHeight().fillMaxWidth().background(customColours.background)) {
            CommonStringListView(
                listOf(PROJECTS, TABLE_TEMPLATES), customColours
            ) { text ->
                if (text == PROJECTS) {
                    viewModel.navigateTo(Screen(PROJECTS))
                } else {
                    viewModel.navigateTo(Screen(TABLE_TEMPLATES))
                }
            }
        }
    }
}

