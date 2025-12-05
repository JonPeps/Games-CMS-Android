package com.jonpeps.gamescms.ui.main.builders.core

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jonpeps.gamescms.ui.applevel.CustomColours
import software.amazon.awssdk.core.interceptor.Context

class TableTemplateAssetFilesToStorageBuilder private constructor() {
    data class Builder(val context: Context, val customColours: CustomColours) {
        private var modalMainText: String = ""
        private lateinit var onDismiss: () -> Unit

        fun setModalMainText(text: String) = apply {
            this.modalMainText = text
        }

        fun setOnDismiss(onDismiss: () -> Unit) = apply {
            this.onDismiss = onDismiss
        }

        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
        fun Build() {


            val sheetState = rememberModalBottomSheetState()
            ModalBottomSheet(
                onDismissRequest = {
                    onDismiss()
                },
                sheetState = sheetState
            ) {
                Box(modifier = Modifier.padding(10.dp)
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(customColours.background)) {

                }
            }
        }
    }
}