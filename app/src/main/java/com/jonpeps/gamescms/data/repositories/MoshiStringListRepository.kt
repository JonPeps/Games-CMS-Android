package com.jonpeps.gamescms.data.repositories

import com.jonpeps.gamescms.data.dataclasses.StringListData
import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateItemListMoshi
import com.jonpeps.gamescms.data.serialization.moshi.IBaseMoshiSerialization
import com.jonpeps.gamescms.data.serialization.moshi.IStringListMoshiSerialization
import com.jonpeps.gamescms.data.serialization.string.IStringFileStorageStrSerialisation
import com.jonpeps.gamescms.ui.tabletemplates.repositories.ITableTemplateStringListMoshiJsonCache
import javax.inject.Inject

interface IMoshiStringListRepository : IBaseMoshiJsonRepository<TableTemplateItemListMoshi>

class MoshiStringListRepository
@Inject constructor(private val strListMoshiSerialization: IStringListMoshiSerialization,
                    tableTemplateStringMoshiJsonCache: ITableTemplateStringListMoshiJsonCache,
                    stringFileStorageStrSerialisation: IStringFileStorageStrSerialisation
) : BaseMoshiJsonRepository<TableTemplateItemListMoshi>(stringFileStorageStrSerialisation, tableTemplateStringMoshiJsonCache), IMoshiStringListRepository {

    override fun getMoshiSerializer(): IBaseMoshiSerialization<TableTemplateItemListMoshi> = strListMoshiSerialization
}