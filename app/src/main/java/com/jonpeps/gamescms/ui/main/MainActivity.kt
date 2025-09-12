package com.jonpeps.gamescms.ui.main

import android.content.Context
import android.content.res.AssetManager
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
import androidx.fragment.app.commit
import com.jonpeps.gamescms.data.DataConstants.Companion.Debug.Companion.DEBUG_PROJECTS_LIST
import com.jonpeps.gamescms.data.DataConstants.Companion.Debug.Companion.DEBUG_TEMPLATES_LIST
import com.jonpeps.gamescms.data.DataConstants.Companion.MAIN_MENU_PROJECTS_ITEM
import com.jonpeps.gamescms.data.DataConstants.Companion.MAIN_MENU_TEMPLATES_ITEM
import com.jonpeps.gamescms.data.DataConstants.Companion.PROJECTS_DIR
import com.jonpeps.gamescms.data.DataConstants.Companion.PROJECT_LIST_CACHE_NAME
import com.jonpeps.gamescms.data.DataConstants.Companion.TEMPLATES_DIR
import com.jonpeps.gamescms.data.DataConstants.Companion.TEMPLATES_LIST_CACHE_NAME
import com.jonpeps.gamescms.ui.applevel.DarkColors
import com.jonpeps.gamescms.ui.applevel.LightColors
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
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
                val fragment = when (it) {
                    MAIN_MENU_PROJECTS_ITEM -> {
                        ProjectsFragment()
                    } else -> {
                        TemplatesFragment()
                    }
                }
                supportFragmentManager.commit {

                }
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

@Composable
fun OnMainScreenItemClick(text: String, context: Context, assets: AssetManager) {
    val viewModel = when (text) {
        MAIN_MENU_PROJECTS_ITEM -> {
            getViewModel(context, assets, PROJECTS_DIR, PROJECT_LIST_CACHE_NAME, DEBUG_PROJECTS_LIST)
        } else -> {
            getViewModel(context, assets, TEMPLATES_DIR, TEMPLATES_LIST_CACHE_NAME, DEBUG_TEMPLATES_LIST)
        }
    }
}

