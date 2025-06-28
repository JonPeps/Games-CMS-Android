package com.jonpeps.gamescms.data.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonpeps.gamescms.data.dataclasses.moshi.StringListMoshi
import com.jonpeps.gamescms.data.repositories.IMoshiStringListRepository
import com.jonpeps.gamescms.data.serialization.ICommonDeleteFileHelper
import com.jonpeps.gamescms.data.serialization.ICommonSerializationRepoHelper
import com.jonpeps.gamescms.data.serialization.SubDeleteFlag
import com.jonpeps.gamescms.data.viewmodels.factories.ListViewModelFactory
import com.jonpeps.gamescms.ui.tabletemplates.viewmodels.IStringListItemsVmChangesCache
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class StringListStatus(
    val success: Boolean,
    val items: ArrayList<String>,
    val message: String?,
    val ex: Exception?)



interface ICommonStringListViewModel {
    fun load(cacheName: String, loadFromCacheIfExists: Boolean = true)
    fun add(name: String)
    fun delete(name: String, subDeleteFlag: SubDeleteFlag = SubDeleteFlag.NONE)
}

@HiltViewModel(assistedFactory = ListViewModelFactory.ICommonStringListViewModelFactory::class)
class CommonStringListViewModel
@AssistedInject constructor(
    @Assisted("param1") private val directoryOrFilesPath: String,
    @Assisted("param2") private val stringListPath: String,
    private val moshiStringListRepository: IMoshiStringListRepository,
    private val commonSerializationRepoHelper: ICommonSerializationRepoHelper,
    private val listItemsVmChangesCache: IStringListItemsVmChangesCache,
    private val commonDeleteFileHelper: ICommonDeleteFileHelper,
    private val coroutineDispatcher: CoroutineDispatcher
): ViewModel(), ICommonStringListViewModel {
    private val _status = MutableStateFlow(StringListStatus(true, arrayListOf(), "", null))
    val status: StateFlow<StringListStatus> = _status

    private var _isProcessing = MutableStateFlow(false)
    val isProcessing: StateFlow<Boolean> = _isProcessing

    private var items = arrayListOf<String>()
    private var cacheName = ""
    private var exception: Exception? = null

    override fun load(cacheName: String, loadFromCacheIfExists: Boolean) {
        this.cacheName = cacheName
        viewModelScope.launch(coroutineDispatcher) {
            _isProcessing.value = true
            items.clear()
            exception = null
            var errorMessage = ""
            var success = true
            if (loadFromCacheIfExists && listItemsVmChangesCache.isPopulated()) {
                items = ArrayList(listItemsVmChangesCache.get(cacheName))
            } else {
                try {
                    initReadFiles()
                    if (moshiStringListRepository.load(cacheName)) {
                        val stringList = moshiStringListRepository.getItem(cacheName)
                        if (stringList != null) {
                            items = ArrayList(stringList.list)
                        } else {
                            success = false
                            errorMessage = FAILED_TO_LOAD_FILE + stringListPath
                        }
                    } else {
                        success = false
                        errorMessage = FAILED_TO_LOAD_FILE + stringListPath
                    }
                } catch (ex: Exception) {
                    exception = ex
                    errorMessage = ex.message.toString()
                    success = false
                }
                if (success) {
                    listItemsVmChangesCache.set(cacheName, items)
                }
            }
            _isProcessing.value = false
            _status.value = StringListStatus(success, items, errorMessage, exception)
        }
    }

    override fun add(name: String) {
        viewModelScope.launch(coroutineDispatcher) {
            _isProcessing.value = true
            initWriteFiles(name)
            items.add(name)
            var success = true
            if (moshiStringListRepository.save(cacheName, StringListMoshi(items))) {
                listItemsVmChangesCache.set(cacheName, items)
            } else {
                success = false
                items.remove(name)
            }
            _isProcessing.value = false
            _status.value = StringListStatus(success, items, if (success) "" else FAILED_TO_SAVE_FILE + name, null)
        }
    }

    override fun delete(name: String, subDeleteFlag: SubDeleteFlag) {
        viewModelScope.launch(coroutineDispatcher) {
            _isProcessing.value = true
            initWriteFiles(name)
            items.remove(name)
            var success = true
            var message = ""
            if (moshiStringListRepository.delete(stringListPath, name)) {
                if (moshiStringListRepository.save(cacheName, StringListMoshi(items))) {
                    if (commonDeleteFileHelper.onSubDelete(directoryOrFilesPath, name, subDeleteFlag)) {
                        listItemsVmChangesCache.set(cacheName, items)
                    } else {
                        success = false
                        message = FAILED_TO_DELETE_FILE_OR_DIRECTORY + name
                    }
                } else {
                    success = false
                    message = FAILED_TO_SAVE_FILE + name
                }
            } else {
                success = false
                message = FAILED_TO_DELETE_FILE + name
            }
            _isProcessing.value = false
            _status.value = StringListStatus(success, items, message, null)
        }
    }

    private fun initReadFiles() {
        moshiStringListRepository.setAbsoluteFile(
            commonSerializationRepoHelper.getAbsoluteFile(stringListPath, ""))
        moshiStringListRepository.setBufferReader(
            commonSerializationRepoHelper.getBufferReader(stringListPath, ""))
    }

    private fun initWriteFiles(name: String) {
        moshiStringListRepository.setAbsoluteFile(
            commonSerializationRepoHelper.getAbsoluteFile(stringListPath, name))
        moshiStringListRepository.setFile(commonSerializationRepoHelper.getMainFile(name))
        moshiStringListRepository.setDirectoryFile(
            commonSerializationRepoHelper.getDirectoryFile(stringListPath))
        moshiStringListRepository.setFileWriter(
            commonSerializationRepoHelper.getFileWriter(stringListPath, name))
        moshiStringListRepository
            .setItem(cacheName, StringListMoshi(items))
    }

    companion object {
        const val FAILED_TO_LOAD_FILE = "Failed to load file: "
        const val FAILED_TO_DELETE_FILE = "Failed to delete file: "
        const val FAILED_TO_DELETE_FILE_OR_DIRECTORY = "Failed to delete file/directory: "
        const val FAILED_TO_SAVE_FILE = "Failed to save file: "
    }
}