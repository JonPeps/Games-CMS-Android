package com.jonpeps.gamescms.ui.tabletemplates.repositories

import com.jonpeps.gamescms.data.dataclasses.TableItemList
import com.jonpeps.gamescms.data.serialization.ITableItemListMoshiSerialization
import javax.inject.Inject

data class TableTemplateRepositoryStatus(
    var success: Boolean,
    var item: TableItemList,
    var message: String?,
    var ex: Exception?)

interface ITableTemplateRepository {
    fun loadTemplate(name: String): Boolean
    fun saveTemplate(name: String): Boolean
    fun deleteTemplate(name: String): Boolean
    fun getRepositoryStatus(): TableTemplateRepositoryStatus
}

class TableTemplateRepository@Inject
constructor(
    private val tableTemplateRepository: ITableItemListMoshiSerialization)
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

    override fun getRepositoryStatus(): TableTemplateRepositoryStatus {
        TODO("Not yet implemented")
    }

}