package com.jonpeps.gamescms.hilt.di

import com.jonpeps.gamescms.data.repositories.ICachedMoshiStringListRepository
import com.jonpeps.gamescms.data.repositories.IMoshiJsonRepositoryFactory
import com.jonpeps.gamescms.data.repositories.IMoshiStringListRepository
import com.jonpeps.gamescms.data.repositories.IMoshiTableTemplateDetailsListRepository
import com.jonpeps.gamescms.data.repositories.IMoshiTableTemplateRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class RepositoryDi {
    @Provides
    fun providesStringListRepository(repositoryFactory: IMoshiJsonRepositoryFactory)
        : IMoshiStringListRepository {
            return repositoryFactory.moshiStringListRepository()
    }
    @Provides
    fun providesCachedStrListRepository(repositoryFactory: IMoshiJsonRepositoryFactory)
        : ICachedMoshiStringListRepository {
            return repositoryFactory.moshiCachedStrListRepository()
    }
    @Provides
    fun providesTableTemplateRepository(repositoryFactory: IMoshiJsonRepositoryFactory)
        : IMoshiTableTemplateRepository {
            return repositoryFactory.moshiTableTemplateRepository()
    }
    @Provides
    fun providesTableTemplateDetailsListRepository(repositoryFactory: IMoshiJsonRepositoryFactory)
        : IMoshiTableTemplateDetailsListRepository {
            return repositoryFactory.moshiTableTemplateDetailsListRepository()
    }
}