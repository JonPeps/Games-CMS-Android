package com.jonpeps.gamescms.ui.main

import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import com.jonpeps.gamescms.data.DataConstants.Companion.MAIN_MENU_PROJECTS_ITEM
import com.jonpeps.gamescms.data.DataConstants.Companion.MAIN_MENU_TEMPLATES_ITEM
import com.jonpeps.gamescms.ui.applevel.DarkColors
import com.jonpeps.gamescms.ui.applevel.LightColors
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragmentActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val useDarkTheme = isSystemInDarkTheme()
            val colors = if (useDarkTheme) {
                DarkColors
            } else {
                LightColors
            }

            MainContainer({
                StartScreenContainer(colourScheme = colors) },
                colourScheme = colors)

            BackHandler {
                finish()
            }
        }

}

    @Composable
    fun StartScreenContainer(colourScheme: ColorScheme) {
        Column(
            modifier = Modifier
                .background(colourScheme.scrim)
                .fillMaxHeight()
        ) {
            CommonStringListView(listOf(MAIN_MENU_PROJECTS_ITEM, MAIN_MENU_TEMPLATES_ITEM), {
                val fragment
                = ProjectsListFragment.newInstance(colourScheme.scrim, baseContext)
                supportFragmentManager.beginTransaction()
                    .add(android.R.id.content, fragment)
                    .commit()
            })
        }
    }

    @Composable
    fun MainContainer(mainView: @Composable () -> Unit, colourScheme: ColorScheme) {
          Surface {
                MaterialTheme(
                    colorScheme = colourScheme,
                ) {
                    mainView()
                }
            }
        }
    }
