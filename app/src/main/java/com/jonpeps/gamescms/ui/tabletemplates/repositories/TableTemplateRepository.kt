package com.jonpeps.gamescms.ui.tabletemplates.repositories

import com.jonpeps.gamescms.data.dataclasses.TableItemList
import com.jonpeps.gamescms.data.repositories.BaseMoshiJsonRepository
import com.jonpeps.gamescms.data.repositories.IBaseMoshiJsonRepository
import com.jonpeps.gamescms.data.serialization.moshi.IBaseMoshiSerialization
import com.jonpeps.gamescms.data.serialization.moshi.ITableItemListMoshiSerialization
import com.jonpeps.gamescms.data.serialization.string.IStringFileStorageStrSerialisation
import javax.inject.Inject

interface ITableTemplateRepository : IBaseMoshiJsonRepository<TableItemList> {
    suspend fun deleteTemplate(): Boolean
}

class TableTemplateRepository
@Inject constructor(private val tableTableItemListMoshiSerialization: ITableItemListMoshiSerialization,
                    stringFileStorageStrSerialisation: IStringFileStorageStrSerialisation)
    :  BaseMoshiJsonRepository<TableItemList>(stringFileStorageStrSerialisation), ITableTemplateRepository {

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

    override fun getMoshiSerializer(): IBaseMoshiSerialization<TableItemList> = tableTableItemListMoshiSerialization
}