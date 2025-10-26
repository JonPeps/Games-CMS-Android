package com.jonpeps.gamescms.data.viewmodels

import androidx.lifecycle.ViewModel
import com.jonpeps.gamescms.data.serialization.StringListStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseStringListViewModel: ViewModel(), IBasicStringListViewModel {
    var status: StringListStatus = StringListStatus(true, arrayListOf(), "", null)

    protected var baseHasFinishedObtainingData = MutableStateFlow(false)
    val hasFinishedObtainingData: StateFlow<Boolean> = baseHasFinishedObtainingData

    protected var items = arrayListOf<String>()
    protected var exception: Exception? = null
}