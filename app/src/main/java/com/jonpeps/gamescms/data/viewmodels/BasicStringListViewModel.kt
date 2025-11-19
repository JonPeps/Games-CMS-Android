package com.jonpeps.gamescms.data.viewmodels

import androidx.lifecycle.viewModelScope
import com.jonpeps.gamescms.data.DataConstants.Companion.FILE_EXTENSION
import com.jonpeps.gamescms.data.dataclasses.moshi.StringListMoshi
import com.jonpeps.gamescms.data.repositories.ICachedMoshiStringListRepository
import com.jonpeps.gamescms.data.serialization.ICommonDeleteFileHelper
import com.jonpeps.gamescms.data.serialization.ICommonSerializationRepoHelper
import com.jonpeps.gamescms.data.serialization.StringListStatus
import com.jonpeps.gamescms.data.serialization.SubDeleteFlag
import com.jonpeps.gamescms.data.viewmodels.factories.BasicStringListViewModelFactory
import com.jonpeps.gamescms.data.helpers.IStringListItemsVmChangesCache
import com.jonpeps.gamescms.data.viewmodels.BasicStringListViewModel.Companion.NO_CACHE_NAME
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

interface IBasicStringListViewModel {
    fun load(cacheName: String = NO_CACHE_NAME, loadFromCacheIfExists: Boolean = true)
    fun add(name: String)
    fun delete(name: String, directory: String, subDeleteFlag: SubDeleteFlag = SubDeleteFlag.NONE)
}

@HiltViewModel(assistedFactory = BasicStringListViewModelFactory.IBasicStringListViewModelFactory::class)
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
        _isProcessing.value = true
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
            _isProcessing.value = false
            status = StringListStatus(success, items, errorMessage, exception)
        }
    }

    override fun add(name: String) {
        if (items.contains(name)) return
        _isProcessing.value = true
        viewModelScope.launch(coroutineDispatcher) {
            items.add(name)
            listItemsVmChangesCache.set(cacheName, items)
            save()
        }
    }

    override fun delete(name: String, directory: String, subDeleteFlag: SubDeleteFlag) {
        _isProcessing.value = true
        viewModelScope.launch(coroutineDispatcher) {
            items.remove(name)
            when (subDeleteFlag) {
                SubDeleteFlag.DIRECTORY_AND_FILES -> {
                    commonDeleteFileHelper.deleteDirectory(directory)
                }
                SubDeleteFlag.FILE -> {
                    commonDeleteFileHelper.deleteFile(directory, name)
                }
                SubDeleteFlag.NONE -> {}
            }
            save()
            listItemsVmChangesCache.set(cacheName, items)
            _isProcessing.value = false
        }
    }

    private suspend fun save() {
            initWriteFiles(fileName)
            val success =
                moshiStringListRepository.save(
                    cacheName, StringListMoshi(items))
            _isProcessing.value = false
            status = StringListStatus(
                success,
                items,
                if (success) "" else FAILED_TO_SAVE_FILE + fileName,
                null
            )
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
        const val NO_CACHE_NAME = ""
        const val FAILED_TO_LOAD_FILE = "Failed to load file: "
        const val FAILED_TO_SAVE_FILE = "Failed to save file: "
    }
}