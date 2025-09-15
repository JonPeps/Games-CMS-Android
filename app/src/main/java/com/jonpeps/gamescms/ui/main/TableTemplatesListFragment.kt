package com.jonpeps.gamescms.ui.main

import android.content.Context
import android.os.Bundle
import androidx.compose.ui.graphics.Color
import com.jonpeps.gamescms.data.DataConstants.Companion.BundleKeys.Companion.CACHED_NAME_KEY
import com.jonpeps.gamescms.data.DataConstants.Companion.BundleKeys.Companion.DEBUG_FILENAME_KEY
import com.jonpeps.gamescms.data.DataConstants.Companion.BundleKeys.Companion.EXT_STORAGE_PATH
import com.jonpeps.gamescms.data.DataConstants.Companion.Debug.Companion.DEBUG_TEMPLATES_LIST
import com.jonpeps.gamescms.data.DataConstants.Companion.MAIN_DIR
import com.jonpeps.gamescms.data.DataConstants.Companion.TEMPLATES_DIR
import com.jonpeps.gamescms.data.DataConstants.Companion.TEMPLATES_LIST_CACHE_NAME

class TableTemplatesListFragment : BaseStrListviewFragment() {
    override fun onClick(text: String) {

    }

    override fun onError(title: String, message: String?) {

    }

    companion object {
        fun newInstance(colour: Color, context: Context): TableTemplatesListFragment {
            val fragment = TableTemplatesListFragment()
            val args = Bundle()
            colourToBundle(args, colour.red, colour.green, colour.blue)
            val externalStoragePath
                    = context.getExternalFilesDir(null)?.absolutePath + MAIN_DIR + TEMPLATES_DIR
            args.putString(EXT_STORAGE_PATH, externalStoragePath)
            args.putString(CACHED_NAME_KEY, TEMPLATES_LIST_CACHE_NAME)
            args.putString(DEBUG_FILENAME_KEY, DEBUG_TEMPLATES_LIST)
            fragment.arguments = args
            return fragment
        }
    }
}