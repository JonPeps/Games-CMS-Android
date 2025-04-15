package com.jonpeps.gamescms.hilt.di

import com.jonpeps.gamescms.data.serialization.moshi.IStringListMoshiSerialization
import com.jonpeps.gamescms.data.serialization.moshi.ITableItemListMoshiSerialization
import com.jonpeps.gamescms.data.serialization.moshi.StringListMoshiSerialization
import com.jonpeps.gamescms.data.serialization.moshi.TableItemListMoshiSerialization
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SerializationDi {
    @Binds
    abstract fun provideStringListMoshiSerialization(stringListMoshiSerialization: StringListMoshiSerialization): IStringListMoshiSerialization

    @Binds
    abstract fun providesTableItemListMoshiSerialization(tableItemListMoshiSerialization: TableItemListMoshiSerialization): ITableItemListMoshiSerialization
}