package com.jonpeps.gamescms.ui.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
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
            val colors = if (useDarkTheme) {
                DarkColors
            } else {
                LightColors
            }

            Surface {
                MaterialTheme(
                    colorScheme = colors,
                ) {

                }
            }
        }
    }


