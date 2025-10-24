package com.jonpeps.gamescms.ui.main.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import com.jonpeps.gamescms.data.DataConstants
import com.jonpeps.gamescms.data.DataConstants.KnownScreens.Companion.ADD_DEFAULTS
import com.jonpeps.gamescms.ui.applevel.CustomColours
import com.jonpeps.gamescms.ui.main.builders.core.BasicFlowComposeBuilder
import com.jonpeps.gamescms.ui.main.composables.CommonStringListView
import com.jonpeps.gamescms.ui.viewmodels.AddDefaultsViewModel

class AddDefaultsActivity: ComponentActivity() {
    private val viewModel: AddDefaultsViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface {
                val context = applicationContext
                val customColours = CustomColours(isSystemInDarkTheme())

                val basicFlowComposeBuilder = BasicFlowComposeBuilder
                    .Builder(context, viewModel, customColours)
                basicFlowComposeBuilder.onBack {
                    finish()
                    val intent = Intent(this, MainFlowActivity::class.java)
                    startActivity(intent)
                    }
                    .showBackIcon(true)
                    .addScreenItem(ADD_DEFAULTS,
                        {
                            CommonStringListView(
                                listOf(
                                    DataConstants.KnownScreens.Companion.INSERT_DEFAULT_PROJECTS,
                                    DataConstants.KnownScreens.Companion.INSERT_DEFAULT_TEMPLATES
                                ), customColours
                            ) { text ->

                            }
                        }
                    )
                basicFlowComposeBuilder.Build()
            }
        }
    }


}