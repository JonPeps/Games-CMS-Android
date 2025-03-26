package com.jonpeps.gamescms.ui.createtable.viewmodels.data

import android.content.Context
import com.jonpeps.gamescms.R

class CreateTableTemplateErrorStr {
    companion object {
        fun getErrorString(errorType: CreateTableTemplateErrorType, context: Context): String {
            return when (errorType) {
                CreateTableTemplateErrorType.ROW_NAME_EMPTY -> context.getString(R.string.error_row_name_empty)
                CreateTableTemplateErrorType.NO_VALUE_WITH_NOT_EDITABLE -> context.getString(R.string.error_no_value_with_not_editable)
            }
        }
    }
}