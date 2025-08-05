package com.jonpeps.gamescms.data.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonpeps.gamescms.data.dataclasses.moshi.StringListMoshi
import com.jonpeps.gamescms.data.repositories.IMoshiStringListRepository
import com.jonpeps.gamescms.data.serialization.ICommonSerializationRepoHelper
import com.jonpeps.gamescms.data.serialization.StringListStatus
import com.jonpeps.gamescms.data.serialization.debug.IAssetSerializationRepoHelper
import com.jonpeps.gamescms.ui.tabletemplates.viewmodels.IStringListItemsVmChangesCache
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.InputStream

interface IAssetsStringListViewModel {
    fun loadFromAssets(cacheName: String, inputStream: InputStream)
}

class AssetsStringListViewModel@AssistedInject constructor(
    @Assisted("param2") private val stringListPath: String,
    private val commonSerializationRepoHelper: ICommonSerializationRepoHelper,
    private val assetSerializationRepoHelper: IAssetSerializationRepoHelper,
    private val moshiStringListRepository: IMoshiStringListRepository,
    private val listItemsVmChangesCache: IStringListItemsVmChangesCache,
    private val coroutineDispatcher: CoroutineDispatcher
): ViewModel(), IAssetsStringListViewModel {
    private val _status = MutableStateFlow(StringListStatus(true, arrayListOf(), "", null))
    val status: StateFlow<StringListStatus> = _status

    private var _isProcessing = MutableStateFlow(false)
    val isProcessing: StateFlow<Boolean> = _isProcessing

    private var items = arrayListOf<String>()
    private var exception: Exception? = null

    override fun loadFromAssets(cacheName: String, inputStream: InputStream) {
        viewModelScope.launch(coroutineDispatcher) {
            _isProcessing.value = true
            items.clear()
            exception = null
            var errorMessage = ""
            var success = true
            try {
                initReadFiles(inputStream)
                if (moshiStringListRepository.load(cacheName)) {
                    val stringList = moshiStringListRepository.getItem(cacheName)
                    if (stringList != null) {
                        items = ArrayList(stringList.list)
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
                        success = false
                    }
                } catch (ex: Exception) {
                    exception = ex
                    errorMessage = ex.message.toString()
                    success = false
                }
            }
            _isProcessing.value = false
            _status.value = StringListStatus(success, items, errorMessage, exception)
        }
    }

    private fun initReadFiles(inputStream: InputStream) {
        moshiStringListRepository.setBufferReader(
            assetSerializationRepoHelper.getBufferReader(inputStream)
        )
    }

    private fun initWriteFiles(cacheName: String) {
        moshiStringListRepository.setAbsoluteFile(
            commonSerializationRepoHelper.getAbsoluteFile(stringListPath, cacheName))
        moshiStringListRepository.setFile(commonSerializationRepoHelper.getMainFile(cacheName))
        moshiStringListRepository.setDirectoryFile(
            commonSerializationRepoHelper.getDirectoryFile(stringListPath))
        moshiStringListRepository.setFileWriter(
            commonSerializationRepoHelper.getFileWriter(stringListPath, cacheName))
        moshiStringListRepository
            .setItem(cacheName, StringListMoshi(items))
    }

    companion object {
        const val FAILED_TO_LOAD_FILE = "Failed to load project list from assets!"
    }
}