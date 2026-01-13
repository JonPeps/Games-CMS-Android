package com.jonpeps.gamescms.data.helpers

import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateDetailsListMoshi
import com.jonpeps.gamescms.data.repositories.IMoshiTableTemplateDetailsListRepository
import com.jonpeps.gamescms.data.serialization.ICommonSerializationRepoHelper
import com.jonpeps.gamescms.data.serialization.debug.IInputStreamSerializationRepoHelper
import com.jonpeps.gamescms.data.serialization.moshi.InputStreamToJsonTypeToStorage
import javax.inject.Inject

class InputStreamTableTemplateStatus@Inject constructor(
    moshiTableTemplateDetailsListRepository: IMoshiTableTemplateDetailsListRepository,
    commonSerializationRepoHelper: ICommonSerializationRepoHelper,
    inputStreamSerializationRepoHelper: IInputStreamSerializationRepoHelper
) : InputStreamToJsonTypeToStorage<TableTemplateDetailsListMoshi>(
    moshiTableTemplateDetailsListRepository,
    commonSerializationRepoHelper,
    inputStreamSerializationRepoHelper) {
}