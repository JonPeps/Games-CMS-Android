package com.jonpeps.gamescms.data.helpers

import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateItemListMoshi
import com.jonpeps.gamescms.data.repositories.IMoshiTableTemplateRepository
import com.jonpeps.gamescms.data.serialization.ICommonSerializationRepoHelper
import com.jonpeps.gamescms.data.serialization.debug.IInputStreamSerializationRepoHelper
import com.jonpeps.gamescms.data.serialization.moshi.InputStreamToJsonTypeToStorage
import javax.inject.Inject

class InputStreamTableTemplate@Inject constructor(
    moshiTableTemplateRepository: IMoshiTableTemplateRepository,
    commonSerializationRepoHelper: ICommonSerializationRepoHelper,
    inputStreamSerializationRepoHelper: IInputStreamSerializationRepoHelper
) : InputStreamToJsonTypeToStorage<TableTemplateItemListMoshi>(
    moshiTableTemplateRepository,
    commonSerializationRepoHelper,
    inputStreamSerializationRepoHelper)