package com.jonpeps.gamescms.data.viewmodels

import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateItemListMoshi
import com.jonpeps.gamescms.data.repositories.IMoshiTableTemplateRepository
import com.jonpeps.gamescms.data.serialization.ICommonSerializationRepoHelper
import com.jonpeps.gamescms.data.serialization.debug.IInputStreamSerializationRepoHelper
import javax.inject.Inject

class InputStreamTableTemplateVm@Inject constructor(
    moshiTableTemplateRepository: IMoshiTableTemplateRepository,
    commonSerializationRepoHelper: ICommonSerializationRepoHelper,
    inputStreamSerializationRepoHelper: IInputStreamSerializationRepoHelper)
    : InputStreamToJsonTypeToStorage<TableTemplateItemListMoshi>(
    moshiTableTemplateRepository,
    commonSerializationRepoHelper,
    inputStreamSerializationRepoHelper)