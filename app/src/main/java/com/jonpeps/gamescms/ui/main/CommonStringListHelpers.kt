package com.jonpeps.gamescms.ui.main

import android.content.res.AssetManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jonpeps.gamescms.data.DataConstants
import com.jonpeps.gamescms.data.viewmodels.BaseStringListViewModel
import com.jonpeps.gamescms.data.viewmodels.CommonStringListViewModel
import com.jonpeps.gamescms.data.viewmodels.InputStreamStringListViewModel
import com.jonpeps.gamescms.data.viewmodels.factories.InputStreamStringListViewModelFactory
import com.jonpeps.gamescms.data.viewmodels.factories.ListViewModelFactory

@Composable
fun getLoadStrListAndGetViewModel(assetManager: AssetManager, path: String, cacheName: String, debugFilename: String): BaseStringListViewModel {
    if (DataConstants.Companion.Debug.DEBUG_LOAD) {
        val viewModel: InputStreamStringListViewModel =
            hiltViewModel<InputStreamStringListViewModel,
                    InputStreamStringListViewModelFactory.IInputStreamStringListViewModelFactory>(
                creationCallback = { it.create(
                    path) }
            )
        viewModel.loadFromInputStream(cacheName,
            assetManager.open(debugFilename), path)
        return viewModel
    } else {
        val viewModel: CommonStringListViewModel =
            hiltViewModel<CommonStringListViewModel,
                    ListViewModelFactory.ICommonStringListViewModelFactory>(
                creationCallback = { it.create(
                    path,
                    cacheName) })
        viewModel.loadFromFile(cacheName)
        return viewModel
    }
}

@Composable
fun MainStringListViewContainer(viewModel: BaseStringListViewModel, onClick: (text: String) -> Unit = {}, colourScheme: ColorScheme) {
    val processingState: Boolean by viewModel.isProcessing.collectAsStateWithLifecycle()
    if (processingState) {
        CommonLoadingScreen()
    } else {
        if (viewModel.status.success) {
            Column(modifier = Modifier
                .background(colourScheme.scrim)
                .fillMaxHeight()) {
                CommonStringListView(viewModel.status.items, onClick)
            }
        } else {
            BasicNoEscapeError("An error occurred!", viewModel.status.ex?.message)
        }
    }
}