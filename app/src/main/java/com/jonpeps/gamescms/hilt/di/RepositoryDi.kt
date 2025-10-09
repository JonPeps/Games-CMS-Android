package com.jonpeps.gamescms.hilt.di

import com.jonpeps.gamescms.data.repositories.IMoshiStringListRepository
import com.jonpeps.gamescms.data.repositories.MoshiStringListRepository
import com.jonpeps.gamescms.data.repositories.StringListMoshiJsonAdapter
import com.jonpeps.gamescms.data.serialization.string.IStringFileStorageStrSerialisation
import com.jonpeps.gamescms.data.repositories.ITableTemplateFileRepository
import com.jonpeps.gamescms.data.repositories.IStringListMoshiJsonCache
import com.jonpeps.gamescms.data.repositories.ITableTemplateStringMoshiJsonCache
import com.jonpeps.gamescms.data.repositories.StringListMoshiJsonCache
import com.jonpeps.gamescms.data.repositories.TableTemplateFileRepository
import com.jonpeps.gamescms.data.repositories.TableTemplateMoshiJsonAdapter
import com.jonpeps.gamescms.data.repositories.TableTemplateStringMoshiJsonCache
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
    fun provideTableTemplateFileRepository(tableTemplateItemListMoshiAdapter: TableTemplateMoshiJsonAdapter,
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


