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
    fun onFinish()
    fun popBackStack(): Boolean
    fun getBundle(screenName: String): Bundle?
}

class ScreenFlowViewModel: ViewModel(), IScreenFlowViewModel  {
    val backStack = mutableStateListOf<Screen>()
    val bundles = mutableMapOf<String, Bundle?>()
    val _showDropdownMenu = MutableStateFlow(false)
    val ShowDropdownMenu: StateFlow<Boolean> = _showDropdownMenu

    init {
        backStack.add(Screen(DataConstants.KnownScreens.START))
    }

    override fun navigateTo(route: Screen, bundle: Bundle?) {
        backStack.add(route)
        bundles[route.screenName] = bundle
    }

    override fun onFinish() {
        navigateTo(Screen(DataConstants.KnownScreens.FINISH))
    }

    override fun popBackStack(): Boolean {
        return if (backStack.size > 1) {
            backStack.removeLastOrNull() != null
        } else {
            false
        }
    }

    override fun getBundle(screenName: String): Bundle? {
        return bundles[screenName]
    }
}