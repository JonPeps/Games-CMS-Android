package com.jonpeps.gamescms.data.viewmodels

import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateItemListMoshi
import com.jonpeps.gamescms.data.repositories.IMoshiTableTemplateRepository
import com.jonpeps.gamescms.data.serialization.ICommonSerializationRepoHelper
import com.jonpeps.gamescms.data.serialization.debug.IInputStreamSerializationRepoHelper
import com.jonpeps.gamescms.data.viewmodels.factories.InputStreamTableTemplateVmFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import java.io.InputStream

@HiltViewModel(assistedFactory = InputStreamTableTemplateVmFactory.IInputStreamTableTemplateVmFactory::class)
class InputStreamTableTemplateVm @AssistedInject constructor(
    @Assisted("param1") private val inputStream: InputStream,
    @Assisted("param2") private val directory: String,
    @Assisted("param3") private val moshiTableTemplateRepository: IMoshiTableTemplateRepository,
    commonSerializationRepoHelper: ICommonSerializationRepoHelper,
    inputStreamSerializationRepoHelper: IInputStreamSerializationRepoHelper,
    coroutineDispatcher: CoroutineDispatcher)
    : InputStreamToJsonTypeToStorageVm<TableTemplateItemListMoshi>(inputStream,
    directory,
    moshiTableTemplateRepository,
    commonSerializationRepoHelper,
    inputStreamSerializationRepoHelper,
    coroutineDispatcher) {

}