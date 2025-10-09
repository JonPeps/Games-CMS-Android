package com.jonpeps.gamescms.data.repositories

import com.jonpeps.gamescms.data.helpers.IBasicStringGenericItemCache
import com.jonpeps.gamescms.data.serialization.string.IStringFileStorageStrSerialisation

open class MoshiJsonRepository<T>(moshiJsonAdapter: MoshiJsonAdapter<T>,
                                  moshiJsonCache: IBasicStringGenericItemCache<T>,
                                  stringFileStorageStrSerialisation: IStringFileStorageStrSerialisation
) : BaseMoshiJsonRepository<T>(moshiJsonAdapter, stringFileStorageStrSerialisation, moshiJsonCache)