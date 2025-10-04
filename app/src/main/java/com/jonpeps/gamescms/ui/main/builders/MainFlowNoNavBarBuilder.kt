package com.jonpeps.gamescms.ui.main.builders

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.jonpeps.gamescms.ui.applevel.CustomColours
import com.jonpeps.gamescms.ui.viewmodels.ScreenFlowViewModel
import com.jonpeps.gamescms.ui.main.builders.data.CustomItemText

class MainFlowNoNavBarBuilder private constructor() {
    data class Builder(val context: Context,
                       val viewModel: ScreenFlowViewModel,
                       val customColours: CustomColours
    ) {
        private var customItemText: CustomItemText? = null

        private lateinit var mainContent: @Composable () -> Unit

        fun setNavBarTitle(customItemText: CustomItemText) = apply {
            this.customItemText = customItemText
        }

        fun setContent(content: @Composable () -> Unit) = apply {
            this.mainContent = content
        }

        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
        fun Build() {
            val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
            Scaffold(
                modifier = Modifier.Companion.nestedScroll(scrollBehavior.nestedScrollConnection),
                topBar = {
                    CenterAlignedTopAppBar(
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = customColours.secondary,
                            titleContentColor = customColours.primary
                        ),
                        title = {
                            customItemText?.let {
                                Text(
                                    text = it.text,
                                    fontSize = it.fontSize,
                                    color = it.color,
                                    fontStyle = it.fontStyle
                                )
                            }
                        },
                        scrollBehavior = scrollBehavior,
                    )
                },
            ) { innerPadding ->
                Box(
                    modifier = Modifier.Companion
                        .padding(innerPadding)
                        .background(customColours.background)
                        .fillMaxWidth()
                        .fillMaxHeight()
                ) {
                    mainContent()
                }
            }
        }
    }
}