package com.jonpeps.gamescms.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import com.jonpeps.gamescms.ui.applevel.CustomColours

@Composable
fun CommonStringListView(items: List<String>, customColours: CustomColours, onClick: (text: String) -> Unit = {}) {
    LazyColumn(
            modifier = Modifier.padding(10.dp).background(customColours.background)
        ) {
            itemsIndexed(items) { _, item ->
                ElevatedCard(
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(75.dp)
                        .background(customColours.background)
                        .clickable { onClick(item) }
                ) {
                    Text(
                        text = item,
                        fontSize = 30.sp,
                        color = customColours.primary,
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
fun CommonLoadingScreen(customColours: CustomColours) {
    val backgroundColor = Color(
        customColours.background.red,
        customColours.background.green,
        customColours.background.blue,
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
            color = customColours.spinner,
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