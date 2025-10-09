package com.jonpeps.gamescms.data.repositories

import com.jonpeps.gamescms.data.dataclasses.moshi.StringListMoshi
import com.jonpeps.gamescms.data.helpers.IBasicStringGenericItemCache
import com.jonpeps.gamescms.data.serialization.string.IStringFileStorageStrSerialisation
import javax.inject.Inject

interface IMoshiStringListRepository : IBaseMoshiJsonRepository<StringListMoshi>

class MoshiStringListRepository
@Inject constructor(adapter: StringListMoshiJsonAdapter,
                    stringFileStorageStrSerialisation: IStringFileStorageStrSerialisation,
                    basicStringGenericItemCache: IBasicStringGenericItemCache<StringListMoshi>
) : BaseMoshiJsonRepository<StringListMoshi>(
    adapter, stringFileStorageStrSerialisation, basicStringGenericItemCache
), IMoshiStringListRepository