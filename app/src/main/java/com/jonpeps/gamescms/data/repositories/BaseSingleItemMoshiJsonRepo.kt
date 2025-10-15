package com.jonpeps.gamescms.data.repositories

import com.jonpeps.gamescms.data.serialization.string.IStringFileStorageStrSerialisation

interface IBaseSingleItemMoshiJsonRepository<T>: IBaseMoshiRepo {
    suspend fun load(): Boolean
    suspend fun serialize(contents: String): Boolean
    suspend fun save(item: T): Boolean

    fun getItem(): T?
    fun setItem(item: T?)
}

abstract class BaseSingleItemMoshiJsonRepository<T>(
    private val moshiJsonAdapter: MoshiJsonAdapter<T>,
    private val stringFileStorageStrSerialisation: IStringFileStorageStrSerialisation,
): BaseMoshiRepo(), IBaseSingleItemMoshiJsonRepository<T>  {
    private var item: T? = null

    override suspend fun load(): Boolean {
        errorMessage = ""
        var success = stringFileStorageStrSerialisation
            .read(mainBufferedReader)
        if (success) {
            val contents = stringFileStorageStrSerialisation.getContents()
            item = moshiJsonAdapter.getJsonAdapter().fromJson(contents)
            if(item == null) {
                errorMessage = EMPTY_JSON_CONTENTS
                success = false
            }
        } else {
            errorMessage = stringFileStorageStrSerialisation.getErrorMsg()
            success = false
        }
        return success
    }

    override suspend fun save(item: T): Boolean {
        errorMessage = ""
        var success = false
        val jsonContents = moshiJsonAdapter.getJsonAdapter().toJson(item)
        if (jsonContents.isNullOrEmpty()) {
            errorMessage = CONVERT_TO_JSON_FAILED
        } else {
            success = stringFileStorageStrSerialisation.write(directoryFile,
                mainFile,
                mainAbsolutePath,
                mainFileWriter,
                jsonContents)
            if (!success) {
                errorMessage = WRITE_TO_FILE_FAILED
            }
        }
        return success
    }

    override suspend fun serialize(contents: String): Boolean {
        errorMessage = ""
        val jsonAdapter = moshiJsonAdapter.getJsonAdapter()
        item = jsonAdapter.fromJson(contents)
        return if (item != null) {
            true
        } else {
            errorMessage = EMPTY_JSON_CONTENTS
            false
        }
    }

    override fun setItem(item: T?) {
        this.item = item
    }

    override fun getItem(): T? = item
}