package com.jonpeps.gamescms.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jonpeps.gamescms.data.DataConstants
import com.jonpeps.gamescms.ui.applevel.CustomColours

@Composable
fun ShowStartScreen(viewModel: ScreenFlowViewModel, customColours: CustomColours) {
    Box(modifier = Modifier.fillMaxHeight().fillMaxWidth().background(customColours.background)) {
        CommonStringListView(
            listOf(DataConstants.KnownScreens.PROJECTS, DataConstants.KnownScreens.TABLE_TEMPLATES), customColours
        ) { text ->
            if (text == DataConstants.KnownScreens.PROJECTS) {
                viewModel.navigateTo(Screen(DataConstants.KnownScreens.PROJECTS))
            } else {
                viewModel.navigateTo(Screen(DataConstants.KnownScreens.TABLE_TEMPLATES))
            }
        }
    }
}