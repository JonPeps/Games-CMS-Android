package com.jonpeps.gamescms.hilt.di

import com.jonpeps.gamescms.data.repositories.IMoshiStringListRepository
import com.jonpeps.gamescms.data.repositories.MoshiStringListRepository
import com.jonpeps.gamescms.data.serialization.moshi.IStringListMoshiSerialization
import com.jonpeps.gamescms.data.serialization.moshi.ITableItemListMoshiSerialization
import com.jonpeps.gamescms.data.serialization.string.IStringFileStorageStrSerialisation
import com.jonpeps.gamescms.ui.tabletemplates.repositories.ITableTemplateFileRepository
import com.jonpeps.gamescms.ui.tabletemplates.repositories.ITableTemplateStringListMoshiJsonCache
import com.jonpeps.gamescms.ui.tabletemplates.repositories.ITableTemplateStringMoshiJsonCache
import com.jonpeps.gamescms.ui.tabletemplates.repositories.TableTemplateFileRepository
import com.jonpeps.gamescms.ui.tabletemplates.repositories.TableTemplateStringListMoshiJsonCache
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
    fun provideMoshiStringListRepository(strListMoshiSerialization: IStringListMoshiSerialization,
                                         stringFileStorageStrSerialisation: IStringFileStorageStrSerialisation,
                                         tableTemplateStringMoshiJsonCache: ITableTemplateStringListMoshiJsonCache)
    : IMoshiStringListRepository {
        return MoshiStringListRepository(strListMoshiSerialization, tableTemplateStringMoshiJsonCache, stringFileStorageStrSerialisation)
    }

    @Provides
    fun provideTableTemplateFileRepository(tableTableItemListMoshiSerialization: ITableItemListMoshiSerialization,
                                           stringFileStorageStrSerialisation: IStringFileStorageStrSerialisation,
                                           tableTemplateStringListListMoshiCache: TableTemplateStringListMoshiJsonCache
    )
    : ITableTemplateFileRepository {
        return TableTemplateFileRepository(tableTableItemListMoshiSerialization, tableTemplateStringListListMoshiCache, stringFileStorageStrSerialisation)
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
    abstract fun bindTableTemplateStringListMoshiJsonCache(
        tableTemplateStringMoshiJsonCacheImpl: TableTemplateStringMoshiJsonCache
    ): ITableTemplateStringMoshiJsonCache
}


