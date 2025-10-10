package com.jonpeps.gamescms.data.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonpeps.gamescms.data.DataConstants.Companion.JSON_EXTENSION
import com.jonpeps.gamescms.data.dataclasses.moshi.StringListMoshi
import com.jonpeps.gamescms.data.repositories.IMoshiStringListRepository
import com.jonpeps.gamescms.data.serialization.ICommonSerializationRepoHelper
import com.jonpeps.gamescms.data.serialization.StringListStatus
import com.jonpeps.gamescms.data.serialization.debug.IInputStreamSerializationRepoHelper
import com.jonpeps.gamescms.data.viewmodels.factories.InputStreamStringListViewModelFactory
import com.jonpeps.gamescms.data.repositories.IStringListItemsVmChangesCache
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.InputStream

@HiltViewModel(assistedFactory = InputStreamStringListViewModelFactory.IInputStreamStringListViewModelFactory::class)
    class InputStreamStringListViewModel@AssistedInject constructor(
    @Assisted("param1") private val inputStream: InputStream,
    @Assisted("param2") private val directory: String,
    private val commonSerializationRepoHelper: ICommonSerializationRepoHelper,
    private val inputStreamSerializationRepoHelper: IInputStreamSerializationRepoHelper,
    private val moshiStringListRepository: IMoshiStringListRepository,
    private val coroutineDispatcher: CoroutineDispatcher
): ViewModel() {
    var status: StringListStatus = StringListStatus(true, arrayListOf(), "", null)

    var items = arrayListOf<String>()
    var exception: Exception? = null

    fun load(cacheName: String, loadFromCacheIfExists: Boolean) {
        viewModelScope.launch(coroutineDispatcher) {
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
                    if (!commonSerializationRepoHelper.createDirectory(directory)) {
                        success = false
                        errorMessage = FAILED_TO_CREATE_DIR + directory
                    } else {
                        initWriteFiles(cacheName)
                        if (moshiStringListRepository.save(cacheName, StringListMoshi(items))) {
                            listItemsVmChangesCache.set(cacheName, items)
                        } else {
                            items.clear()
                            success = false
                            errorMessage = FAILED_TO_WRITE_FILE
                        }
                    }
                } catch (ex: Exception) {
                    items.clear()
                    exception = ex
                    errorMessage = ex.message.toString()
                    success = false
                }
            }
            status = StringListStatus(success, items, errorMessage, exception)
        }
    }

    private fun initReadFiles(inputStream: InputStream) {
        moshiStringListRepository.setBufferReader(
            inputStreamSerializationRepoHelper.getBufferReader(inputStream)
        )
    }

    private fun initWriteFiles(cacheName: String) {
        val fileName = cacheName + JSON_EXTENSION
        moshiStringListRepository.setAbsoluteFile(
            commonSerializationRepoHelper.getAbsoluteFile(directory, fileName))
        moshiStringListRepository.setFile(commonSerializationRepoHelper.getMainFile(fileName))
        moshiStringListRepository.setDirectoryFile(
            commonSerializationRepoHelper.getDirectoryFile(directory))
        moshiStringListRepository.setFileWriter(
            commonSerializationRepoHelper.getFileWriter(directory, fileName))
        moshiStringListRepository
            .setItem(cacheName, StringListMoshi(items))
    }

    companion object {
        const val FAILED_TO_LOAD_FILE = "Failed to load string list from assets!"
        const val FAILED_TO_WRITE_FILE = "Failed to write string list to local storage!"
        const val FAILED_TO_CREATE_DIR = "Failed to create directory: "
    }
}