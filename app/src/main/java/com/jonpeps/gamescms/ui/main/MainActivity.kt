package com.jonpeps.gamescms.ui.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jonpeps.gamescms.data.viewmodels.CommonStringListViewModel
import com.jonpeps.gamescms.data.viewmodels.factories.ListViewModelFactory
import com.jonpeps.gamescms.ui.applevel.DarkColors
import com.jonpeps.gamescms.ui.applevel.LightColors
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val useDarkTheme = isSystemInDarkTheme()
            val colors = if (useDarkTheme) {
                DarkColors
            } else {
                LightColors
            }
            val viewModel: CommonStringListViewModel =
                hiltViewModel<CommonStringListViewModel,
                        ListViewModelFactory.ICommonStringListViewModelFactory>(
                    creationCallback = { it.create(
                        "",
                        "app/source/main/assets/table_template_list.json") }
                )

            viewModel.loadFromFile("ProjectsList")
            MainContainer({ MainView(viewModel, colourScheme = colors) }, colourScheme = colors)
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
    fun MainView(viewModel: CommonStringListViewModel, colourScheme: ColorScheme) {
        val processingState: Boolean by viewModel.isProcessing.collectAsStateWithLifecycle()
        if (processingState) {
            CommonLoadingScreen()
        } else {
            Column(modifier = Modifier.background(colourScheme.scrim).fillMaxHeight()) {
                CommonStringListView(listOf("item1", "item2", "item3", "item4"))
            }
        }
    }