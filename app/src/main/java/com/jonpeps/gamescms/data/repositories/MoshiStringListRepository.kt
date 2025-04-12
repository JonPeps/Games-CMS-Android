package com.jonpeps.gamescms.data.repositories

import com.jonpeps.gamescms.data.dataclasses.StringListData
import com.jonpeps.gamescms.data.serialization.moshi.IBaseMoshiSerialization
import com.jonpeps.gamescms.data.serialization.moshi.IStringListMoshiSerialization
import com.jonpeps.gamescms.data.serialization.string.IStringFileStorageStrSerialisation
import javax.inject.Inject

interface IMoshiStringListRepository : IBaseMoshiJsonRepository<StringListData>

class MoshiStringListRepository
@Inject constructor(private val strListMoshiSerialization: IStringListMoshiSerialization,
                    stringFileStorageStrSerialisation: IStringFileStorageStrSerialisation
) : BaseMoshiJsonRepository<StringListData>(stringFileStorageStrSerialisation), IMoshiStringListRepository {

    override fun getMoshiSerializer(): IBaseMoshiSerialization<StringListData> = strListMoshiSerialization
}