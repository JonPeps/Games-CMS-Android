package com.jonpeps.gamescms.data.repositories

import com.jonpeps.gamescms.data.dataclasses.moshi.StringListMoshi
import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateDetailsListMoshi
import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateItemListMoshi
import com.jonpeps.gamescms.data.repositories.base.CachedMoshiJsonRepository
import com.jonpeps.gamescms.data.repositories.base.ISingleItemMoshiJsonRepository
import com.jonpeps.gamescms.data.repositories.base.SingleItemMoshiJsonRepository
import com.jonpeps.gamescms.data.serialization.string.IStringFileStorageStrSerialisation
import javax.inject.Inject

interface IMoshiJsonRepositoryFactory {
    fun moshiStringListRepository()
        : ISingleItemMoshiJsonRepository<StringListMoshi>
    fun moshiCachedStrListRepository()
        : CachedMoshiJsonRepository<StringListMoshi>
    fun moshiTableTemplateRepository()
        : ISingleItemMoshiJsonRepository<TableTemplateItemListMoshi>
    fun moshiTableTemplateDetailsListRepository()
        : ISingleItemMoshiJsonRepository<TableTemplateDetailsListMoshi>
}

class MoshiJsonRepositoryFactoryImpl @Inject constructor(
    private val moshiJsonAdapterFactory
        : IMoshiJsonAdapterFactory,
    private val stringFileStorageStrSerialisation
        : IStringFileStorageStrSerialisation,
    private val stringListMoshiJsonCache
        : IStringListMoshiJsonCache
) : IMoshiJsonRepositoryFactory {

    override fun moshiStringListRepository()
        : SingleItemMoshiJsonRepository<StringListMoshi> {
            return SingleItemMoshiJsonRepository(
                moshiJsonAdapterFactory.jsonStringList(),
                stringFileStorageStrSerialisation
            )
    }

    override fun moshiCachedStrListRepository()
        : CachedMoshiJsonRepository<StringListMoshi> {
            return CachedMoshiJsonRepository(
                moshiJsonAdapterFactory.jsonStringList(),
                stringFileStorageStrSerialisation,
                stringListMoshiJsonCache
            )
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