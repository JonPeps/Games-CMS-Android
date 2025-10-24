package com.jonpeps.gamescms.ui.viewmodels

import com.jonpeps.gamescms.data.DataConstants
import com.jonpeps.gamescms.ui.main.builders.Screen

class MainFlowViewModel: BaseScreenFlowViewModel<Screen>(), IScreenFlowViewModel<Screen>  {
    override fun reInit() {
        backStack.clear()
        backStack.add(Screen(DataConstants.KnownScreens.START))
    }

    init {
        reInit()
    }
}