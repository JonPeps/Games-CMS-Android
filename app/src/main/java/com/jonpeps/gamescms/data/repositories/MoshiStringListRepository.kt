package com.jonpeps.gamescms.data.repositories

import com.jonpeps.gamescms.data.dataclasses.moshi.StringListMoshi
import com.jonpeps.gamescms.data.serialization.string.IStringFileStorageStrSerialisation
import com.jonpeps.gamescms.ui.tabletemplates.repositories.IStringListMoshiJsonCache
import com.squareup.moshi.JsonAdapter
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class StringListMoshiJsonAdapter : MoshiJsonAdapter<StringListMoshi> {
    override fun getJsonAdapter(): JsonAdapter<StringListMoshi> = object :
        JsonAdapter<StringListMoshi>() {
        override fun fromJson(reader: com.squareup.moshi.JsonReader): StringListMoshi {
            return StringListMoshi(reader.readJsonValue() as List<String>)
        }
        override fun toJson(writer: com.squareup.moshi.JsonWriter, value: StringListMoshi?) {
            writer.jsonValue(value)
        }
    }
}

interface IMoshiStringListRepository : IBaseMoshiJsonRepository<StringListMoshi>

@Suppress("UNCHECKED_CAST")
open class MoshiStringListRepository
@Inject constructor(stringListMoshiJsonAdapter: StringListMoshiJsonAdapter,
                    tableTemplateStringMoshiJsonCache: IStringListMoshiJsonCache,
                    stringFileStorageStrSerialisation: IStringFileStorageStrSerialisation
) : BaseMoshiJsonRepository<StringListMoshi>(stringListMoshiJsonAdapter,
    stringFileStorageStrSerialisation,
    tableTemplateStringMoshiJsonCache), IMoshiStringListRepository