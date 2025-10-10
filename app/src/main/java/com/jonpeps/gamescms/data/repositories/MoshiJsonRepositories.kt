package com.jonpeps.gamescms.data.repositories

import com.jonpeps.gamescms.data.dataclasses.moshi.StringListMoshi
import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateItemListMoshi
import com.jonpeps.gamescms.data.serialization.moshi.MoshiJsonBuilder
import com.jonpeps.gamescms.data.serialization.string.IStringFileStorageStrSerialisation
import com.squareup.moshi.JsonAdapter
import javax.inject.Inject

class StringListMoshiJsonAdapter : MoshiJsonAdapter<StringListMoshi> {
    override fun getJsonAdapter(): JsonAdapter<StringListMoshi> =
        MoshiJsonBuilder.build().adapter(StringListMoshi::class.java)
}

class TableTemplateMoshiJsonAdapter : MoshiJsonAdapter<TableTemplateItemListMoshi> {
    override fun getJsonAdapter(): JsonAdapter<TableTemplateItemListMoshi> =
        MoshiJsonBuilder.build().adapter(TableTemplateItemListMoshi::class.java)
}

///////////////////////////////////////////////////////////////////////////////

interface IMoshiStringListRepository : IBaseCachedMoshiJsonRepository<StringListMoshi>

open class MoshiStringListRepository
@Inject constructor(stringListMoshiJsonAdapter: StringListMoshiJsonAdapter,
                    tableTemplateStringMoshiJsonCache: IStringListMoshiJsonCache,
                    stringFileStorageStrSerialisation: IStringFileStorageStrSerialisation
) : BaseCachedMoshiJsonRepository<StringListMoshi>(stringListMoshiJsonAdapter,
    stringFileStorageStrSerialisation,
    tableTemplateStringMoshiJsonCache), IMoshiStringListRepository

////////////////////////////////////////////////////////////////////////////////

interface ITableTemplateFileRepository : IBaseCachedMoshiJsonRepository<TableTemplateItemListMoshi>

class TableTemplateFileRepository
@Inject constructor(tableTemplateItemListMoshiAdapter: TableTemplateMoshiJsonAdapter,
                    tableTemplateStringMoshiJsonCache: ITableTemplateStringMoshiJsonCache,
                    stringFileStorageStrSerialisation: IStringFileStorageStrSerialisation)
    : BaseCachedMoshiJsonRepository<TableTemplateItemListMoshi>(tableTemplateItemListMoshiAdapter,
    stringFileStorageStrSerialisation,
    tableTemplateStringMoshiJsonCache), ITableTemplateFileRepository

