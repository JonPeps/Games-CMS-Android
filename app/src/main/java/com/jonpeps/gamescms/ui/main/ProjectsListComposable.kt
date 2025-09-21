package com.jonpeps.gamescms.ui.main

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.jonpeps.gamescms.data.DataConstants.Companion.Debug.Companion.DEBUG_PROJECTS_LIST
import com.jonpeps.gamescms.data.DataConstants.Companion.MAIN_DIR
import com.jonpeps.gamescms.data.DataConstants.Companion.PROJECTS_DIR
import com.jonpeps.gamescms.data.DataConstants.Companion.PROJECT_LIST_CACHE_NAME

@Composable
fun ShowProjectsList(context: Context,
                     colour: Color,
                     onClick: (String) -> Unit,
                     onError: @Composable (String, String?) -> Unit) {
    val externalStoragePath
            = context.getExternalFilesDir(null)?.absolutePath + MAIN_DIR + PROJECTS_DIR
    ShowStrList(context,
        externalStoragePath,
        PROJECT_LIST_CACHE_NAME,
        DEBUG_PROJECTS_LIST,
        colour,
        onClick,
        onError)
}