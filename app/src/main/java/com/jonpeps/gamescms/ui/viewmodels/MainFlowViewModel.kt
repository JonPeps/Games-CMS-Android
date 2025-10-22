package com.jonpeps.gamescms.ui.viewmodels

import com.jonpeps.gamescms.data.DataConstants
import com.jonpeps.gamescms.ui.main.builders.Screen

class MainFlowViewModel: BaseScreenFlowViewModel<Screen>(), IScreenFlowViewModel<Screen>  {
    init {
        backStack.add(Screen(DataConstants.KnownScreens.START))
    }
}