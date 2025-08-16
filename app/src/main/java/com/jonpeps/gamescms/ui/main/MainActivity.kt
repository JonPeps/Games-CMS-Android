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
import com.jonpeps.gamescms.data.DataConstants.Companion.PROJECT_DIR
import com.jonpeps.gamescms.data.DataConstants.Companion.PROJECT_LIST_CACHE_NAME
import com.jonpeps.gamescms.data.viewmodels.InputStreamStringListViewModel
import com.jonpeps.gamescms.data.viewmodels.factories.InputStreamStringListViewModelFactory
import com.jonpeps.gamescms.ui.applevel.DarkColors
import com.jonpeps.gamescms.ui.applevel.LightColors
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.IOException

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
            val path = baseContext.filesDir.path + PROJECT_DIR
            val directory = File(path)
            if (!directory.exists()) {
                val result = directory.mkdir()
                if (!result) {
                    // TODO: Handle error
                }
            }
            val viewModel: InputStreamStringListViewModel =
                hiltViewModel<InputStreamStringListViewModel,
                        InputStreamStringListViewModelFactory.IInputStreamStringListViewModelFactory>(
                    creationCallback = { it.create(
                        path) }
                )
            try {
                viewModel.loadFromInputStream(PROJECT_LIST_CACHE_NAME, assets.open("dummy_projects_list.json"))
            } catch (ex: IOException) {
                // TODO: Handle error
            }
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
    fun MainView(viewModel: InputStreamStringListViewModel, colourScheme: ColorScheme) {
        val processingState: Boolean by viewModel.isProcessing.collectAsStateWithLifecycle()
        if (processingState) {
            CommonLoadingScreen()
        } else {
            Column(modifier = Modifier
                .background(colourScheme.scrim)
                .fillMaxHeight()) {
                CommonStringListView(viewModel.status.items)
            }
        }
    }