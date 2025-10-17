package com.jonpeps.gamescms.ui.viewmodels

import android.os.Bundle
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.jonpeps.gamescms.data.DataConstants
import com.jonpeps.gamescms.ui.main.builders.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface IScreenFlowViewModel {
    fun navigateTo(route: Screen, bundle: Bundle? = null)
    fun popBackStack()
    fun getBundle(screenName: String): Bundle?
}

class ScreenFlowViewModel: ViewModel(), IScreenFlowViewModel  {
    val backStack = mutableStateListOf<Screen>()
    val bundles = mutableMapOf<String, Bundle?>()

    private val _isOnFirstScreen = MutableStateFlow(true)
    val isOnFirstScreen: StateFlow<Boolean> get() = _isOnFirstScreen

    init {
        backStack.add(Screen(DataConstants.KnownScreens.START))
    }

    override fun navigateTo(route: Screen, bundle: Bundle?) {
        backStack.add(route)
        bundles[route.screenName] = bundle
        _isOnFirstScreen.value = false
    }

    override fun popBackStack() {
        backStack.removeLastOrNull() != null
        _isOnFirstScreen.value = backStack.size == 1
    }

    override fun getBundle(screenName: String): Bundle? {
        return bundles[screenName]
    }
}