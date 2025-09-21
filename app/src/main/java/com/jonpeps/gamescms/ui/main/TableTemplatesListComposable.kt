package com.jonpeps.gamescms.ui.main

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.jonpeps.gamescms.data.DataConstants.Companion.Debug.Companion.DEBUG_TEMPLATES_LIST
import com.jonpeps.gamescms.data.DataConstants.Companion.MAIN_DIR
import com.jonpeps.gamescms.data.DataConstants.Companion.TEMPLATES_DIR
import com.jonpeps.gamescms.data.DataConstants.Companion.TEMPLATES_LIST_CACHE_NAME

@Composable
fun ShowTableTemplatesList(context: Context,
                           colour: Color,
                           onClick: (String) -> Unit,
                           onError: @Composable (String, String?) -> Unit) {
    val externalStoragePath
            = context.getExternalFilesDir(null)?.absolutePath + MAIN_DIR + TEMPLATES_DIR
    ShowStrList(context,
        externalStoragePath,
        TEMPLATES_LIST_CACHE_NAME,
        DEBUG_TEMPLATES_LIST,
        colour,
        onClick,
        onError)
}