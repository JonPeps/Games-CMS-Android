package com.jonpeps.gamescms.hilt.di

import com.jonpeps.gamescms.data.serialization.moshi.IMoshiJsonAdapterCreator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class MoshiDiBindings {
    @Binds
    abstract fun bindMoshiJsonAdapterCreator(moshiJsonAdapterCreator: IMoshiJsonAdapterCreator): IMoshiJsonAdapterCreator
}