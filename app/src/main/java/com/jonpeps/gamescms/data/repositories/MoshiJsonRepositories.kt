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

interface IMoshiStringListRepository : IBaseSingleItemMoshiJsonRepository<StringListMoshi>
interface ICachedMoshiStringListRepository : IBaseCachedMoshiJsonRepository<StringListMoshi>

class MoshiStringListRepository
@Inject constructor(stringListMoshiJsonAdapter: StringListMoshiJsonAdapter,
                    stringFileStorageStrSerialisation: IStringFileStorageStrSerialisation
) : BaseSingleItemMoshiJsonRepository<StringListMoshi>(stringListMoshiJsonAdapter,
    stringFileStorageStrSerialisation), IMoshiStringListRepository

class CachedMoshiStringListRepository
@Inject constructor(stringListMoshiJsonAdapter: StringListMoshiJsonAdapter,
                    tableTemplateStringMoshiJsonCache: IStringListMoshiJsonCache,
                    stringFileStorageStrSerialisation: IStringFileStorageStrSerialisation
) : BaseCachedMoshiJsonRepository<StringListMoshi>(stringListMoshiJsonAdapter,
    stringFileStorageStrSerialisation,
    tableTemplateStringMoshiJsonCache), ICachedMoshiStringListRepository

interface IMoshiTableTemplateRepository : IBaseSingleItemMoshiJsonRepository<TableTemplateItemListMoshi>

class MoshiTableTemplateRepository
@Inject constructor(tableTemplateItemListMoshiAdapter: TableTemplateMoshiJsonAdapter,
                    stringFileStorageStrSerialisation: IStringFileStorageStrSerialisation)
    : BaseSingleItemMoshiJsonRepository<TableTemplateItemListMoshi>(tableTemplateItemListMoshiAdapter,
    stringFileStorageStrSerialisation), IMoshiTableTemplateRepository

