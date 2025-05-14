package com.jonpeps.gamescms.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TableRowTextField(label: String,
                      valueChanged: (String) -> Unit,
                      showError: Boolean = false,
                      errorText: String = "") {
    val text = remember { mutableStateOf("") }
    Column(modifier = Modifier.padding(4.dp)) {
        TextField(
            value = text.value,
            onValueChange = { text.value = it; valueChanged(text.value) },
            label = { Text(label) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
        )
        if (showError) {
            Text(color = Color.Red,
                 text = errorText,
                 modifier = Modifier.padding(bottom = 1.dp, start = 1.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
inline fun <reified T : Enum<T>> DropdownEnumValues(labelText: String, crossinline onDropdownItemChanged: (T) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val items = enumValues<T>()
    var selectedItem by remember { mutableStateOf(items[0]) }

    Column(modifier = Modifier.padding(4.dp)) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                value = selectedItem.name,
                label = { Text(labelText) },
                onValueChange = {
                    onDropdownItemChanged(selectedItem)
                },
                readOnly = true,
                modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable, true).fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                items.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item.name) },
                        onClick = {
                            selectedItem = item
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ElevatedComposable(content: @Composable (ColumnScope.() -> Unit)) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(5.dp)
    ) {
        content()
    }
}