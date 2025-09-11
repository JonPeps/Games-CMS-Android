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
import com.jonpeps.gamescms.data.viewmodels.BaseStringListViewModel
import com.jonpeps.gamescms.data.viewmodels.CommonStringListViewModel
import com.jonpeps.gamescms.data.viewmodels.InputStreamStringListViewModel
import com.jonpeps.gamescms.data.viewmodels.factories.InputStreamStringListViewModelFactory
import com.jonpeps.gamescms.data.viewmodels.factories.ListViewModelFactory

abstract class BaseStrListviewFragment : MainContainerFragment() {
    private enum class LoadingType {
        DEBUG_LOAD, STORAGE_LOAD
    }

    protected var filePath: String? = null
    protected var cachedName: String? = null
    protected var debugFilename: String? = null
    protected var colourScheme: ColorScheme? = null
    private var loadingType: LoadingType? = null

    @Composable
    override fun GetContent() {
        val context = requireContext()
        var viewModel1: InputStreamStringListViewModel? = null
        var viewModel2: CommonStringListViewModel? = null

        if (DataConstants.Companion.Debug.DEBUG_LOAD) {
            loadingType = LoadingType.DEBUG_LOAD
            viewModel1 = hiltViewModel<InputStreamStringListViewModel,
                    InputStreamStringListViewModelFactory.IInputStreamStringListViewModelFactory>(
                creationCallback = {
                    it.create(
                        filePath?:""
                    )
                }
            )
        } else {
            loadingType = LoadingType.STORAGE_LOAD
            viewModel2 = hiltViewModel<CommonStringListViewModel,
                    ListViewModelFactory.ICommonStringListViewModelFactory>(
                creationCallback = {
                    it.create(
                        filePath?:"",
                        cachedName?:""
                    )
                })
        }
        val baseViewModel: BaseStringListViewModel? = when (loadingType) {
            LoadingType.DEBUG_LOAD -> {
                viewModel1
            }
            LoadingType.STORAGE_LOAD -> {
                viewModel2
            }
            else -> {
                onError(context.getString(R.string.error_unknown))
                return
            }
        }

        val shouldPostToUI = baseViewModel?.isProcessing?.collectAsStateWithLifecycle() ?: false
        if (shouldPostToUI == true) {
            if (baseViewModel?.status?.success == true) {
                Column(
                    modifier = Modifier
                        .background(colourScheme?.scrim?: Color.Gray)
                        .fillMaxHeight()
                ) {
                    CommonStringListView(baseViewModel.status.items, {
                        onClick(it)
                    })
                }
            } else {
                onError(
                    baseViewModel?.status?.message ?: context.getString(R.string.error_unknown),
                    baseViewModel?.status?.ex?.message
                )
            }
        } else {
            CommonLoadingScreen()
        }

        when (loadingType) {
            LoadingType.DEBUG_LOAD -> {
                viewModel1?.loadFromInputStream(cachedName?:"",
                    context.assets.open(debugFilename?:""), filePath?:"")
            }
            LoadingType.STORAGE_LOAD -> {
                viewModel2?.loadFromFile(cachedName?:"")
            }
            else -> {
                onError(context.getString(R.string.error_unknown))
            }
        }
    }

    abstract fun onClick(text: String)

    abstract fun onError(title: String, message: String? = null)
}