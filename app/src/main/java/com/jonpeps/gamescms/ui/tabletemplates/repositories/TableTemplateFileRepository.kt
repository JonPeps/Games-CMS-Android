package com.jonpeps.gamescms.ui.tabletemplates.repositories

import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateItemListMoshi
import com.jonpeps.gamescms.data.repositories.BaseMoshiJsonRepository
import com.jonpeps.gamescms.data.repositories.IBaseMoshiJsonRepository
import com.jonpeps.gamescms.data.serialization.moshi.ITableItemListMoshiSerialization
import com.jonpeps.gamescms.data.serialization.string.IStringFileStorageStrSerialisation
import javax.inject.Inject

interface ITableTemplateFileRepository : IBaseMoshiJsonRepository<TableTemplateItemListMoshi>

class TableTemplateFileRepository
@Inject constructor(private val tableTableItemListMoshiSerialization: ITableItemListMoshiSerialization,
                    tableTemplateStringMoshiJsonCache: ITableTemplateStringMoshiJsonCache,
                    stringFileStorageStrSerialisation: IStringFileStorageStrSerialisation)
    : BaseMoshiJsonRepository<TableTemplateItemListMoshi>(stringFileStorageStrSerialisation, tableTemplateStringMoshiJsonCache), ITableTemplateFileRepository {

    override fun getMoshiSerializer() = tableTableItemListMoshiSerialization
}