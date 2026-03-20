package com.jonpeps.gamescms.data.repositories

import com.jonpeps.gamescms.data.dataclasses.moshi.StringListMoshi
import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateDetailsListMoshi
import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateItemListMoshi
import com.jonpeps.gamescms.data.repositories.base.BaseCachedMoshiJsonRepository
import com.jonpeps.gamescms.data.repositories.base.BaseSingleItemMoshiJsonRepository
import com.jonpeps.gamescms.data.repositories.base.IBaseCachedMoshiJsonRepository
import com.jonpeps.gamescms.data.repositories.base.IBaseSingleItemMoshiJsonRepository
import com.jonpeps.gamescms.data.repositories.base.MoshiJsonAdapter
import com.jonpeps.gamescms.data.serialization.string.IStringFileStorageStrSerialisation
import javax.inject.Inject

interface IMoshiStringListRepository :
    IBaseSingleItemMoshiJsonRepository<StringListMoshi>

interface ICachedMoshiStringListRepository :
    IBaseCachedMoshiJsonRepository<StringListMoshi>

interface IMoshiTableTemplateRepository :
    IBaseSingleItemMoshiJsonRepository<TableTemplateItemListMoshi>

interface IMoshiTableTemplateDetailsListRepository :
    IBaseSingleItemMoshiJsonRepository<TableTemplateDetailsListMoshi>

interface MoshiJsonRepositoryFactory {
    fun moshiStringListRepository(): IMoshiStringListRepository
    fun moshiCachedStrListRepository(): ICachedMoshiStringListRepository
    fun moshiTableTemplateRepository(): IMoshiTableTemplateRepository
    fun moshiTableTemplateDetailsListRepository(): IMoshiTableTemplateDetailsListRepository
}

class MoshiJsonRepositoryFactoryImpl
@Inject constructor(
    private val moshiJsonAdapterFactory: IMoshiJsonAdapterFactory,
    private val stringFileStorageStrSerialisation: IStringFileStorageStrSerialisation,
    private val tableTemplateStringMoshiJsonCache: IStringListMoshiJsonCache
    ) : MoshiJsonRepositoryFactory {

    override fun moshiStringListRepository(): IMoshiStringListRepository
        = MoshiStringListRepository(
        moshiJsonAdapterFactory
            .jsonStringList(),
            stringFileStorageStrSerialisation)

    override fun moshiCachedStrListRepository(): ICachedMoshiStringListRepository
        = CachedMoshiStringListRepository(
            moshiJsonAdapterFactory
                .jsonStringList(),
            tableTemplateStringMoshiJsonCache,
            stringFileStorageStrSerialisation
        )

    override fun moshiTableTemplateRepository(): IMoshiTableTemplateRepository
        = MoshiTableTemplateRepository(
            moshiJsonAdapterFactory
                .jsonTableTemplateItemList(),
            stringFileStorageStrSerialisation
        )

    override fun moshiTableTemplateDetailsListRepository()
        : IMoshiTableTemplateDetailsListRepository
            = MoshiTableTemplateDetailsListRepository(
            moshiJsonAdapterFactory
                .jsonTableTemplateDetailsList(),
            stringFileStorageStrSerialisation
            )

    class MoshiStringListRepository(stringListMoshiJsonAdapter: MoshiJsonAdapter<StringListMoshi>,
                                    stringFileStorageStrSerialisation: IStringFileStorageStrSerialisation
    ) : BaseSingleItemMoshiJsonRepository<StringListMoshi>(stringListMoshiJsonAdapter,
        stringFileStorageStrSerialisation), IMoshiStringListRepository

    class CachedMoshiStringListRepository(stringListMoshiJsonAdapter: MoshiJsonAdapter<StringListMoshi>,
                                          tableTemplateStringMoshiJsonCache: IStringListMoshiJsonCache,
                                          stringFileStorageStrSerialisation: IStringFileStorageStrSerialisation
    ) : BaseCachedMoshiJsonRepository<StringListMoshi>(stringListMoshiJsonAdapter,
        stringFileStorageStrSerialisation,
        tableTemplateStringMoshiJsonCache), ICachedMoshiStringListRepository

    class MoshiTableTemplateRepository(tableTemplateItemListMoshiAdapter: MoshiJsonAdapter<TableTemplateItemListMoshi>,
                                       stringFileStorageStrSerialisation: IStringFileStorageStrSerialisation)
        : BaseSingleItemMoshiJsonRepository<TableTemplateItemListMoshi>(tableTemplateItemListMoshiAdapter,
        stringFileStorageStrSerialisation), IMoshiTableTemplateRepository

    class MoshiTableTemplateDetailsListRepository(tableTemplateDetailsListMoshiAdapter: MoshiJsonAdapter<TableTemplateDetailsListMoshi>,
                                                  stringFileStorageStrSerialisation: IStringFileStorageStrSerialisation)
        : BaseSingleItemMoshiJsonRepository<TableTemplateDetailsListMoshi>(
        tableTemplateDetailsListMoshiAdapter, stringFileStorageStrSerialisation), IMoshiTableTemplateDetailsListRepository
}