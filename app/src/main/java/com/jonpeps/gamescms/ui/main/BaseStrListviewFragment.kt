package com.jonpeps.gamescms.ui.main

import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.jonpeps.gamescms.R
import com.jonpeps.gamescms.data.DataConstants
import com.jonpeps.gamescms.data.DataConstants.Companion.BundleKeys.Companion.CACHED_NAME_KEY
import com.jonpeps.gamescms.data.DataConstants.Companion.BundleKeys.Companion.DEBUG_FILENAME_KEY
import com.jonpeps.gamescms.data.DataConstants.Companion.BundleKeys.Companion.EXT_STORAGE_PATH
import com.jonpeps.gamescms.data.DataConstants.Companion.BundleKeys.Companion.FILE_PATH_KEY
import com.jonpeps.gamescms.data.viewmodels.CommonStringListViewModel
import com.jonpeps.gamescms.data.viewmodels.InputStreamStringListViewModel
import com.jonpeps.gamescms.data.viewmodels.factories.InputStreamStringListViewModelFactory
import com.jonpeps.gamescms.data.viewmodels.factories.ListViewModelFactory

abstract class BaseStrListviewFragment : MainContainerFragment() {
    @Composable
    override fun GetContent() {
        val externalStoragePath = arguments?.getString(EXT_STORAGE_PATH)?:""
        val cachedName = arguments?.getString(CACHED_NAME_KEY)?:""
        val debugFilename = arguments?.getString(DEBUG_FILENAME_KEY)?:""
        val colour = fromColourBundle(arguments?:Bundle())
        val context = requireContext()
        val viewModel = if (DataConstants.Companion.Debug.DEBUG_LOAD) {
            hiltViewModel<InputStreamStringListViewModel,
                    InputStreamStringListViewModelFactory.IInputStreamStringListViewModelFactory>(
                creationCallback = {
                    it.create(context.assets.open(debugFilename), externalStoragePath)
                }
            )
        } else {
            hiltViewModel<CommonStringListViewModel,
                    ListViewModelFactory.ICommonStringListViewModelFactory>(
                creationCallback = {
                    it.create(externalStoragePath, cachedName)
                })
        }

        val shouldPostToUI = viewModel.hasFinishedObtainingData.collectAsState()
        if (shouldPostToUI.value) {
            if (viewModel.status.success) {
                Column(
                    modifier = Modifier
                        .background(colour)
                        .fillMaxHeight()
                ) {
                    CommonStringListView(viewModel.status.items, {
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
            CommonLoadingScreen()
            viewModel.load(cachedName)
        }
    }

    protected abstract fun onClick(text: String)

    protected abstract fun onError(title: String, message: String? = null)
}