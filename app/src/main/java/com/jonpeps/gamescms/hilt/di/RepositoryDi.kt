package com.jonpeps.gamescms.hilt.di

import com.jonpeps.gamescms.data.repositories.IMoshiStringListRepository
import com.jonpeps.gamescms.data.repositories.MoshiStringListRepository
import com.jonpeps.gamescms.data.serialization.moshi.IStringListMoshiSerialization
import com.jonpeps.gamescms.data.serialization.moshi.ITableItemListMoshiSerialization
import com.jonpeps.gamescms.data.serialization.string.IStringFileStorageStrSerialisation
import com.jonpeps.gamescms.ui.tabletemplates.repositories.ITableTemplateFileRepository
import com.jonpeps.gamescms.ui.tabletemplates.repositories.TableTemplateFileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class RepositoryDiProvider {
    @Provides
    fun provideMoshiStringListRepository(strListMoshiSerialization: IStringListMoshiSerialization,
                                         stringFileStorageStrSerialisation: IStringFileStorageStrSerialisation)
    : IMoshiStringListRepository {
        return MoshiStringListRepository(strListMoshiSerialization, stringFileStorageStrSerialisation)
    }

    @Provides
    fun provideTableTemplateFileRepository(tableTableItemListMoshiSerialization: ITableItemListMoshiSerialization,
                                           stringFileStorageStrSerialisation: IStringFileStorageStrSerialisation)
    : ITableTemplateFileRepository {
        return TableTemplateFileRepository(tableTableItemListMoshiSerialization, stringFileStorageStrSerialisation)
    }
}


