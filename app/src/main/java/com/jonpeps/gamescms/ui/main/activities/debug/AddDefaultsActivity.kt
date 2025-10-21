package com.jonpeps.gamescms.ui.main.activities.debug

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import com.jonpeps.gamescms.data.DataConstants.KnownScreens.Companion.INSERT_DEFAULT_PROJECTS
import com.jonpeps.gamescms.data.DataConstants.KnownScreens.Companion.INSERT_DEFAULT_TEMPLATES
import com.jonpeps.gamescms.ui.applevel.CustomColours
import com.jonpeps.gamescms.ui.main.activities.MainFlowActivity
import com.jonpeps.gamescms.ui.main.builders.NavBarBuilder
import com.jonpeps.gamescms.ui.main.composables.CommonStringListView

class AddDefaultsActivity: ComponentActivity() {


    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface {
                val context = applicationContext
                val customColours = CustomColours(isSystemInDarkTheme())

                NavBarBuilder.Builder(context, customColours)
                    .showBackIcon(true)
                    .onIconBack {
                        finish()
                        val intent = Intent(this, MainFlowActivity::class.java)
                        startActivity(intent)
                    }
                    .setContent {
                    CommonStringListView(
                    listOf(INSERT_DEFAULT_PROJECTS, INSERT_DEFAULT_TEMPLATES), customColours
                    ) { text ->

                    }
                }.Build()
            }
        }
    }


}