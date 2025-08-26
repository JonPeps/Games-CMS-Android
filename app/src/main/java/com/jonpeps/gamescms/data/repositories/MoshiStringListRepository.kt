package com.jonpeps.gamescms.data.repositories

import com.jonpeps.gamescms.data.dataclasses.moshi.StringListMoshi
import com.jonpeps.gamescms.data.serialization.moshi.MoshiJsonBuilder
import com.jonpeps.gamescms.data.serialization.string.IStringFileStorageStrSerialisation
import com.jonpeps.gamescms.ui.tabletemplates.repositories.IStringListMoshiJsonCache
import com.squareup.moshi.JsonAdapter
import javax.inject.Inject

class StringListMoshiJsonAdapter : MoshiJsonAdapter<StringListMoshi> {
    override fun getJsonAdapter(): JsonAdapter<StringListMoshi> =
        MoshiJsonBuilder.build().adapter(StringListMoshi::class.java)
}

interface IMoshiStringListRepository : IBaseMoshiJsonRepository<StringListMoshi>

open class MoshiStringListRepository
@Inject constructor(stringListMoshiJsonAdapter: StringListMoshiJsonAdapter,
                    tableTemplateStringMoshiJsonCache: IStringListMoshiJsonCache,
                    stringFileStorageStrSerialisation: IStringFileStorageStrSerialisation
) : BaseMoshiJsonRepository<StringListMoshi>(stringListMoshiJsonAdapter,
    stringFileStorageStrSerialisation,
    tableTemplateStringMoshiJsonCache), IMoshiStringListRepository