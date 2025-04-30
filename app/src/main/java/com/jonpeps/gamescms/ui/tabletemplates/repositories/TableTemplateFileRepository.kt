package com.jonpeps.gamescms.ui.tabletemplates.repositories

import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateItemListMoshi
import com.jonpeps.gamescms.data.repositories.BaseMoshiJsonRepository
import com.jonpeps.gamescms.data.repositories.IBaseMoshiJsonRepository
import com.jonpeps.gamescms.data.serialization.moshi.IBaseMoshiSerialization
import com.jonpeps.gamescms.data.serialization.moshi.ITableItemListMoshiSerialization
import com.jonpeps.gamescms.data.serialization.string.IStringFileStorageStrSerialisation
import javax.inject.Inject

interface ITableTemplateFileRepository : IBaseMoshiJsonRepository<TableTemplateItemListMoshi> {
    suspend fun deleteTemplate(): Boolean
}

class TableTemplateFileRepository
@Inject constructor(private val tableTableItemListMoshiSerialization: ITableItemListMoshiSerialization,
                    tableTemplateStringMoshiJsonCache: ITableTemplateStringListMoshiJsonCache,
                    stringFileStorageStrSerialisation: IStringFileStorageStrSerialisation)
    :  BaseMoshiJsonRepository<TableTemplateItemListMoshi>(stringFileStorageStrSerialisation, tableTemplateStringMoshiJsonCache), ITableTemplateFileRepository {

    override suspend fun deleteTemplate(): Boolean {
        errorMessage = ""
        if (mainAbsolutePath.delete()) {
            return true
        } else {
            errorMessage = FAILED_TO_DELETE_FILE + mainAbsolutePath
            return false
        }
    }

    companion object {
        const val FAILED_TO_DELETE_FILE = "Failed to delete file: "
    }

    override fun getMoshiSerializer(): IBaseMoshiSerialization<TableTemplateItemListMoshi> = tableTableItemListMoshiSerialization
}