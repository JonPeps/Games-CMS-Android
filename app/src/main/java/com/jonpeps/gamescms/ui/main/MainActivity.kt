package com.jonpeps.gamescms.ui.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
    fun ElevatedCardExample() {
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            modifier = Modifier.fillMaxWidth().wrapContentHeight()
        ) {
            CreateTableRowTextField("Row name")
        }
    }

    @Composable
    fun CreateTableRowTextField(label: String) {
        val text = remember { mutableStateOf("") }
        TextField(
            value = text.value,
            onValueChange = { text.value = it },
            label = { Text(label) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth().padding(10.dp)
        )
    }

    @Composable
    fun AppTheme(
        useDarkTheme: Boolean = isSystemInDarkTheme()
    ) {
            val colors = if (!useDarkTheme) {
                LightColors
            } else {
                DarkColors
            }

            Surface {
                MaterialTheme(
                    colorScheme = colors,
                ) {
                    Column(
                        modifier = Modifier.wrapContentHeight(),
                    ) {
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(16.dp),
                            content = { ElevatedCardExample() })
                    }
                }
            }
        }
    }


