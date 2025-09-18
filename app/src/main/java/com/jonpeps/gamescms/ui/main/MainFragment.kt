package com.jonpeps.gamescms.ui.main

import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.jonpeps.gamescms.R
import com.jonpeps.gamescms.data.DataConstants.Companion.MAIN_MENU_PROJECTS_ITEM
import com.jonpeps.gamescms.data.DataConstants.Companion.MAIN_MENU_TEMPLATES_ITEM

class MainFragment : MainContainerFragment() {
    @Composable
    override fun GetContent() {
        val colour = fromColourBundle(arguments?:Bundle())
        Column(
            modifier = Modifier
                .background(colour)
                .fillMaxHeight()
        ) {
            CommonStringListView(listOf(MAIN_MENU_PROJECTS_ITEM, MAIN_MENU_TEMPLATES_ITEM), { text->
                context?.let { context ->
                    activity?.let { activity ->
                        val fragment = if (text == MAIN_MENU_PROJECTS_ITEM) {
                            ProjectsListFragment.newInstance(colour, context)
                        } else {
                            TableTemplatesListFragment.newInstance(colour, context)
                        }
                        activity.supportFragmentManager.beginTransaction()
                            .add(R.id.main_fragment_activity_container, fragment)
                            .commit()
                    }
                }
            })
        }
    }

    companion object {
        fun newInstance(colour: Color): MainFragment {
            val fragment = MainFragment()
            val args = Bundle()
            colourToBundle(args, colour.red, colour.green, colour.blue)
            fragment.arguments = args
            return fragment
        }
    }
}