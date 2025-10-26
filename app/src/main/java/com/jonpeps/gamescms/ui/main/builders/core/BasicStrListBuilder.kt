package com.jonpeps.gamescms.ui.main.builders.core

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.jonpeps.gamescms.R
import com.jonpeps.gamescms.data.viewmodels.StartFlowStringListViewModel
import com.jonpeps.gamescms.data.viewmodels.factories.StartFlowStringListViewModelFactory
import com.jonpeps.gamescms.ui.applevel.CustomColours
import com.jonpeps.gamescms.ui.main.composables.CommonLoadingScreen
import com.jonpeps.gamescms.ui.main.composables.CommonStringListView

class BasicStrListBuilder private constructor() {
    data class Builder(val context: Context, val customColours: CustomColours) {
        private var storagePath: String = ""
        private var cachedName: String = ""
        private lateinit var onClick: (String) -> Unit
        private lateinit var onError: @Composable (String, String?) -> Unit

        fun setStoragePath(storagePath: String) = apply { this.storagePath = storagePath }
        fun setCachedName(cachedName: String) = apply { this.cachedName = cachedName }
        fun setOnClick(onClick: (String) -> Unit) = apply { this.onClick = onClick }
        fun setOnError(onError: @Composable (String, String?) -> Unit) = apply { this.onError = onError }

        @Composable
        fun Build() {
            ShowStrList(context, storagePath, cachedName, customColours, onClick, onError)
        }

        @Composable
        private fun ShowStrList(context: Context,
                                storagePath: String,
                                cachedName: String,
                                customColours: CustomColours,
                                onClick: (String) -> Unit,
                                onError: @Composable (String, String?) -> Unit) {
            val viewModel =
                hiltViewModel<StartFlowStringListViewModel,
                        StartFlowStringListViewModelFactory.IStartFlowStringListViewModelFactory>(
                    creationCallback = {
                        it.create(storagePath, cachedName)
                    })

            val shouldPostToUI = viewModel.hasFinishedObtainingData.collectAsState()
            if (shouldPostToUI.value) {
                if (viewModel.status.success) {
                    Column(
                        modifier = Modifier.Companion
                            .background(customColours.background)
                            .fillMaxHeight()
                    ) {
                        CommonStringListView(viewModel.status.items, customColours) {
                            onClick(it)
                        }
                    }
                } else {
                    onError(
                        viewModel.status.message ?: context.getString(R.string.error_unknown),
                        viewModel.status.ex?.message
                    )
                }
            } else {
                CommonLoadingScreen(customColours)
                viewModel.load(cachedName)
            }
        }
    }
}