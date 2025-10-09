package com.jonpeps.gamescms.hilt.di

import com.jonpeps.gamescms.data.repositories.StringListMoshiJsonAdapter
import com.jonpeps.gamescms.data.serialization.CommonDeleteFileHelper
import com.jonpeps.gamescms.data.serialization.ICommonDeleteFileHelper
import com.jonpeps.gamescms.data.serialization.string.IStringFileStorageStrSerialisation
import com.jonpeps.gamescms.data.serialization.string.IStringSerialization
import com.jonpeps.gamescms.data.serialization.string.StringFileStorageStrSerialisation
import com.jonpeps.gamescms.data.serialization.string.StringSerialization
import com.jonpeps.gamescms.data.repositories.TableTemplateMoshiJsonAdapter
import com.jonpeps.gamescms.data.repositories.IStringListItemsVmChangesCache
import com.jonpeps.gamescms.data.repositories.StringListItemsVmChangesCache
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(SingletonComponent::class)
class SerializationDiProvider {
    @Provides
    fun provideStringFileStorageStrSerialisation(stringSerialization: IStringSerialization,
                                                 dispatcher: CoroutineDispatcher)
    : IStringFileStorageStrSerialisation {
        return StringFileStorageStrSerialisation(stringSerialization, dispatcher)
    }

    @Provides
    fun provideStringSerialization(): IStringSerialization {
        return StringSerialization()
    }

    @Provides
    fun providesStringListItemsVmChangesCache(): IStringListItemsVmChangesCache {
        return StringListItemsVmChangesCache()
    }

    @Provides
    fun providesStringListMoshiJsonAdapter(): StringListMoshiJsonAdapter {
        return StringListMoshiJsonAdapter()
    }

    @Provides
    fun providesTableTemplateItemListMoshiAdapter(): TableTemplateMoshiJsonAdapter {
        return TableTemplateMoshiJsonAdapter()
    }

    @Provides
    fun providesCommonDeleteFileHelper(): ICommonDeleteFileHelper {
        return CommonDeleteFileHelper()
    }
}