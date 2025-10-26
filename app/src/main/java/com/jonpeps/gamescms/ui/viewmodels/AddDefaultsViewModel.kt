package com.jonpeps.gamescms.ui.viewmodels

import com.jonpeps.gamescms.data.DataConstants.KnownScreens.Companion.ADD_DEFAULTS
import com.jonpeps.gamescms.ui.main.builders.core.Screen

class AddDefaultsViewModel: BaseScreenFlowViewModel<Screen>(), IScreenFlowViewModel<Screen> {
    init {
        backStack.add(Screen(ADD_DEFAULTS))
    }


}