package com.jonpeps.gamescms.ui.main

import android.content.Context
import androidx.compose.runtime.Composable
import com.jonpeps.gamescms.data.DataConstants.Companion.MAIN_DIR
import com.jonpeps.gamescms.data.DataConstants.Companion.TEMPLATES_DIR
import com.jonpeps.gamescms.data.DataConstants.Companion.TEMPLATES_LIST_CACHE_NAME
import com.jonpeps.gamescms.data.DataConstants.Debug.Companion.DEBUG_TEMPLATES_LIST
import com.jonpeps.gamescms.ui.applevel.CustomColours

@Composable
fun ShowTableTemplatesList(context: Context,
                           customColours: CustomColours,
                           onClick: (String) -> Unit,
                           onError: @Composable (String, String?) -> Unit) {
    val externalStoragePath
            = context.getExternalFilesDir(null)?.absolutePath + MAIN_DIR + TEMPLATES_DIR
    ShowStrList(context,
        externalStoragePath,
        TEMPLATES_LIST_CACHE_NAME,
        DEBUG_TEMPLATES_LIST,
        customColours,
        onClick,
        onError)
}