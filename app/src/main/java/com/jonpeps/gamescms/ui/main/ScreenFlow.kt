package com.jonpeps.gamescms.ui.main

sealed interface ScreenFlow {
    data object Start: ScreenFlow
    data class Screen(val screenName: String): ScreenFlow
    data object Finish: ScreenFlow
}