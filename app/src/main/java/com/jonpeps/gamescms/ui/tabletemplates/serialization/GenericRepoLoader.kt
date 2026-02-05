package com.jonpeps.gamescms.ui.tabletemplates.serialization

import com.jonpeps.gamescms.data.dataclasses.CommonDataItem
import com.jonpeps.gamescms.data.helpers.IGenericSerializationCache
import com.jonpeps.gamescms.data.repositories.IBaseSingleItemMoshiJsonRepository
import com.jonpeps.gamescms.data.serialization.ICommonSerializationRepoHelper

interface IGenericRepoLoader<T> {
    suspend fun load(name: String,
                     path: String,
                     cacheName: String,
                     loadFromCacheIfExists: Boolean)

    fun getItem(): CommonDataItem<T>?
}

open class GenericRepoLoader<T>(
    private val repository: IBaseSingleItemMoshiJsonRepository<T>,
    private val repoHelper: ICommonSerializationRepoHelper,
    private val cache: IGenericSerializationCache<CommonDataItem<T>>): IGenericRepoLoader<T> {

    private var item: CommonDataItem<T>? = null
    private var exception: Exception? = null

    override suspend fun load(name: String,
                              path: String,
                              cacheName: String,
                              loadFromCacheIfExists: Boolean) {
        var dataItem: T? = null
        exception = null
        var errorMessage = ""
        var success = true
        if (loadFromCacheIfExists && cache.isPopulated()) {
            item = cache.get(cacheName)
        } else {
            try {
                initReadFiles(name, path)
                if (repository.load()) {
                    dataItem = repository.getItem()
                    dataItem?.let {
                        item = CommonDataItem(true,
                            dataItem,
                            0,
                            errorMessage,
                            exception)
                    }?:run {
                        success = false
                        errorMessage = JSON_ITEM_TO_SAVE_IS_NULL + name
                    }
                }
            } catch (ex: Exception) {
                exception = ex
                errorMessage = ex.message.toString()
                success = false
            }
        }

        if (!loadFromCacheIfExists && success) {
            item = CommonDataItem(true, dataItem, 0, errorMessage, exception)
            cache.set(cacheName, item!!)
        }
    }

    override fun getItem(): CommonDataItem<T>? = item

    private fun initReadFiles(name: String, path: String) {
        repository.setAbsoluteFile(repoHelper.getAbsoluteFile(path, name))
        repository.setBufferReader(repoHelper.getBufferReader(path, name))
    }

    companion object {
        const val JSON_ITEM_TO_SAVE_IS_NULL = "Json item to save is null: "
    }
}