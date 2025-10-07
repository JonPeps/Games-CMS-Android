package com.jonpeps.gamescms.ui.main.builders

import android.content.Context
import android.os.Bundle
import androidx.compose.runtime.Composable
import com.jonpeps.gamescms.data.DataConstants.Companion.MAIN_DIR
import com.jonpeps.gamescms.ui.applevel.CustomColours
import com.jonpeps.gamescms.ui.main.activities.MainActivity
import com.jonpeps.gamescms.ui.main.composables.BasicNoEscapeError
import com.jonpeps.gamescms.ui.viewmodels.ScreenFlowViewModel

class StartFlowComposeBuilder private constructor() {
    data class StrListItemFromFile(val screenName: String,
                                   val folders: List<String>,
                                   val cacheName: String,
                                   val toScreen: String)

    data class StrListItem(val screenName: String, val content: @Composable () -> Unit)

    data class Builder(val context: Context,
                       val viewModel: ScreenFlowViewModel,
                       val customColours: CustomColours) {

        private val strListItemsFromFile: MutableList<StrListItemFromFile> = mutableListOf()
        private val strListItems: MutableList<StrListItem> = mutableListOf()

        fun addStrListItemFromFile(screenName: String,
                                   folders: List<String>,
                                   cacheName: String,
                                   toScreen: String) = apply {
            strListItemsFromFile.add(StrListItemFromFile(screenName, folders, cacheName, toScreen))
        }

        fun addStrListItem(screenName: String, content: @Composable () -> Unit) = apply {
            strListItems.add(StrListItem(screenName, content))
        }

        @Composable
        fun Build() {
            val externalPath = context.getExternalFilesDir(null)
            val screenFlowBuilder = ScreenFlowBuilder.Builder()
            strListItemsFromFile.forEach { item ->
                var path = externalPath?.absolutePath + "/" + MAIN_DIR
                item.folders.forEach {
                    path += "$it/"
                }
                val builder = BasicStrListBuilder.Builder(context, customColours)
                    .setStoragePath(path)
                    .setCachedName(item.cacheName)
                    .setOnClick {
                        viewModel.navigateTo(Screen(item.toScreen),
                            Bundle().apply { putString(BUNDLE_ITEM_CLICKED_ID, it) })
                    }
                    .setOnError { header, value -> @Composable { BasicNoEscapeError(header, value) }
                    }
                screenFlowBuilder.add(item.screenName, { builder.Build() })
            }

            strListItems.forEach {
                screenFlowBuilder.add(it.screenName, it.content)
            }

            NavBuilder.Builder(viewModel)
                .setOnBack {
                    if(!viewModel.popBackStack()) {
                        val mainActivity = context as MainActivity
                        mainActivity.finish()
                    }
                }.navContent(screenFlowBuilder.build())
        }
    }

    companion object {
        const val BUNDLE_ITEM_CLICKED_ID = "bundle_id"
    }
}