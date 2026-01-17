package com.jonpeps.gamescms.ui.tabletemplates.serialization

import com.jonpeps.gamescms.data.DataConstants.Companion.JSON_EXTENSION
import com.jonpeps.gamescms.data.DataConstants.Companion.SPACE
import com.jonpeps.gamescms.data.DataConstants.Companion.UNDER_SCORE
import com.jonpeps.gamescms.data.dataclasses.ItemType
import com.jonpeps.gamescms.data.helpers.toLocalDate

interface ISerializeTableTemplateHelpers {
    fun getFilename(templateName: String): String
    fun validateTableTemplateValue(value: String, itemType: ItemType): Boolean
}

class SerializeTableTemplateHelpers : ISerializeTableTemplateHelpers {
    override fun getFilename(templateName: String): String {
        return templateName.lowercase().replace(SPACE, UNDER_SCORE) + JSON_EXTENSION
    }

    override fun validateTableTemplateValue(
        value: String,
        itemType: ItemType
    ): Boolean {
        return when (itemType) {
            ItemType.INT -> value.toIntOrNull() != null
            ItemType.BOOLEAN -> STR_BOOLEANS.contains(value.lowercase())
            ItemType.STRING -> true
            ItemType.UTC_DATE -> value.toLocalDate() != null
        }
    }
    companion object {
        val STR_BOOLEANS = arrayOf("true", "false", "1", "0")
    }
}