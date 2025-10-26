package com.jonpeps.gamescms.data.viewmodels

import androidx.lifecycle.viewModelScope
import com.jonpeps.gamescms.data.dataclasses.moshi.StringListMoshi
import com.jonpeps.gamescms.data.repositories.ICachedMoshiStringListRepository
import com.jonpeps.gamescms.data.serialization.ICommonDeleteFileHelper
import com.jonpeps.gamescms.data.serialization.ICommonSerializationRepoHelper
import com.jonpeps.gamescms.data.serialization.StringListStatus
import com.jonpeps.gamescms.data.serialization.SubDeleteFlag
import com.jonpeps.gamescms.data.viewmodels.factories.StartFlowStringListViewModelFactory
import com.jonpeps.gamescms.data.helpers.IStringListItemsVmChangesCache
import com.jonpeps.gamescms.data.viewmodels.BasicStringListViewModel.Companion.NO_CACHE_NAME
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import java.io.File

interface IBasicStringListViewModel {
    fun load(cacheName: String = NO_CACHE_NAME, loadFromCacheIfExists: Boolean = true)
    fun add(name: String)
    fun delete(name: String, subDeleteFlag: SubDeleteFlag = SubDeleteFlag.NONE)
    fun deleteAll()
}

@HiltViewModel(assistedFactory = StartFlowStringListViewModelFactory.IStartFlowStringListViewModelFactory::class)
class BasicStringListViewModel
@AssistedInject constructor(
    @Assisted("param1") private val directory: String,
    @Assisted("param2") private val fileName: String,
    private val moshiStringListRepository: ICachedMoshiStringListRepository,
    private val commonSerializationRepoHelper: ICommonSerializationRepoHelper,
    private val listItemsVmChangesCache: IStringListItemsVmChangesCache,
    private val commonDeleteFileHelper: ICommonDeleteFileHelper,
    private val coroutineDispatcher: CoroutineDispatcher
): BaseStringListViewModel(), IBasicStringListViewModel {
    private var cacheName = ""

    override fun load(cacheName: String, loadFromCacheIfExists: Boolean) {
        this.cacheName = cacheName
        viewModelScope.launch(coroutineDispatcher) {
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
                            items = ArrayList(stringList.items)
                        } else {
                            success = false
                            errorMessage = FAILED_TO_LOAD_FILE + fileName
                        }
                    } else {
                        success = false
                        errorMessage = FAILED_TO_LOAD_FILE + fileName
                    }
                } catch (ex: Exception) {
                    exception = ex
                    errorMessage = ex.message.toString()
                    success = false
                }
                if (cacheName != NO_CACHE_NAME && success) {
                    listItemsVmChangesCache.set(cacheName, items)
                }
            }
            baseHasFinishedObtainingData.value = true
            status = StringListStatus(success, items, errorMessage, exception)
        }
    }

    override fun add(name: String) {
        viewModelScope.launch(coroutineDispatcher) {
            baseHasFinishedObtainingData.value = false
            initWriteFiles(name)
            items.add(name)
            var success = true
            if (moshiStringListRepository.save(cacheName, StringListMoshi(items))) {
                listItemsVmChangesCache.set(cacheName, items)
            } else {
                success = false
                items.remove(name)
            }
            status = StringListStatus(success, items, if (success) "" else FAILED_TO_SAVE_FILE + name, null)
        }
    }

    override fun delete(name: String, subDeleteFlag: SubDeleteFlag) {
        viewModelScope.launch(coroutineDispatcher) {
            baseHasFinishedObtainingData.value = false
            initWriteFiles(name)
            items.remove(name)
            var success = true
            var message = ""
            if (moshiStringListRepository.delete(fileName, name)) {
                if (moshiStringListRepository.save(cacheName, StringListMoshi(items))) {
                    if (commonDeleteFileHelper.onSubDelete(directory, name, subDeleteFlag)) {
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
            status = StringListStatus(success, items, message, null)
        }
    }

    override fun deleteAll() {
        viewModelScope.launch(coroutineDispatcher) {
            commonDeleteFileHelper.deleteDirectory(File(directory))
        }
    }

    private fun initReadFiles() {
        val totalFilename = fileName + FILE_EXTENSION
        moshiStringListRepository.setAbsoluteFile(
            commonSerializationRepoHelper.getAbsoluteFile(directory, totalFilename))
        moshiStringListRepository.setBufferReader(
            commonSerializationRepoHelper.getBufferReader(directory, totalFilename))
    }

    private fun initWriteFiles(name: String) {
        moshiStringListRepository.setAbsoluteFile(
            commonSerializationRepoHelper.getAbsoluteFile(directory, name))
        moshiStringListRepository.setFile(commonSerializationRepoHelper.getMainFile(name))
        moshiStringListRepository.assignDirectoryFile(
            commonSerializationRepoHelper.getDirectoryFile(directory))
        moshiStringListRepository.setFileWriter(
            commonSerializationRepoHelper.getFileWriter(directory, name))
        moshiStringListRepository
            .setItem(cacheName, StringListMoshi(items))
    }

    companion object {
        const val FILE_EXTENSION = ".json"
        const val NO_CACHE_NAME = ""
        const val FAILED_TO_LOAD_FILE = "Failed to load file: "
        const val FAILED_TO_DELETE_FILE = "Failed to delete file: "
        const val FAILED_TO_DELETE_FILE_OR_DIRECTORY = "Failed to delete file/directory: "
        const val FAILED_TO_SAVE_FILE = "Failed to save file: "
    }
}