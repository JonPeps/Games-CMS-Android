package com.jonpeps.gamescms.data.repositories

import com.jonpeps.gamescms.data.dataclasses.moshi.StringListMoshi
import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateDetailsListMoshi
import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateItemListMoshi
import com.jonpeps.gamescms.data.repositories.base.CachedMoshiJsonRepository
import com.jonpeps.gamescms.data.repositories.base.SingleItemMoshiJsonRepository
import com.jonpeps.gamescms.data.repositories.base.IBaseCachedMoshiJsonRepository
import com.jonpeps.gamescms.data.repositories.base.ISingleItemMoshiJsonRepository
import com.jonpeps.gamescms.data.serialization.string.IStringFileStorageStrSerialisation
import javax.inject.Inject

interface IMoshiStringListRepository :
    ISingleItemMoshiJsonRepository<StringListMoshi>

interface ICachedMoshiStringListRepository :
    IBaseCachedMoshiJsonRepository<StringListMoshi>

interface IMoshiTableTemplateRepository :
    ISingleItemMoshiJsonRepository<TableTemplateItemListMoshi>

interface IMoshiTableTemplateDetailsListRepository :
    ISingleItemMoshiJsonRepository<TableTemplateDetailsListMoshi>

interface IMoshiJsonRepositoryFactory {
    fun moshiStringListRepository()
        : SingleItemMoshiJsonRepository<StringListMoshi>
    fun moshiCachedStrListRepository()
        : ICachedMoshiStringListRepository
    fun moshiTableTemplateRepository()
        : SingleItemMoshiJsonRepository<TableTemplateItemListMoshi>
    fun moshiTableTemplateDetailsListRepository()
        : SingleItemMoshiJsonRepository<TableTemplateDetailsListMoshi>
}

class MoshiJsonRepositoryFactoryImpl @Inject constructor(
    private val moshiJsonAdapterFactory: IMoshiJsonAdapterFactory,
    private val stringFileStorageStrSerialisation: IStringFileStorageStrSerialisation,
    private val tableTemplateStringMoshiJsonCache: IStringListMoshiJsonCache
) : IMoshiJsonRepositoryFactory {

    override fun moshiStringListRepository()
        : SingleItemMoshiJsonRepository<StringListMoshi> {
        return SingleItemMoshiJsonRepository(
            moshiJsonAdapterFactory.jsonStringList(),
            stringFileStorageStrSerialisation
        )
    }

    override fun moshiCachedStrListRepository(): ICachedMoshiStringListRepository {
        return object : CachedMoshiJsonRepository<StringListMoshi>(
            moshiJsonAdapterFactory.jsonStringList(),
            stringFileStorageStrSerialisation,
            tableTemplateStringMoshiJsonCache
        ), ICachedMoshiStringListRepository {}
    }

    override fun moshiTableTemplateRepository()
        : SingleItemMoshiJsonRepository<TableTemplateItemListMoshi> {
        return SingleItemMoshiJsonRepository(
            moshiJsonAdapterFactory.jsonTableTemplateItemList(),
            stringFileStorageStrSerialisation
        )
    }

    override fun moshiTableTemplateDetailsListRepository()
        : SingleItemMoshiJsonRepository<TableTemplateDetailsListMoshi> {
        return SingleItemMoshiJsonRepository(
            moshiJsonAdapterFactory.jsonTableTemplateDetailsList(),
            stringFileStorageStrSerialisation
        )
    }
}