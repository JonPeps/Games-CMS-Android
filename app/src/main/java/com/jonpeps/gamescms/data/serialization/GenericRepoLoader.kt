package com.jonpeps.gamescms.data.serialization

import androidx.annotation.VisibleForTesting
import com.jonpeps.gamescms.data.dataclasses.CommonDataItem
import com.jonpeps.gamescms.data.helpers.IGenericSerializationCache
import com.jonpeps.gamescms.data.repositories.base.ISingleItemMoshiJsonRepository

interface IGenericRepoLoader<T,K> {
    suspend fun load(name: String,
                     path: String,
                     cacheName: String,
                     loadFromCacheIfExists: Boolean)

    fun getItem(): CommonDataItem<K>?
}

abstract class GenericRepoLoader<T,K>(
    private val repository: ISingleItemMoshiJsonRepository<T>,
    private val repoHelper: ICommonSerializationRepoHelper,
    private val cache: IGenericSerializationCache<K>
): IGenericRepoLoader<T,K> {

    private var finalItem: CommonDataItem<K>? = null

    override suspend fun load(name: String,
                              path: String,
                              cacheName: String,
                              loadFromCacheIfExists: Boolean) {
        var success = true
        var message = ""
        var exception: Exception? = null
        var parsedValue: K? = null
        if (loadFromCacheIfExists && cache.isPopulated()) {
            finalItem = CommonDataItem(true,
                cache.get(cacheName),
                0,
                "",
                null)
            return
        } else {
            try {
                initReadFiles(name, path)
                if (repository.load()) {
                    val dataItem = repository.getItem()
                    if (dataItem == null) {
                        success = false
                        message = JSON_ITEM_TO_SAVE_IS_NULL + name
                    } else {
                        parsedValue = getParsedValue(dataItem)
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
            finalItem = CommonDataItem(true,
                parsedValue,
                0,
                "",
                null)
            cache.set(cacheName, parsedValue!!)
        } else {
            finalItem = CommonDataItem(false,
                null,
                0,
                message,
                exception)
        }
    }

    override fun getItem(): CommonDataItem<K>? = finalItem

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    internal abstract fun getParsedValue(item: T): K?

    private fun initReadFiles(name: String, path: String) {
        repository.setAbsoluteFile(repoHelper.getAbsoluteFile(path, name))
        repository.setBufferReader(repoHelper.getBufferReader(path, name))
    }

    companion object {
        const val JSON_ITEM_TO_SAVE_IS_NULL = "Json item to save is null: "
    }
}