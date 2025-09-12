package com.jonpeps.gamescms.data.viewmodels

import androidx.lifecycle.ViewModel
import com.jonpeps.gamescms.data.serialization.StringListStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface IBaseStringListViewModel {
    fun load(cacheName: String, loadFromCacheIfExists: Boolean = true)
}

abstract class BaseStringListViewModel: ViewModel(), IBaseStringListViewModel {
    var status: StringListStatus = StringListStatus(true, arrayListOf(), "", null)

    protected var baseIsProcessing = MutableStateFlow(false)
    val isProcessing: StateFlow<Boolean> = baseIsProcessing

    protected var items = arrayListOf<String>()
    protected var exception: Exception? = null
}