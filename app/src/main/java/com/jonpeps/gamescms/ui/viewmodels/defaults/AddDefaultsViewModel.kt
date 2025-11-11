package com.jonpeps.gamescms.ui.viewmodels.defaults

import com.jonpeps.gamescms.data.DataConstants.KnownScreens.Companion.ADD_DEFAULTS
import com.jonpeps.gamescms.ui.main.builders.core.Screen
import com.jonpeps.gamescms.ui.viewmodels.BaseScreenFlowViewModel
import com.jonpeps.gamescms.ui.viewmodels.IScreenFlowViewModel

class AddDefaultsViewModel: BaseScreenFlowViewModel<Screen>(), IScreenFlowViewModel<Screen> {
    init {
        backStack.add(Screen(ADD_DEFAULTS))
    }


}