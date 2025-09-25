package com.jonpeps.gamescms.ui.main

import android.os.Bundle
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.jonpeps.gamescms.data.DataConstants

interface IScreenFlowViewModel {
    fun navigateTo(route: Screen, bundle: Bundle? = null)
    fun onFinish()
    fun popBackStack(): Boolean
    fun getBundle(screenName: String): Bundle?
}

class ScreenFlowViewModel: ViewModel(), IScreenFlowViewModel  {
    val backStack = mutableStateListOf<Screen>()
    val bundles = mutableMapOf<String, Bundle?>()

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