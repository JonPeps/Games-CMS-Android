package com.jonpeps.gamescms.ui.main.builders

import android.content.Context
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import com.jonpeps.gamescms.ui.viewmodels.IScreenFlowViewModel

class NavBuilder private constructor() {
    data class Builder<K: Any>(val context: Context, val viewModel: IScreenFlowViewModel<K>) {
        private lateinit var entryProvider: (key: K) -> NavEntry<K>
        private lateinit var onEndOfBackstack: () -> Unit

        fun setEndOfBackstack(onEnd: () -> Unit) = apply {
            this.onEndOfBackstack = onEnd
        }

        fun navContent(entryProvider: (key: K) -> NavEntry<K>) = apply {
            this.entryProvider = entryProvider
        }

        @Composable
        fun Build() {
            NavDisplay(
                backStack = viewModel.getBackstack(),
                onBack = {
                    viewModel.popBackStack()
                         },
                entryDecorators = listOf(
                    rememberSceneSetupNavEntryDecorator(),
                    rememberSavedStateNavEntryDecorator(),
                    rememberViewModelStoreNavEntryDecorator()
                ),
                entryProvider = entryProvider,
                transitionSpec = {
                    slideInHorizontally(initialOffsetX = { it }) togetherWith
                            slideOutHorizontally(targetOffsetX = { -it })
                },
                popTransitionSpec = {
                    slideInHorizontally(initialOffsetX = { -it }) togetherWith
                            slideOutHorizontally(targetOffsetX = { it })
                },
                predictivePopTransitionSpec = {
                    slideInHorizontally(initialOffsetX = { -it }) togetherWith
                            slideOutHorizontally(targetOffsetX = { it })
                }
            )
        }
    }
}