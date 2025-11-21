package com.jonpeps.gamescms.ui.tabletemplates.serialization

import com.jonpeps.gamescms.data.DataConstants.Companion.JSON_EXTENSION

interface ISerializeTableTemplateHelpers {
    fun getFilename(templateName: String): String
}

class SerializeTableTemplateHelpers : ISerializeTableTemplateHelpers {
    override fun getFilename(templateName: String): String {
        return templateName.lowercase().replace(WHITE_SPACE, UNDERSCORE) + JSON_EXTENSION
    }

    companion object {
        const val WHITE_SPACE = " "
        const val UNDERSCORE = "_"
    }
}