package com.jonpeps.gamescms.ui.createtable.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface IBaseCreateTableTemplatesVm<Errors : Any> {
    fun getErrors(): ArrayList<Errors>
}

open class BaseCreateTableTemplatesVm<Errors : Any> : ViewModel(), IBaseCreateTableTemplatesVm<Errors> {
    protected val hasErrors = MutableStateFlow(false)
    protected val hasGotErrors: StateFlow<Boolean> = hasErrors
    private val errors = arrayListOf<Errors>()

    protected fun addError(error: Errors) {
        if (errors.contains(error)) return
        errors.add(error)
        hasErrors.value = true
    }

    protected fun removeError(error: Errors) {
        if (errors.contains(error)) errors.remove(error)
        hasErrors.value = errors.isNotEmpty()
    }

    override fun getErrors(): ArrayList<Errors> = errors
}