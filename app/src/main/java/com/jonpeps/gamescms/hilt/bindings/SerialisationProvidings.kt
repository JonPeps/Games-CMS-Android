package com.jonpeps.gamescms.hilt.bindings

import com.jonpeps.gamescms.data.serialization.moshi.IMoshiJsonAdapterCreator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SerialisationProvidings {
    @Provides
    @Singleton
    fun provideMoshiJsonAdapterTableItem(moshiJsonAdapterCreator: IMoshiJsonAdapterCreator)
        = moshiJsonAdapterCreator.getTableItemTemplateJsonAdapter()
}