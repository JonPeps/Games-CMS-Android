package com.jonpeps.gamescms.data.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonpeps.gamescms.data.dataclasses.moshi.StringListMoshi
import com.jonpeps.gamescms.data.repositories.IMoshiStringListRepository
import com.jonpeps.gamescms.data.serialization.ICommonSerializationRepoHelper
import com.jonpeps.gamescms.data.viewmodels.factories.ListViewModelFactory
import com.jonpeps.gamescms.ui.tabletemplates.viewmodels.IStringListItemsVmChangesCache
import com.jonpeps.gamescms.ui.tabletemplates.viewmodels.TableTemplateGroupViewModel.Companion.JSON_ITEM_TO_LOAD_IS_NULL
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
    fun load(cacheName: String, pathToList: String, loadFromCacheIfExists: Boolean = true)
    fun add(name: String)
    fun delete(pathToFile: String, name: String)
}

@HiltViewModel(assistedFactory = ListViewModelFactory.ICommonStringListViewModelFactory::class)
class CommonStringListViewModel
@AssistedInject constructor(
    @Assisted private val stringListPath: String,
    private val moshiStringListRepository: IMoshiStringListRepository,
    private val commonSerializationRepoHelper: ICommonSerializationRepoHelper,
    private val listItemsVmChangesCache: IStringListItemsVmChangesCache,
    private val coroutineDispatcher: CoroutineDispatcher
): ViewModel(), ICommonStringListViewModel {
    private val _status = MutableStateFlow(StringListStatus(true, arrayListOf(), "", null))
    val status: StateFlow<StringListStatus> = _status

    private var _isProcessing = MutableStateFlow(false)
    val isProcessing: StateFlow<Boolean> = _isProcessing

    private var items = arrayListOf<String>()
    private var index = 0
    private var cacheName = ""
    private var exception: Exception? = null


    override fun load(cacheName: String, pathToList: String, loadFromCacheIfExists: Boolean) {
        this.cacheName = cacheName
        viewModelScope.launch(coroutineDispatcher) {
            _isProcessing.value = true
            exception = null
            var errorMessage = ""
            var success = true
            if (loadFromCacheIfExists && listItemsVmChangesCache.isPopulated()) {
                items = ArrayList(listItemsVmChangesCache.get(cacheName))
            } else {
                try {
                    initReadFiles(pathToList)
                    if (moshiStringListRepository.load(pathToList)) {
                        val stringList = moshiStringListRepository.getItem(pathToList)
                        if (stringList != null) {
                            items = ArrayList(stringList.list)
                        } else {
                            success = false
                            errorMessage = JSON_ITEM_TO_LOAD_IS_NULL + pathToList
                        }
                    } else {
                        success = false
                        errorMessage = JSON_ITEM_TO_LOAD_IS_NULL + pathToList
                    }
                } catch (ex: Exception) {
                    exception = ex
                    errorMessage = ex.message.toString()
                    success = false
                }
                if (success) {
                    listItemsVmChangesCache.set(cacheName, items)
                }
                _status.value = StringListStatus(success, items, errorMessage, exception)
            }
        }
    }

    override fun add(name: String) {
        TODO("Not yet implemented")
    }

    override fun delete(pathToFile: String, name: String) {
        TODO("Not yet implemented")
    }

    private fun initReadFiles(name: String) {
        moshiStringListRepository.setAbsoluteFile(
            commonSerializationRepoHelper.getAbsoluteFile(stringListPath, name))
        moshiStringListRepository.setBufferReader(
            commonSerializationRepoHelper.getBufferReader(stringListPath, name))
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
}