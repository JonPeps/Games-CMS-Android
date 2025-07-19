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
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.jonpeps.gamescms.data.viewmodels.CommonStringListViewModel
import com.jonpeps.gamescms.data.viewmodels.ICommonStringListViewModel
import com.jonpeps.gamescms.data.viewmodels.factories.ListViewModelFactory
import com.jonpeps.gamescms.ui.applevel.DarkColors
import com.jonpeps.gamescms.ui.applevel.LightColors
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme(
                false,
        )
    }
}

    @Composable
    fun AppTheme(
        useDarkTheme: Boolean = isSystemInDarkTheme()
    ) {
            val viewModel: ICommonStringListViewModel =
                hiltViewModel<CommonStringListViewModel,
                        ListViewModelFactory.ICommonStringListViewModelFactory>(
                    creationCallback = { it.create("", "") }
                )

            val colors = if (useDarkTheme) {
                DarkColors
            } else {
                LightColors
            }

            Surface {
                MaterialTheme(
                    colorScheme = colors,
                ) {
                    MainView(viewModel, colourScheme = colors)
                }
            }
        }
    }

    @Composable
    fun MainView(viewModel: ICommonStringListViewModel,
                 colourScheme: ColorScheme) {
        Column(modifier = Modifier.background(colourScheme.scrim).fillMaxHeight()) {
            CommonStringListView(listOf("item1", "item2", "item3", "item4"))
        }
    }