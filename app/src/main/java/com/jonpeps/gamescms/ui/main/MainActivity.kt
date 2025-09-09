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
import com.jonpeps.gamescms.data.DataConstants
import com.jonpeps.gamescms.data.DataConstants.Companion.PROJECTS_DIR
import com.jonpeps.gamescms.data.DataConstants.Companion.PROJECT_DIR
import com.jonpeps.gamescms.data.DataConstants.Companion.PROJECT_LIST_CACHE_NAME
import com.jonpeps.gamescms.data.viewmodels.BaseStringListViewModel
import com.jonpeps.gamescms.data.viewmodels.CommonStringListViewModel
import com.jonpeps.gamescms.data.viewmodels.InputStreamStringListViewModel
import com.jonpeps.gamescms.data.viewmodels.factories.InputStreamStringListViewModelFactory
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

            val path = baseContext.filesDir.path + PROJECT_DIR + PROJECTS_DIR
            val baseStringListViewModel: BaseStringListViewModel = if (DataConstants.Companion.Debug.DEBUG_LOAD) {
                val viewModel: InputStreamStringListViewModel =
                    hiltViewModel<InputStreamStringListViewModel,
                            InputStreamStringListViewModelFactory.IInputStreamStringListViewModelFactory>(
                        creationCallback = { it.create(
                            path) }
                    )
                viewModel.loadFromInputStream(PROJECT_LIST_CACHE_NAME,
                    assets.open(DataConstants.Companion.Debug.DEBUG_PROJECTS_LIST), path)
                viewModel
            } else {
                val viewModel: CommonStringListViewModel =
                    hiltViewModel<CommonStringListViewModel,
                            ListViewModelFactory.ICommonStringListViewModelFactory>(
                        creationCallback = { it.create(
                            path,
                            PROJECT_LIST_CACHE_NAME) })
                viewModel.loadFromFile(PROJECT_LIST_CACHE_NAME)
                viewModel
            }

            MainContainer({ MainView(baseStringListViewModel, colourScheme = colors) }, colourScheme = colors)
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
    fun MainView(viewModel: BaseStringListViewModel, colourScheme: ColorScheme) {
        val processingState: Boolean by viewModel.isProcessing.collectAsStateWithLifecycle()
        if (processingState) {
            CommonLoadingScreen()
        } else {
            if (viewModel.status.success) {
                Column(modifier = Modifier
                    .background(colourScheme.scrim)
                    .fillMaxHeight()) {
                    CommonStringListView(viewModel.status.items)
                }
            } else {
                BasicNoEscapeError("An error occurred!", viewModel.status.ex?.message)
            }
        }
    }

