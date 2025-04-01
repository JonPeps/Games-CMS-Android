package com.jonpeps.gamescms.hilt.bindings

import com.jonpeps.gamescms.data.serialization.moshi.IMoshiJsonAdapterCreator
import com.jonpeps.gamescms.data.serialization.string.IStringSerialization
import com.jonpeps.gamescms.data.serialization.moshi.ITableItemListMoshiSerialization
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SerialisationBindings {
    @Binds
    @Singleton
    abstract fun provideSerializeString(serializeString: IStringSerialization): IStringSerialization

    @Binds
    @Singleton
    abstract fun provideMoshiJsonAdapterCreator(moshiJsonAdapterCreator: IMoshiJsonAdapterCreator): IMoshiJsonAdapterCreator

    @Binds
    @Singleton
    abstract fun provideTableItemListMoshiSerialization(tableItemListMoshiSerialization: ITableItemListMoshiSerialization): ITableItemListMoshiSerialization
}