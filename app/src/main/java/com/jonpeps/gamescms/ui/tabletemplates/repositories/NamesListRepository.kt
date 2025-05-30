package com.jonpeps.gamescms.ui.tabletemplates.repositories

import com.jonpeps.gamescms.data.dataclasses.moshi.StringListMoshi
import com.jonpeps.gamescms.data.repositories.BaseMoshiJsonRepository
import com.jonpeps.gamescms.data.repositories.IBaseMoshiJsonRepository
import com.jonpeps.gamescms.data.serialization.moshi.IStringListMoshiSerialization
import com.jonpeps.gamescms.data.serialization.string.IStringFileStorageStrSerialisation
import javax.inject.Inject

interface ITableTemplateFileNamesRepository : IBaseMoshiJsonRepository<StringListMoshi>

class NamesListRepository
@Inject constructor(private val stringListMoshiSerialization: IStringListMoshiSerialization,
                    stringListMoshiJsonCache: IStringListMoshiJsonCache,
                    stringFileStorageStrSerialisation: IStringFileStorageStrSerialisation)
    : BaseMoshiJsonRepository<StringListMoshi>(stringFileStorageStrSerialisation, stringListMoshiJsonCache), ITableTemplateFileNamesRepository {

    override fun getMoshiSerializer() = stringListMoshiSerialization
}