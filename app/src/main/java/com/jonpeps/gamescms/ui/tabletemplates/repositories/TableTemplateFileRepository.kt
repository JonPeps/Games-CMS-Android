package com.jonpeps.gamescms.ui.tabletemplates.repositories

import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateItemListMoshi
import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateItemMoshi
import com.jonpeps.gamescms.data.repositories.BaseMoshiJsonRepository
import com.jonpeps.gamescms.data.repositories.IBaseMoshiJsonRepository
import com.jonpeps.gamescms.data.repositories.MoshiJsonAdapter
import com.jonpeps.gamescms.data.serialization.string.IStringFileStorageStrSerialisation
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import javax.inject.Inject

class TableTemplateItemListMoshiAdapter : MoshiJsonAdapter<TableTemplateItemListMoshi> {
    override fun getJsonAdapter(): JsonAdapter<TableTemplateItemListMoshi> = object : JsonAdapter<TableTemplateItemListMoshi>() {
        override fun fromJson(reader: JsonReader): TableTemplateItemListMoshi {
            return TableTemplateItemListMoshi(reader.nextString().toString(), reader.readJsonValue() as List<TableTemplateItemMoshi>)
        }
        override fun toJson(writer: JsonWriter, value: TableTemplateItemListMoshi?) {
            writer.jsonValue(value)
        }
    }
}

interface ITableTemplateFileRepository : IBaseMoshiJsonRepository<TableTemplateItemListMoshi>

@Suppress("UNCHECKED_CAST")
class TableTemplateFileRepository
@Inject constructor(tableTemplateItemListMoshiAdapter: TableTemplateItemListMoshiAdapter,
                    tableTemplateStringMoshiJsonCache: ITableTemplateStringMoshiJsonCache,
                    stringFileStorageStrSerialisation: IStringFileStorageStrSerialisation)
    : BaseMoshiJsonRepository<TableTemplateItemListMoshi>(tableTemplateItemListMoshiAdapter,
    stringFileStorageStrSerialisation,
    tableTemplateStringMoshiJsonCache), ITableTemplateFileRepository {
}