package com.jonpeps.gamescms.ui.main.composables

import androidx.compose.foundation.background
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jonpeps.gamescms.ui.applevel.CustomColours


@Composable
fun CommonEmptyScreen(text: String, customColours: CustomColours, dismissBtnText: String, onDismiss: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxSize()
            .fillMaxHeight()
            .background(customColours.background)
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(25.dp)
            .align(Alignment.Center)) {
            Text(
                text = text,
                fontSize = 30.sp,
                color = customColours.primary,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            )
        }
        Column(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(25.dp)
            .align(Alignment.BottomCenter)) {
            CommonElevatedButton(customColours, dismissBtnText, onDismiss)
        }
    }
}

@Composable
fun CommonStringListView(items: List<String>, customColours: CustomColours, onClick: (text: String) -> Unit = {}) {
    LazyColumn(
            modifier = Modifier
                .padding(10.dp)
                .background(customColours.background)
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
}

@Composable
fun BasicError(customColours: CustomColours, header: String, value: String?, buttonText: String, onDismiss: () -> Unit) {
    val state = rememberScrollState()
    Column(modifier = Modifier
        .background(customColours.background)
        .padding(4.dp)
        .fillMaxHeight()) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp),
            textAlign = TextAlign.Center,
            color = Color.Red,
            text = header,
            fontSize = 20.sp
        )
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(state)
        ) {
            Text(value?:"", modifier = Modifier.fillMaxWidth()
                .wrapContentHeight()
                .padding(5.dp),
                color = Color.Red,
                fontSize = 20.sp)
            Spacer(modifier = Modifier.weight(1.0f))
            CommonElevatedButton(customColours, buttonText, onDismiss)
        }
    }
}

@Composable
fun CommonElevatedButton(customColours: CustomColours, buttonText: String, onDismiss: () -> Unit) {
    ElevatedButton(modifier = Modifier
        .fillMaxWidth()
        .height(60.dp)
        , onClick = { onDismiss() }, colors = ButtonColors(
        customColours.background,
        customColours.primary,
        customColours.background,
        customColours.primary)) {
        Text(text = buttonText, color = customColours.primary,
            fontSize = 35.sp)
    }
}