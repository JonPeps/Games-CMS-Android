package com.jonpeps.gamescms.ui.main

import android.content.Context
import androidx.compose.runtime.Composable
import com.jonpeps.gamescms.data.DataConstants.Companion.MAIN_DIR
import com.jonpeps.gamescms.data.DataConstants.Companion.PROJECTS_DIR
import com.jonpeps.gamescms.data.DataConstants.Companion.PROJECT_LIST_CACHE_NAME
import com.jonpeps.gamescms.data.DataConstants.Debug.Companion.DEBUG_PROJECTS_LIST
import com.jonpeps.gamescms.ui.applevel.CustomColours

@Composable
fun ShowProjectsList(context: Context,
                     customColours: CustomColours,
                     onClick: (String) -> Unit,
                     onError: @Composable (String, String?) -> Unit) {
    val externalStoragePath
            = context.getExternalFilesDir(null)?.absolutePath + MAIN_DIR + PROJECTS_DIR
    ShowStrList(context,
        externalStoragePath,
        PROJECT_LIST_CACHE_NAME,
        DEBUG_PROJECTS_LIST,
        customColours,
        onClick,
        onError)
}

@Composable
fun ProjectsStrList(context: Context, customColours: CustomColours, onItemClicked: (String) -> Unit) {
    ShowProjectsList(
        context,
        customColours,
        {
            onItemClicked(it)
        },
        { header, value -> @Composable {
                BasicNoEscapeError(header, value)
            }
        }
    )
}


@Composable
fun TableTemplatesStrList(context: Context, customColours: CustomColours, onItemClicked: (String) -> Unit) {
    ShowTableTemplatesList(
        context,
        customColours,
        {
            onItemClicked(it)
        },
        { header, value -> @Composable {
                BasicNoEscapeError(header, value)
            }
        }
    )
}