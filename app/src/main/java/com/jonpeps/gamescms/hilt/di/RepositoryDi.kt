package com.jonpeps.gamescms.hilt.di

import com.jonpeps.gamescms.data.dataclasses.moshi.StringListMoshi
import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateDetailsListMoshi
import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateItemListMoshi
import com.jonpeps.gamescms.data.repositories.IMoshiJsonRepositoryFactory
import com.jonpeps.gamescms.data.repositories.base.IBaseCachedMoshiJsonRepository
import com.jonpeps.gamescms.data.repositories.base.ISingleItemMoshiJsonRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class RepositoryDi {
    @Provides
    fun providesStringListRepository(repositoryFactory: IMoshiJsonRepositoryFactory)
        : ISingleItemMoshiJsonRepository<StringListMoshi> {
            return repositoryFactory.moshiStringListRepository()
    }
    @Provides
    fun providesCachedStrListRepository(repositoryFactory: IMoshiJsonRepositoryFactory)
        : IBaseCachedMoshiJsonRepository<StringListMoshi> {
            return repositoryFactory.moshiCachedStrListRepository()
    }
    @Provides
    fun providesTableTemplateRepository(repositoryFactory: IMoshiJsonRepositoryFactory)
        : ISingleItemMoshiJsonRepository<TableTemplateItemListMoshi> {
            return repositoryFactory.moshiTableTemplateRepository()
    }
    @Provides
    fun providesTableTemplateDetailsListRepository(repositoryFactory: IMoshiJsonRepositoryFactory)
        : ISingleItemMoshiJsonRepository<TableTemplateDetailsListMoshi> {
            return repositoryFactory.moshiTableTemplateDetailsListRepository()
    }
}