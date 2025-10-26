package com.jonpeps.gamescms.data.viewmodels

import com.jonpeps.gamescms.data.dataclasses.moshi.StringListMoshi
import com.jonpeps.gamescms.data.repositories.IMoshiStringListRepository
import com.jonpeps.gamescms.data.serialization.ICommonSerializationRepoHelper
import com.jonpeps.gamescms.data.serialization.debug.IInputStreamSerializationRepoHelper
import com.jonpeps.gamescms.data.viewmodels.factories.InputStreamStringListViewModelFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import java.io.InputStream

@HiltViewModel(assistedFactory = InputStreamStringListViewModelFactory.IInputStreamToStringListVmFactory::class)
class InputStreamStringListViewModel @AssistedInject constructor(
    @Assisted("param1") private val inputStream: InputStream,
    @Assisted("param2") private val directory: String,
    moshiStringListRepository: IMoshiStringListRepository,
    commonSerializationRepoHelper: ICommonSerializationRepoHelper,
    inputStreamSerializationRepoHelper: IInputStreamSerializationRepoHelper,
    coroutineDispatcher: CoroutineDispatcher
): InputStreamToJsonTypeToStorageVm<StringListMoshi>(
    inputStream,
    directory,
    moshiStringListRepository,
    commonSerializationRepoHelper,
    inputStreamSerializationRepoHelper,
    coroutineDispatcher
)