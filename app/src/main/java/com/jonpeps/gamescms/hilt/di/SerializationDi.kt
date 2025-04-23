package com.jonpeps.gamescms.hilt.di

import com.jonpeps.gamescms.data.dataclasses.mappers.ITableItemFinalMapper
import com.jonpeps.gamescms.data.dataclasses.mappers.TableItemFinalMapper
import com.jonpeps.gamescms.data.serialization.moshi.IStringListMoshiSerialization
import com.jonpeps.gamescms.data.serialization.moshi.ITableItemListMoshiSerialization
import com.jonpeps.gamescms.data.serialization.moshi.MoshiJsonAdapterCreator
import com.jonpeps.gamescms.data.serialization.moshi.StringListMoshiSerialization
import com.jonpeps.gamescms.data.serialization.moshi.TableItemListMoshiSerialization
import com.jonpeps.gamescms.data.serialization.string.IStringFileStorageStrSerialisation
import com.jonpeps.gamescms.data.serialization.string.IStringSerialization
import com.jonpeps.gamescms.data.serialization.string.StringFileStorageStrSerialisation
import com.jonpeps.gamescms.data.serialization.string.StringSerialization
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(SingletonComponent::class)
class SerializationDiProvider {
    @Provides
    fun provideStringListMoshiSerialization(moshiJsonAdapterCreator: MoshiJsonAdapterCreator,
                                            dispatcher: CoroutineDispatcher): IStringListMoshiSerialization {
        return StringListMoshiSerialization(moshiJsonAdapterCreator, dispatcher)
    }

    @Provides
    fun providesTableItemListMoshiSerialization(moshiJsonAdapterCreator: MoshiJsonAdapterCreator,
                                                dispatcher: CoroutineDispatcher): ITableItemListMoshiSerialization {
        return TableItemListMoshiSerialization(moshiJsonAdapterCreator, dispatcher)
    }

    @Provides
    fun provideStringFileStorageStrSerialisation(stringSerialization: IStringSerialization,
                                                 dispatcher: CoroutineDispatcher)
    : IStringFileStorageStrSerialisation {
        return StringFileStorageStrSerialisation(stringSerialization, dispatcher)
    }

    @Provides
    fun provideTableItemFinalMapper(): ITableItemFinalMapper {
        return TableItemFinalMapper()
    }

    @Provides
    fun provideStringSerialization(): IStringSerialization {
        return StringSerialization()
    }
}