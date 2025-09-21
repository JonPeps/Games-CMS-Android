package com.jonpeps.gamescms.ui.main

import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jonpeps.gamescms.data.DataConstants.Companion.BundleKeys.Companion.COLOUR_B
import com.jonpeps.gamescms.data.DataConstants.Companion.BundleKeys.Companion.COLOUR_G
import com.jonpeps.gamescms.data.DataConstants.Companion.BundleKeys.Companion.COLOUR_R
import com.jonpeps.gamescms.ui.applevel.DarkColors
import com.jonpeps.gamescms.ui.applevel.LightColors

@Composable
fun CommonStringListView(items: List<String>, onClick: (text: String) -> Unit = {}, useDarkTheme: Boolean = isSystemInDarkTheme()) {
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
                        .clickable { onClick(item) }
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
                            .clickable { onClick(item) }
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
    val backgroundColor = Color(
        colors.background.red,
        colors.background.green,
        colors.background.blue,
        alpha = 0.75f
    )
    Box(
        contentAlignment = Alignment.Center,
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

    class ErrorStringHolder(val header: String, val value: String, val repeatCount: Int = 350)

    class ErrorClassStringProvider : PreviewParameterProvider<ErrorStringHolder> {
        override val values: Sequence<ErrorStringHolder> = sequenceOf(
            ErrorStringHolder(
                "An error occurred, correct your logic!", "An exception! "
            )
        )
    }
}

@Composable
fun BasicNoEscapeError(header: String, value: String?) {
    val state = rememberScrollState()
    Column(modifier = Modifier.padding(4.dp)
        .fillMaxHeight()
        .border(
            width = 2.dp, color = Color.Gray,
            shape = RoundedCornerShape(10.dp)
        )) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp),
            textAlign = TextAlign.Center,
            color = Color.Red,
            text = header
        )
        value?.let {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .padding(16.dp)
                    .verticalScroll(state)
            ) {
                Text(it, modifier = Modifier.fillMaxWidth()
                    .wrapContentHeight()
                    .padding(5.dp),
                    color = Color.Red)
            }
        }
    }
}

fun colourToBundle(bundle: Bundle, red: Float, green: Float, blue: Float) {
    bundle.putFloat(COLOUR_R, red)
    bundle.putFloat(COLOUR_G, green)
    bundle.putFloat(COLOUR_B, blue)
}

fun fromColourBundle(bundle: Bundle): Color {
    val red = bundle.getFloat(COLOUR_R)
    val green = bundle.getFloat(COLOUR_G)
    val blue = bundle.getFloat(COLOUR_B)
    return Color(red, green, blue)
}