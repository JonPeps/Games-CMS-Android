package com.jonpeps.gamescms.ui.main

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class ScreenFlowViewModel : ViewModel()  {
    private val _screenName = MutableStateFlow<String?>(null)
    val backStack = mutableStateListOf<ScreenFlow>()

    init {
        backStack.add(ScreenFlow.Start)
    }

    fun navigateTo(route: ScreenFlow) {
        backStack.add(route)
    }

    fun onFinish() {
        _screenName.value?.let {
            navigateTo(ScreenFlow.Finish)
        }
    }

    fun popBackStack(): Boolean {
        return if (backStack.size > 1) {
            backStack.removeLastOrNull() != null
        } else {
            false
        }
    }
}