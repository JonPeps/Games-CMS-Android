package com.jonpeps.gamescms.ui.createtable.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

open class BaseCreateTableTemplateVm<Errors : Any>
@Inject constructor(protected val createTableTemplateBridgeVm: ICreateTableTemplateBridgeVm)
    : ViewModel() {
    protected val mutableErrors = MutableStateFlow(arrayListOf<Errors>())
    val errors: StateFlow<ArrayList<Errors>> = mutableErrors

    protected fun addError(errorType: Errors) {
        if (mutableErrors.value.contains(errorType)) return
        mutableErrors.value.add(errorType)
    }

    protected fun removeError(errorType: Errors) {
        if (mutableErrors.value.contains(errorType)) mutableErrors.value.remove(errorType)
    }
}