package com.jonpeps.gamescms.ui.main

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.jonpeps.gamescms.R
import com.jonpeps.gamescms.data.DataConstants
import com.jonpeps.gamescms.data.viewmodels.CommonStringListViewModel
import com.jonpeps.gamescms.data.viewmodels.InputStreamStringListViewModel
import com.jonpeps.gamescms.data.viewmodels.factories.InputStreamStringListViewModelFactory
import com.jonpeps.gamescms.data.viewmodels.factories.ListViewModelFactory
import com.jonpeps.gamescms.ui.applevel.CustomColours

class BasicStrListBuilder private constructor() {
    data class Builder(val context: Context, val customColours: CustomColours) {
        private var storagePath: String = ""
        private var cachedName: String = ""
        private var debugFilename: String = ""
        private lateinit var onClick: (String) -> Unit
        private lateinit var onError: @Composable (String, String?) -> Unit

        fun setStoragePath(storagePath: String) = apply { this.storagePath = storagePath }
        fun setCachedName(cachedName: String) = apply { this.cachedName = cachedName }
        fun setDebugFilename(debugFilename: String) = apply { this.debugFilename = debugFilename }
        fun setOnClick(onClick: (String) -> Unit) = apply { this.onClick = onClick }
        fun setOnError(onError: @Composable (String, String?) -> Unit) = apply { this.onError = onError }

        @Composable
        fun Build() {
            ShowStrList(context, storagePath, cachedName, debugFilename, customColours, onClick, onError)
        }
    }

    @Composable
    private fun ShowStrList(context: Context,
                    storagePath: String,
                    cachedName: String,
                    debugFilename: String,
                    customColours: CustomColours,
                    onClick: (String) -> Unit,
                    onError: @Composable (String, String?) -> Unit) {
        val viewModel = if (DataConstants.Debug.DEBUG_LOAD) {
            hiltViewModel<InputStreamStringListViewModel,
                    InputStreamStringListViewModelFactory.IInputStreamStringListViewModelFactory>(
                creationCallback = {
                    it.create(context.assets.open(debugFilename), storagePath)
                }
            )
        } else {
            hiltViewModel<CommonStringListViewModel,
                    ListViewModelFactory.ICommonStringListViewModelFactory>(
                creationCallback = {
                    it.create(storagePath, cachedName)
                })
        }

        val shouldPostToUI = viewModel.hasFinishedObtainingData.collectAsState()
        if (shouldPostToUI.value) {
            if (viewModel.status.success) {
                Column(
                    modifier = Modifier
                        .background(customColours.background)
                        .fillMaxHeight()
                ) {
                    CommonStringListView(viewModel.status.items, customColours, {
                        onClick(it)
                    })
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