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

    override suspend fun load(name: String,
                              path: String,
                              cacheName: String,
                              loadFromCacheIfExists: Boolean) {
        var dataItem: T? = null
        var success = true
        var message = ""
        var exception: Exception? = null
        if (loadFromCacheIfExists && cache.isPopulated()) {
            item = cache.get(cacheName)
            return
        } else {
            try {
                initReadFiles(name, path)
                if (repository.load()) {
                    dataItem = repository.getItem()
                    if (dataItem == null) {
                        success = false
                        message = JSON_ITEM_TO_SAVE_IS_NULL + name
                    }
                } else {
                    success = false
                    message = repository.getErrorMsg()
                }
            } catch (ex: Exception) {
                success = false
                message = ex.message.toString()
                exception = ex
            }
        }

        if (success) {
            item = CommonDataItem(true,
                dataItem,
                0,
                message,
                null)
            cache.set(cacheName, item!!)
        } else {
            item = CommonDataItem(false,
                null,
                0,
                message,
                exception)
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