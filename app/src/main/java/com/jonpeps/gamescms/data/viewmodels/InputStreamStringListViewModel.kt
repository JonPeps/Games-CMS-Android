package com.jonpeps.gamescms.data.viewmodels

import com.jonpeps.gamescms.data.DataConstants.Companion.COMMON_SPLIT_PER_STRING_ITEM
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

interface IInputStreamStringListViewModel : IInputStreamToJsonTypeToStorageVm {
    fun getFileNamesOfList(): List<String>
}

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
), IInputStreamStringListViewModel {
    private val fileNamesOfList = arrayListOf<String>()
    override fun getFileNamesOfList() = fileNamesOfList
    override fun onItem(item: StringListMoshi?) {
        item?.let {  item ->
            val newItems = arrayListOf<String>()
            item.items.forEach { listItem ->
                val splitList = listItem.split(COMMON_SPLIT_PER_STRING_ITEM)
                if (splitList.size == 2) {
                    newItems.add(splitList[0])
                    fileNamesOfList.add(splitList[1])
                }
            }
            item.items = newItems
        }
    }
}