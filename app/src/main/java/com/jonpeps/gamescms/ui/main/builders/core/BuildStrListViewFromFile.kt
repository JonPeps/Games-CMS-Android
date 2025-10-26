package com.jonpeps.gamescms.ui.main.builders.core

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.compose.runtime.Composable
import com.jonpeps.gamescms.R
import com.jonpeps.gamescms.data.DataConstants.Companion.MAIN_DIR
import com.jonpeps.gamescms.ui.applevel.CustomColours
import com.jonpeps.gamescms.ui.main.activities.MainFlowActivity
import com.jonpeps.gamescms.ui.main.composables.BasicError
import com.jonpeps.gamescms.ui.viewmodels.BaseScreenFlowViewModel

data class StrListItemFromFile(val screenName: String,
                               val folders: List<String>,
                               val cacheName: String,
                               val toScreen: String)

class BuildStrListViewFromFile private constructor() {
    data class Builder(val context: Context,
                       val viewModel: BaseScreenFlowViewModel<Screen>,
                       val screenFlowBuilder: ScreenFlowBuilder.Builder,
                       val customColours: CustomColours) {
        private val strListItemsFromFile: MutableList<StrListItemFromFile> = mutableListOf()

        fun addStrListItem(screenName: String,
                           folders: List<String>,
                           cacheName: String,
                           toScreen: String) = apply {
            strListItemsFromFile.add(StrListItemFromFile(screenName, folders, cacheName, toScreen))
        }

        @Composable
        fun Build() {
            val externalPath = context.getExternalFilesDir(null)
            strListItemsFromFile.forEach { item ->
                var path = externalPath?.absolutePath + "/" + MAIN_DIR
                item.folders.forEach {
                    path += "$it/"
                }
                val builder = BasicStrListBuilder.Builder(context, customColours)
                    .setStoragePath(path)
                    .setCachedName(item.cacheName)
                    .setOnClick {
                        viewModel.navigateTo(Screen(item.toScreen), item.toScreen,
                            Bundle().apply { putString(BUNDLE_ITEM_CLICKED_ID, it) })
                    }
                    .setOnError { header, value -> @Composable {
                            BasicError(customColours, header, value, context.getString(R.string.cta_dismiss)) {
                                (context as MainFlowActivity).finish()
                                context.startActivity(
                                    Intent(
                                        context,
                                        MainFlowActivity::class.java
                                    )
                                )
                            }
                        }
                    }
                screenFlowBuilder.add(item.screenName, { builder.Build() })
            }
        }
    }

    companion object {
        const val BUNDLE_ITEM_CLICKED_ID = "bundle_id_str_item"
    }
}