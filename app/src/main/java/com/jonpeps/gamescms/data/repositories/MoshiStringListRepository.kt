package com.jonpeps.gamescms.data.repositories

import com.jonpeps.gamescms.data.dataclasses.moshi.StringListMoshi
import com.jonpeps.gamescms.data.serialization.moshi.IStringListMoshiSerialization
import com.jonpeps.gamescms.data.serialization.string.IStringFileStorageStrSerialisation
import com.jonpeps.gamescms.ui.tabletemplates.repositories.IStringListMoshiJsonCache
import javax.inject.Inject

interface IMoshiStringListRepository : IBaseMoshiJsonRepository<StringListMoshi>

class MoshiStringListRepository
@Inject constructor(private val strListMoshiSerialization: IStringListMoshiSerialization,
                    tableTemplateStringMoshiJsonCache: IStringListMoshiJsonCache,
                    stringFileStorageStrSerialisation: IStringFileStorageStrSerialisation
) : BaseMoshiJsonRepository<StringListMoshi>(stringFileStorageStrSerialisation, tableTemplateStringMoshiJsonCache), IMoshiStringListRepository {

    override fun getMoshiSerializer() = strListMoshiSerialization
}