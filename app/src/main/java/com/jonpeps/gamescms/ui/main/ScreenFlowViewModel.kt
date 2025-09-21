package com.jonpeps.gamescms.ui.main

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class ScreenFlowViewModel : ViewModel()  {
    val backStack = mutableStateListOf<ScreenFlow>()

    init {
        backStack.add(ScreenFlow.Start)
    }

    fun navigateTo(route: ScreenFlow) {
        backStack.add(route)
    }

    fun onFinish() {
        navigateTo(ScreenFlow.Finish)
    }

    fun popBackStack(): Boolean {
        return if (backStack.size > 1) {
            backStack.removeLastOrNull() != null
        } else {
            false
        }
    }
}