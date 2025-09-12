package com.jonpeps.gamescms.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jonpeps.gamescms.R
import com.jonpeps.gamescms.data.DataConstants
import com.jonpeps.gamescms.data.viewmodels.CommonStringListViewModel
import com.jonpeps.gamescms.data.viewmodels.InputStreamStringListViewModel
import com.jonpeps.gamescms.data.viewmodels.factories.InputStreamStringListViewModelFactory
import com.jonpeps.gamescms.data.viewmodels.factories.ListViewModelFactory

abstract class BaseStrListviewFragment : MainContainerFragment() {
    protected var filePath: String? = null
    protected var cachedName: String? = null
    protected var debugFilename: String? = null
    protected var colourScheme: ColorScheme? = null

    @Composable
    override fun GetContent() {
        val context = requireContext()
        val viewModel = if (DataConstants.Companion.Debug.DEBUG_LOAD) {
            hiltViewModel<InputStreamStringListViewModel,
                    InputStreamStringListViewModelFactory.IInputStreamStringListViewModelFactory>(
                creationCallback = {
                    it.create(
                        filePath?:"",
                        context.assets.open(debugFilename?:""),
                        filePath?:""
                    )
                }
            )
        } else {
            hiltViewModel<CommonStringListViewModel,
                    ListViewModelFactory.ICommonStringListViewModelFactory>(
                creationCallback = {
                    it.create(
                        filePath?:"",
                        cachedName?:""
                    )
                })
        }

        val shouldPostToUI = viewModel.isProcessing.collectAsStateWithLifecycle()
        if (shouldPostToUI.value) {
            if (viewModel.status.success) {
                Column(
                    modifier = Modifier
                        .background(colourScheme?.scrim?: Color.Gray)
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
        }
    }

    abstract fun onClick(text: String)

    abstract fun onError(title: String, message: String? = null)
}