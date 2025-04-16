package com.jonpeps.gamescms.ui.createtable.errors

import android.content.Context
import com.jonpeps.gamescms.R

class CreateTableTemplatePageErrorStr {
    companion object {
        fun getErrorString(errorType: CreateTableTemplatePageErrorType, context: Context): String {
            return when (errorType) {
                CreateTableTemplatePageErrorType.ROW_NAME_EXISTS -> context.getString(R.string.error_row_name_exists)
                CreateTableTemplatePageErrorType.NO_PRIMARY_KEY -> context.getString(R.string.error_no_primary_key)
                CreateTableTemplatePageErrorType.NO_SORT_KEY -> context.getString(R.string.error_no_sort_key)
                CreateTableTemplatePageErrorType.ROW_NAME_EMPTY -> context.getString(R.string.error_row_name_empty)
                CreateTableTemplatePageErrorType.NO_VALUE_WITH_NOT_EDITABLE -> context.getString(R.string.error_no_value_with_not_editable)
            }
        }
    }
}