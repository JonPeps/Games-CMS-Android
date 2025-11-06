package com.jonpeps.gamescms.data.viewmodels

import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateItemListMoshi
import com.jonpeps.gamescms.data.repositories.IMoshiTableTemplateRepository
import com.jonpeps.gamescms.data.serialization.ICommonSerializationRepoHelper
import com.jonpeps.gamescms.data.serialization.debug.IInputStreamSerializationRepoHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class InputStreamTableTemplateVm@Inject constructor(
    moshiTableTemplateRepository: IMoshiTableTemplateRepository,
    commonSerializationRepoHelper: ICommonSerializationRepoHelper,
    inputStreamSerializationRepoHelper: IInputStreamSerializationRepoHelper,
    coroutineDispatcher: CoroutineDispatcher)
    : InputStreamToJsonTypeToStorageVm<TableTemplateItemListMoshi>(
    moshiTableTemplateRepository,
    commonSerializationRepoHelper,
    inputStreamSerializationRepoHelper,
    coroutineDispatcher) {
}