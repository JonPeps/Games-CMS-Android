package com.jonpeps.gamescms.hilt.di

import com.jonpeps.gamescms.data.repositories.IMoshiStringListRepository
import com.jonpeps.gamescms.data.repositories.MoshiStringListRepository
import com.jonpeps.gamescms.data.repositories.StringListMoshiJsonAdapter
import com.jonpeps.gamescms.data.serialization.string.IStringFileStorageStrSerialisation
import com.jonpeps.gamescms.ui.tabletemplates.repositories.ITableTemplateFileRepository
import com.jonpeps.gamescms.ui.tabletemplates.repositories.IStringListMoshiJsonCache
import com.jonpeps.gamescms.ui.tabletemplates.repositories.ITableTemplateStringMoshiJsonCache
import com.jonpeps.gamescms.ui.tabletemplates.repositories.StringListMoshiJsonCache
import com.jonpeps.gamescms.ui.tabletemplates.repositories.TableTemplateFileRepository
import com.jonpeps.gamescms.ui.tabletemplates.repositories.TableTemplateItemListMoshiAdapter
import com.jonpeps.gamescms.ui.tabletemplates.repositories.TableTemplateStringMoshiJsonCache
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class RepositoryDiProvider {
    @Provides
    fun provideMoshiStringListRepository(stringListMoshiJsonAdapter: StringListMoshiJsonAdapter,
                                         stringFileStorageStrSerialisation: IStringFileStorageStrSerialisation,
                                         stringMoshiJsonCache: IStringListMoshiJsonCache)
    : IMoshiStringListRepository {
        return MoshiStringListRepository(stringListMoshiJsonAdapter, stringMoshiJsonCache, stringFileStorageStrSerialisation)
    }

    @Provides
    fun provideTableTemplateFileRepository(tableTemplateItemListMoshiAdapter: TableTemplateItemListMoshiAdapter,
                                           stringFileStorageStrSerialisation: IStringFileStorageStrSerialisation,
                                           tableTemplateStringMoshiCache: TableTemplateStringMoshiJsonCache
    )
    : ITableTemplateFileRepository {
        return TableTemplateFileRepository(tableTemplateItemListMoshiAdapter, tableTemplateStringMoshiCache, stringFileStorageStrSerialisation)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryDiBindings {
    @Binds
    abstract fun bindTableTemplateStringMoshiJsonCache(
        tableTemplateStringMoshiJsonCacheImpl: TableTemplateStringMoshiJsonCache
    ): ITableTemplateStringMoshiJsonCache

    @Binds
    abstract fun bindStringListMoshiJsonCache(
        stringListMoshiCache: StringListMoshiJsonCache
    ): IStringListMoshiJsonCache
}


