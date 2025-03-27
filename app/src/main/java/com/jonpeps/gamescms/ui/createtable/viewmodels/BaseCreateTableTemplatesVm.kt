package com.jonpeps.gamescms.ui.createtable.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface IBaseCreateTableTemplatesVm<Errors : Any> {
    fun getErrors(): ArrayList<Errors>
}

open class BaseCreateTableTemplatesVm<Errors : Any> : ViewModel(), IBaseCreateTableTemplatesVm<Errors> {
    private val _errorsListChanged = MutableStateFlow(false)
    protected val errorsListChanged: StateFlow<Boolean> = _errorsListChanged
    private val errors = arrayListOf<Errors>()

    protected fun addError(error: Errors) {
        if (errors.contains(error)) return
        errors.add(error)
        _errorsListChanged.value = true
    }

    protected fun removeError(error: Errors) {
        if (errors.contains(error)) errors.remove(error)
        _errorsListChanged.value = true
    }

    override fun getErrors(): ArrayList<Errors> = errors
}