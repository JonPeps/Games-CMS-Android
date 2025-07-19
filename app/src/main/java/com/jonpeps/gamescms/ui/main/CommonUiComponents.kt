package com.jonpeps.gamescms.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jonpeps.gamescms.ui.applevel.DarkColors
import com.jonpeps.gamescms.ui.applevel.LightColors

@Composable
fun CommonStringListView(items: List<String>, useDarkTheme: Boolean = isSystemInDarkTheme()) {
    val colors = if (useDarkTheme) {
        DarkColors
    } else {
        LightColors
    }
    LazyColumn(
            modifier = Modifier.padding(10.dp).background(colors.scrim)
        ) {
            itemsIndexed(items) { _, item ->
                ElevatedCard(
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(75.dp)
                        .background(colors.scrim)
                ) {
                    Text(
                        text = item,
                        fontSize = 30.sp,
                        color = colors.primary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(10.dp, 20.dp, 10.dp)
                            .fillMaxWidth()
                            .fillMaxHeight()
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }

@Composable
fun CommonLoadingScreen(useDarkTheme: Boolean = isSystemInDarkTheme()) {
    val colors = if (useDarkTheme) {
        DarkColors
    } else {
        LightColors
    }
    val backgroundColor
            = Color(colors.background.red,
        colors.background.green,
        colors.background.blue,
        alpha = 0.75f)
    Box(contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(64.dp),
            color = colors.onBackground,
            strokeWidth = 5.dp
        )
    }
}