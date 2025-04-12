package com.jonpeps.gamescms.hilt.bindings

import com.jonpeps.gamescms.data.serialization.moshi.IMoshiJsonAdapterCreator
import com.jonpeps.gamescms.data.serialization.moshi.IStringListMoshiSerialization
import com.jonpeps.gamescms.data.serialization.string.IStringSerialization
import com.jonpeps.gamescms.data.serialization.moshi.ITableItemListMoshiSerialization
import com.jonpeps.gamescms.data.serialization.string.IStringFileStorageStrSerialisation
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

    @Binds
    @Singleton
    abstract fun provideStringListMoshiSerialization(stringListMoshiSerialization: IStringListMoshiSerialization): IStringListMoshiSerialization

    @Binds
    @Singleton
    abstract fun provideStringFileStorageStrSerialisation(strFilesStorageStrSerialisation: IStringFileStorageStrSerialisation): IStringFileStorageStrSerialisation
}