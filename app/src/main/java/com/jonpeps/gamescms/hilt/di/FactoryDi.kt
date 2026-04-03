package com.jonpeps.gamescms.hilt.di

import com.jonpeps.gamescms.data.repositories.IMoshiJsonAdapterFactory
import com.jonpeps.gamescms.data.repositories.IMoshiJsonCachesFactory
import com.jonpeps.gamescms.data.repositories.IMoshiJsonRepositoryFactory
import com.jonpeps.gamescms.data.repositories.MoshiJsonAdapterFactoryImpl
import com.jonpeps.gamescms.data.repositories.MoshiJsonCachesFactoryImpl
import com.jonpeps.gamescms.data.repositories.MoshiJsonRepositoryFactoryImpl
import com.jonpeps.gamescms.data.serialization.moshi.IInputStreamFactory
import com.jonpeps.gamescms.data.serialization.moshi.InputStreamFactoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class FactoryDiBindings {
    @Binds
    abstract fun bindMoshiJsonRepositoryFactory(
        moshiJsonRepositoryFactoryImpl: MoshiJsonRepositoryFactoryImpl
    ) : IMoshiJsonRepositoryFactory
    @Binds
    abstract fun bindMoshiJsonAdapterFactory(
        moshiJsonAdapterFactoryImpl: MoshiJsonAdapterFactoryImpl
    ) : IMoshiJsonAdapterFactory
    @Binds
    abstract fun bindStringListMoshiJsonCache(
        moshiJsonCachesFactoryImpl: MoshiJsonCachesFactoryImpl
    ) : IMoshiJsonCachesFactory
    @Binds
    abstract fun bindISToJsonTypeToStorage(
        iSFactoryImpl: InputStreamFactoryImpl
    ) : IInputStreamFactory
}