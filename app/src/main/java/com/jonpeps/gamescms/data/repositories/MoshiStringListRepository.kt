package com.jonpeps.gamescms.data.repositories

import com.jonpeps.gamescms.data.dataclasses.moshi.StringListMoshi
import com.jonpeps.gamescms.data.serialization.string.IStringFileStorageStrSerialisation
import com.jonpeps.gamescms.ui.tabletemplates.repositories.IStringListMoshiJsonCache
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.ToJson
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class JsonAdapterStringListMoshi : JsonAdapter<StringListMoshi>() {
    @FromJson
    override fun fromJson(reader: JsonReader): StringListMoshi {
        val items = reader.readJsonValue() as (Map<String, Any>)
        val itemsList = items[ITEM_KEY] as List<String>
        return StringListMoshi(itemsList)
    }
    @ToJson
    override fun toJson(writer: JsonWriter, value: StringListMoshi?) {
        val jsonValue = value.toString()
        writer.jsonValue(jsonValue)
    }

    companion object {
        const val ITEM_KEY = "items"
    }
}

class StringListMoshiJsonAdapter : MoshiJsonAdapter<StringListMoshi> {
    override fun getJsonAdapter(): JsonAdapter<StringListMoshi> = JsonAdapterStringListMoshi()
}

interface IMoshiStringListRepository : IBaseMoshiJsonRepository<StringListMoshi>

open class MoshiStringListRepository
@Inject constructor(stringListMoshiJsonAdapter: StringListMoshiJsonAdapter,
                    tableTemplateStringMoshiJsonCache: IStringListMoshiJsonCache,
                    stringFileStorageStrSerialisation: IStringFileStorageStrSerialisation
) : BaseMoshiJsonRepository<StringListMoshi>(stringListMoshiJsonAdapter,
    stringFileStorageStrSerialisation,
    tableTemplateStringMoshiJsonCache), IMoshiStringListRepository