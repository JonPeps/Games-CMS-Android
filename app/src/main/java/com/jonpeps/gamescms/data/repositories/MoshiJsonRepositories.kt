package com.jonpeps.gamescms.data.repositories

import com.jonpeps.gamescms.data.dataclasses.moshi.StringListMoshi
import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateDetailsListMoshi
import com.jonpeps.gamescms.data.serialization.moshi.MoshiJsonBuilder
import com.jonpeps.gamescms.data.serialization.string.IStringFileStorageStrSerialisation
import com.squareup.moshi.JsonAdapter
import javax.inject.Inject

class StringListMoshiJsonAdapter : MoshiJsonAdapter<StringListMoshi> {
    override fun getJsonAdapter(): JsonAdapter<StringListMoshi> =
        MoshiJsonBuilder.build().adapter(StringListMoshi::class.java)
}

class TableTemplateMoshiJsonAdapter : MoshiJsonAdapter<TableTemplateDetailsListMoshi> {
    override fun getJsonAdapter(): JsonAdapter<TableTemplateDetailsListMoshi> =
        MoshiJsonBuilder.build().adapter(TableTemplateDetailsListMoshi::class.java)
}

class TableTemplateDetailsListMoshiAdapter : MoshiJsonAdapter<TableTemplateDetailsListMoshi> {
    override fun getJsonAdapter(): JsonAdapter<TableTemplateDetailsListMoshi> {
        return MoshiJsonBuilder.build().adapter(TableTemplateDetailsListMoshi::class.java)
    }
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

interface IMoshiTableTemplateRepository : IBaseSingleItemMoshiJsonRepository<TableTemplateDetailsListMoshi>

class MoshiTableTemplateRepository
@Inject constructor(tableTemplateItemListMoshiAdapter: TableTemplateMoshiJsonAdapter,
                    stringFileStorageStrSerialisation: IStringFileStorageStrSerialisation)
    : BaseSingleItemMoshiJsonRepository<TableTemplateDetailsListMoshi>(tableTemplateItemListMoshiAdapter,
    stringFileStorageStrSerialisation), IMoshiTableTemplateRepository

interface IMoshiTableTemplateDetailsListRepository : IBaseSingleItemMoshiJsonRepository<TableTemplateDetailsListMoshi>

class MoshiTableTemplateDetailsListRepository
@Inject constructor(tableTemplateDetailsListMoshiAdapter: TableTemplateDetailsListMoshiAdapter,
                    stringFileStorageStrSerialisation: IStringFileStorageStrSerialisation)
    : BaseSingleItemMoshiJsonRepository<TableTemplateDetailsListMoshi>(
    tableTemplateDetailsListMoshiAdapter, stringFileStorageStrSerialisation), IMoshiTableTemplateDetailsListRepository