package com.jonpeps.gamescms.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentContainerView
import com.jonpeps.gamescms.R
import com.jonpeps.gamescms.ui.applevel.DarkColors
import com.jonpeps.gamescms.ui.applevel.LightColors
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragmentActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val useDarkTheme = isSystemInDarkTheme()
            val colors = if (useDarkTheme) {
                DarkColors
            } else {
                LightColors
            }
            Surface {
                MaterialTheme(
                    colorScheme = colors,
                ) {
                    MainContent(colors.scrim)
                }
            }
        }
    }

    @Composable
    private fun MainContent(colour: Color) {
        AndroidView(
            factory = { context ->
                FragmentContainerView(context).apply {
                    id = R.id.main_fragment_activity_container
                }
            }, modifier = Modifier,
            update = {

            }
        )
    }
}