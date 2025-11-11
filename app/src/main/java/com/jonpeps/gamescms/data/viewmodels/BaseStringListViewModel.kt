package com.jonpeps.gamescms.data.viewmodels

import androidx.lifecycle.ViewModel
import com.jonpeps.gamescms.data.serialization.StringListStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseStringListViewModel: ViewModel(), IBasicStringListViewModel {
    var status: StringListStatus = StringListStatus(true, arrayListOf(), "", null)

    protected var _isProcessing = MutableStateFlow(true)
    val isProcessing: StateFlow<Boolean> = _isProcessing

    protected var items = arrayListOf<String>()
    protected var exception: Exception? = null
}