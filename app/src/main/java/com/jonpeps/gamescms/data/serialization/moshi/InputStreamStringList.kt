package com.jonpeps.gamescms.data.serialization.moshi

import com.jonpeps.gamescms.data.dataclasses.moshi.StringListMoshi
import com.jonpeps.gamescms.data.repositories.IMoshiStringListRepository
import com.jonpeps.gamescms.data.serialization.ICommonSerializationRepoHelper
import com.jonpeps.gamescms.data.serialization.debug.IInputStreamSerializationRepoHelper
import javax.inject.Inject

class InputStreamStringList@Inject constructor(
    moshiStringListRepository: IMoshiStringListRepository,
    commonSerializationRepoHelper: ICommonSerializationRepoHelper,
    inputStreamSerializationRepoHelper: IInputStreamSerializationRepoHelper,
): InputStreamToJsonTypeToStorage<StringListMoshi>(
    moshiStringListRepository,
    commonSerializationRepoHelper,
    inputStreamSerializationRepoHelper
)