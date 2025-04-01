package com.jonpeps.gamescms.ui.tabletemplates.repositories

import com.jonpeps.gamescms.data.dataclasses.TableItem
import com.jonpeps.gamescms.data.serialization.moshi.ITableItemListMoshiSerialization
import javax.inject.Inject

interface ITableTemplateRepository {
    fun loadTemplate(name: String): Boolean
    fun saveTemplate(name: String): Boolean
    fun deleteTemplate(name: String): Boolean
    fun getItem(): TableItem?
    fun getErrorMsg(): String
}

class TableTemplateRepository
@Inject constructor(
    private val tableTemplateRepository: ITableItemListMoshiSerialization
)
    : ITableTemplateRepository {

    override fun loadTemplate(name: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun saveTemplate(name: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun deleteTemplate(name: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun getItem(): TableItem? {
        TODO("Not yet implemented")
    }

    override fun getErrorMsg(): String {
        TODO("Not yet implemented")
    }


}