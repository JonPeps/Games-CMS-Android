package com.jonpeps.gamescms.data.viewmodels

import androidx.lifecycle.viewModelScope
import com.jonpeps.gamescms.data.DataConstants.Companion.JSON_EXTENSION
import com.jonpeps.gamescms.data.dataclasses.moshi.StringListMoshi
import com.jonpeps.gamescms.data.repositories.IMoshiStringListRepository
import com.jonpeps.gamescms.data.serialization.ICommonSerializationRepoHelper
import com.jonpeps.gamescms.data.serialization.StringListStatus
import com.jonpeps.gamescms.data.serialization.debug.IInputStreamSerializationRepoHelper
import com.jonpeps.gamescms.data.viewmodels.factories.InputStreamStringListViewModelFactory
import com.jonpeps.gamescms.ui.tabletemplates.viewmodels.IStringListItemsVmChangesCache
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import java.io.InputStream

interface IInputStreamStringListViewModel {
    fun loadFromInputStream(cacheName: String, inputStream: InputStream)
}

@HiltViewModel(assistedFactory = InputStreamStringListViewModelFactory.IInputStreamStringListViewModelFactory::class)
    class InputStreamStringListViewModel@AssistedInject constructor(
    @Assisted("param1") private val stringListPath: String,
    private val commonSerializationRepoHelper: ICommonSerializationRepoHelper,
    private val inputStreamSerializationRepoHelper: IInputStreamSerializationRepoHelper,
    private val moshiStringListRepository: IMoshiStringListRepository,
    private val listItemsVmChangesCache: IStringListItemsVmChangesCache,
    private val coroutineDispatcher: CoroutineDispatcher
): BaseStringListViewModel(), IInputStreamStringListViewModel {
    override fun loadFromInputStream(cacheName: String, inputStream: InputStream) {
        viewModelScope.launch(coroutineDispatcher) {
            baseIsProcessing.value = true
            items.clear()
            exception = null
            var errorMessage = ""
            var success = true
            try {
                initReadFiles(inputStream)
                if (moshiStringListRepository.serialize(cacheName, commonSerializationRepoHelper.readAll(inputStream))) {
                    val stringList = moshiStringListRepository.getItem(cacheName)
                    if (stringList != null) {
                        items = ArrayList(stringList.items)
                    } else {
                        success = false
                        errorMessage = FAILED_TO_LOAD_FILE
                    }
                } else {
                    success = false
                    errorMessage = FAILED_TO_LOAD_FILE
                }
            } catch (ex: Exception) {
                exception = ex
                errorMessage = ex.message.toString()
                success = false
            }
            if (success) {
                try {
                    initWriteFiles(cacheName)
                    if (moshiStringListRepository.save(cacheName, StringListMoshi(items))) {
                        listItemsVmChangesCache.set(cacheName, items)
                    } else {
                        items.clear()
                        success = false
                        errorMessage = FAILED_TO_WRITE_FILE
                    }
                } catch (ex: Exception) {
                    items.clear()
                    exception = ex
                    errorMessage = ex.message.toString()
                    success = false
                }
            }
            baseIsProcessing.value = false
            status = StringListStatus(success, items, errorMessage, exception)
        }
    }

    private fun initReadFiles(inputStream: InputStream) {
        moshiStringListRepository.setBufferReader(
            inputStreamSerializationRepoHelper.getBufferReader(inputStream)
        )
    }

    private fun initWriteFiles(cacheName: String) {
        moshiStringListRepository.setAbsoluteFile(
            commonSerializationRepoHelper.getAbsoluteFile(stringListPath, cacheName + JSON_EXTENSION))
        moshiStringListRepository.setFile(commonSerializationRepoHelper.getMainFile(cacheName + JSON_EXTENSION))
        moshiStringListRepository.setDirectoryFile(
            commonSerializationRepoHelper.getDirectoryFile(stringListPath))
        moshiStringListRepository.setFileWriter(
            commonSerializationRepoHelper.getFileWriter(stringListPath, cacheName + JSON_EXTENSION))
        moshiStringListRepository
            .setItem(cacheName, StringListMoshi(items))
    }

    companion object {
        const val FAILED_TO_LOAD_FILE = "Failed to load string list from assets!"
        const val FAILED_TO_WRITE_FILE = "Failed to write string list to local storage!"
    }
}