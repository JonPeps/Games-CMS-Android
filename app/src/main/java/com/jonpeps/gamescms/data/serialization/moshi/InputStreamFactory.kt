package com.jonpeps.gamescms.data.serialization.moshi

import com.jonpeps.gamescms.data.dataclasses.moshi.StringListMoshi
import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateDetailsListMoshi
import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateItemListMoshi
import com.jonpeps.gamescms.data.repositories.base.ISingleItemMoshiJsonRepository
import com.jonpeps.gamescms.data.serialization.ICommonSerializationRepoHelper
import com.jonpeps.gamescms.data.serialization.debug.IInputStreamSerializationRepoHelper
import javax.inject.Inject

interface IInputStreamFactory {
    fun inputStreamToStringListStorage()
        : ISToJsonTypeToStorage<StringListMoshi>
    fun inputStreamToTemplateListStorage()
        : ISToJsonTypeToStorage<TableTemplateItemListMoshi>
    fun inputStreamToTemplateDetailsListStorage()
        : ISToJsonTypeToStorage<TableTemplateDetailsListMoshi>
}

class InputStreamFactoryImpl @Inject constructor(
    private val moshiStringListRepository
        : ISingleItemMoshiJsonRepository<StringListMoshi>,
    private val moshiTableTemplateRepository
        : ISingleItemMoshiJsonRepository<TableTemplateItemListMoshi>,
    private val moshiTableTemplateDetailsListRepository
        : ISingleItemMoshiJsonRepository<TableTemplateDetailsListMoshi>,
    private val commonSerializationRepoHelper
        : ICommonSerializationRepoHelper,
    private val inputStreamSerializationRepoHelper
        : IInputStreamSerializationRepoHelper
): IInputStreamFactory {

    override fun inputStreamToStringListStorage()
        : ISToJsonTypeToStorage<StringListMoshi> {
            return ISToJsonTypeToStorage(
                moshiStringListRepository,
                commonSerializationRepoHelper,
                inputStreamSerializationRepoHelper
            )
    }

    override fun inputStreamToTemplateListStorage()
        : ISToJsonTypeToStorage<TableTemplateItemListMoshi> {
            return ISToJsonTypeToStorage(
                moshiTableTemplateRepository,
                commonSerializationRepoHelper,
                inputStreamSerializationRepoHelper)
    }

    override fun inputStreamToTemplateDetailsListStorage()
        : ISToJsonTypeToStorage<TableTemplateDetailsListMoshi> {
            return ISToJsonTypeToStorage(
                moshiTableTemplateDetailsListRepository,
                commonSerializationRepoHelper,
                inputStreamSerializationRepoHelper)
    }
}