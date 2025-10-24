package com.jonpeps.gamescms.ui.viewmodels

import android.os.Bundle
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface IScreenFlowViewModel<T> {
    fun navigateTo(route: T, bundleId: String? = "", bundle: Bundle? = null)
    fun getCurrentScreen(): T
    fun popBackStack(): Boolean
    fun getBackstack(): List<T>
    fun getBundle(bundleId: String): Bundle?
    fun isEmpty(): Boolean
}

abstract class BaseScreenFlowViewModel<T>: ViewModel(), IScreenFlowViewModel<T> {
    val backStack = mutableStateListOf<T>()
    val bundles = mutableMapOf<String, Bundle?>()

    private val _isOnFirstScreen = MutableStateFlow(true)
    val isOnFirstScreen: StateFlow<Boolean> get() = _isOnFirstScreen

    override fun navigateTo(route: T, bundleId: String?, bundle: Bundle?) {
        backStack.add(route)
        _isOnFirstScreen.value = false
        bundleId?.let {
            bundles[it] = bundle
        }
    }

    override fun getCurrentScreen(): T = backStack[backStack.size-1]

    override fun popBackStack(): Boolean {
        if (_isOnFirstScreen.value) {
            return false
        }
        backStack.removeLastOrNull()
        _isOnFirstScreen.value = backStack.size == 1
        return backStack.size > 1
    }

    override fun isEmpty(): Boolean = backStack.size == 1

    override fun getBackstack(): List<T> = backStack

    override fun getBundle(bundleId: String): Bundle? {
        return bundles[bundleId]
    }
}