package com.jonpeps.gamescms.ui.createtable

interface IGlobalWatchCoreValuesChangedListener {
    fun onNameChanged(name: String)
    fun isPrimaryChanged(isPrimary: Boolean)
    fun isSortKeyChanged(isSort: Boolean)
}