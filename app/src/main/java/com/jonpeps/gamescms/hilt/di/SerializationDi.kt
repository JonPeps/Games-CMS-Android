package com.jonpeps.gamescms.hilt.di

import com.jonpeps.gamescms.data.repositories.StringListMoshiJsonAdapter
import com.jonpeps.gamescms.data.serialization.CommonDeleteFileHelper
import com.jonpeps.gamescms.data.serialization.ICommonDeleteFileHelper
import com.jonpeps.gamescms.data.serialization.string.IStringFileStorageStrSerialisation
import com.jonpeps.gamescms.data.serialization.string.IStringSerialization
import com.jonpeps.gamescms.data.serialization.string.StringFileStorageStrSerialisation
import com.jonpeps.gamescms.data.serialization.string.StringSerialization
import com.jonpeps.gamescms.ui.tabletemplates.repositories.TableTemplateItemListMoshiAdapter
import com.jonpeps.gamescms.ui.tabletemplates.viewmodels.IStringListItemsVmChangesCache
import com.jonpeps.gamescms.ui.tabletemplates.viewmodels.StringListItemsVmChangesCache
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
    fun providesTableTemplateItemListMoshiAdapter(): TableTemplateItemListMoshiAdapter {
        return TableTemplateItemListMoshiAdapter()
    }

    @Provides
    fun providesCommonDeleteFileHelper(): ICommonDeleteFileHelper {
        return CommonDeleteFileHelper()
    }
}