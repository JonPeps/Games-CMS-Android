package com.jonpeps.gamescms.data.viewmodels

import com.jonpeps.gamescms.data.dataclasses.moshi.StringListMoshi
import com.jonpeps.gamescms.data.repositories.IMoshiStringListRepository
import com.jonpeps.gamescms.data.serialization.ICommonSerializationRepoHelper
import com.jonpeps.gamescms.data.serialization.debug.IInputStreamSerializationRepoHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class InputStreamStringListViewModel@Inject constructor(
    moshiStringListRepository: IMoshiStringListRepository,
    commonSerializationRepoHelper: ICommonSerializationRepoHelper,
    inputStreamSerializationRepoHelper: IInputStreamSerializationRepoHelper,
    coroutineDispatcher: CoroutineDispatcher
): InputStreamToJsonTypeToStorageVm<StringListMoshi>(
    moshiStringListRepository,
    commonSerializationRepoHelper,
    inputStreamSerializationRepoHelper,
    coroutineDispatcher
)