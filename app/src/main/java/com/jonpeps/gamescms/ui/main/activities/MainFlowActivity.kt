package com.jonpeps.gamescms.ui.main.activities

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.sp
import com.jonpeps.gamescms.R
import com.jonpeps.gamescms.data.DataConstants.Companion.PROJECTS_DIR
import com.jonpeps.gamescms.data.DataConstants.Companion.PROJECT_LIST_CACHE_NAME
import com.jonpeps.gamescms.data.DataConstants.Companion.TEMPLATES_DIR
import com.jonpeps.gamescms.data.DataConstants.Companion.TEMPLATES_LIST_CACHE_NAME
import com.jonpeps.gamescms.data.DataConstants.KnownScreens.Companion.PROJECTS
import com.jonpeps.gamescms.data.DataConstants.KnownScreens.Companion.START
import com.jonpeps.gamescms.data.DataConstants.KnownScreens.Companion.TABLE_TEMPLATES
import com.jonpeps.gamescms.ui.applevel.CustomColours
import com.jonpeps.gamescms.ui.main.builders.DropdownMenuItemBuilder
import com.jonpeps.gamescms.ui.main.composables.CommonStringListView
import com.jonpeps.gamescms.ui.main.builders.Screen
import com.jonpeps.gamescms.ui.main.builders.StartFlowComposeBuilder
import com.jonpeps.gamescms.ui.main.builders.data.CustomItemText
import com.jonpeps.gamescms.ui.viewmodels.ScreenFlowViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFlowActivity : ComponentActivity() {
    private val viewModel: ScreenFlowViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface {
                val context = applicationContext
                val customColours = CustomColours(isSystemInDarkTheme())

                val dropdownMenuItemBuilder = DropdownMenuItemBuilder
                    .Builder()
                    .add(CustomItemText(
                        context.getString(R.string.menu_item_defaults),
                        20.sp,
                        customColours.primary,
                        FontStyle.Normal), enabled = true
                    ) { }
                    .add(CustomItemText(
                        context.getString(R.string.menu_item_debug),
                        20.sp,
                        customColours.primary,
                        FontStyle.Normal), enabled = true
                    ) { }

                    StartFlowComposeBuilder
                    .Builder(context, viewModel, customColours)
                    .addStrListItemFromFile(
                            PROJECTS,
                            listOf(PROJECTS_DIR),
                            PROJECT_LIST_CACHE_NAME,
                            ""
                    )
                    .addStrListItemFromFile(
                        TABLE_TEMPLATES,
                        listOf(TEMPLATES_DIR),
                        TEMPLATES_LIST_CACHE_NAME,
                        ""
                    )
                    .addStrListItem(START, {
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
                    }).addMenuItems {
                        dropdownMenuItemBuilder.Build()
                    }.Build()
            }
        }
    }
}