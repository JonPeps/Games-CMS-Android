package com.jonpeps.gamescms.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.sp
import com.jonpeps.gamescms.data.DataConstants.KnownScreens.Companion.PROJECTS
import com.jonpeps.gamescms.data.DataConstants.KnownScreens.Companion.START
import com.jonpeps.gamescms.data.DataConstants.KnownScreens.Companion.TABLE_TEMPLATES
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
                val navScreenEntries = ScreenFlowBuilder.Builder()
                .add(START, {
                ShowStartScreen(viewModel, customColours)
                })
                .add(PROJECTS, {
                    ProjectsStrList(applicationContext, customColours) {
                        // TODO
                    }})
                .add(TABLE_TEMPLATES, {
                    TableTemplatesStrList(applicationContext, customColours) {
                        // TODO
                    }}
                ).build()

                val navBuilder = NavBuilder.Builder(viewModel)
                navBuilder.setOnBack { if(!viewModel.popBackStack()) {
                    finish()
                } }.navContent {
                    navScreenEntries(it)
                }

                val menuItems = DropdownMenuItemBuilder.Builder()
                    .add(CustomItemText("Item 1", 20.sp, customColours.primary, FontStyle.Normal),
                        true,
                        onClick = {
                        }
                    )
                MainFlowWithNavBarBuilder
                    .Builder(applicationContext, viewModel, customColours)
                    .setNavBarTitle(CustomItemText("Main Screen", 20.sp, customColours.primary, FontStyle.Normal))
                    .onIconBack {
                        viewModel.popBackStack()
                    }.menuItems {
                        menuItems.Build()
                    }.setContent {
                        navBuilder.Build()
                    }.Build()
            }
        }
    }
}