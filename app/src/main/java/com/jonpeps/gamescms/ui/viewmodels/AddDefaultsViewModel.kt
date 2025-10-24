package com.jonpeps.gamescms.ui.viewmodels

import com.jonpeps.gamescms.data.DataConstants.KnownScreens.Companion.ADD_DEFAULTS
import com.jonpeps.gamescms.ui.main.builders.Screen

class AddDefaultsViewModel: BaseScreenFlowViewModel<Screen>(), IScreenFlowViewModel<Screen> {
    override fun reInit() {
        backStack.clear()
        backStack.add(Screen(ADD_DEFAULTS))
    }

    init {
        reInit()
    }
}