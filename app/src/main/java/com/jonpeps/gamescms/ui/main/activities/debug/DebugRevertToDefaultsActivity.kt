package com.jonpeps.gamescms.ui.main.activities.debug

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.jonpeps.gamescms.data.DataConstants.KnownScreens.Companion.INSERT_DEFAULT_TEMPLATES
import com.jonpeps.gamescms.data.DataConstants.KnownScreens.Companion.INSERT_DUMMY_PROJECTS
import com.jonpeps.gamescms.ui.applevel.CustomColours
import com.jonpeps.gamescms.ui.main.composables.CommonStringListView

class DebugRevertToDefaultsActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface {
                val context = applicationContext
                val customColours = CustomColours(isSystemInDarkTheme())
                Box(modifier = Modifier.fillMaxHeight().fillMaxWidth().background(customColours.background)) {
                    CommonStringListView(
                        listOf(INSERT_DUMMY_PROJECTS, INSERT_DEFAULT_TEMPLATES), customColours
                    ) { text ->

                    }
                }
            }
        }
    }
}