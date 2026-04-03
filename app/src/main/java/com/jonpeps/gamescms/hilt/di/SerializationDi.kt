package com.jonpeps.gamescms.hilt.di

import com.jonpeps.gamescms.data.dataclasses.moshi.StringListMoshi
import com.jonpeps.gamescms.data.serialization.CommonDeleteFileHelper
import com.jonpeps.gamescms.data.serialization.ICommonDeleteFileHelper
import com.jonpeps.gamescms.data.serialization.string.IStringFileStorageStrSerialisation
import com.jonpeps.gamescms.data.serialization.string.IStringSerialization
import com.jonpeps.gamescms.data.serialization.string.StringFileStorageStrSerialisation
import com.jonpeps.gamescms.data.serialization.string.StringSerialization
import com.jonpeps.gamescms.data.helpers.IStringListItemsVmChangesCache
import com.jonpeps.gamescms.data.helpers.ITableTemplateGroupValidator
import com.jonpeps.gamescms.data.helpers.StringListItemsVmChangesCache
import com.jonpeps.gamescms.data.helpers.TableTemplateGroupValidator
import com.jonpeps.gamescms.data.repositories.IMoshiJsonCachesFactory
import com.jonpeps.gamescms.data.repositories.IStringListMoshiJsonCache
import com.jonpeps.gamescms.data.repositories.ITableTemplateStringMoshiJsonCache
import com.jonpeps.gamescms.data.repositories.base.ISingleItemMoshiJsonRepository
import com.jonpeps.gamescms.data.serialization.CommonSerializationRepoHelper
import com.jonpeps.gamescms.data.serialization.ICommonSerializationRepoHelper
import com.jonpeps.gamescms.data.serialization.debug.IInputStreamSerializationRepoHelper
import com.jonpeps.gamescms.data.serialization.debug.InputStreamSerializationRepoHelper
import com.jonpeps.gamescms.data.serialization.moshi.ISToJsonTypeToStorage
import com.jonpeps.gamescms.ui.tabletemplates.serialization.ISerializeTableTemplateHelpers
import com.jonpeps.gamescms.ui.tabletemplates.serialization.SerializeTableTemplateHelpers
import com.jonpeps.gamescms.ui.tabletemplates.serialization.SerializeTableTemplateUpdateCore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(SingletonComponent::class)
class SerializationDiProvider {
    @Provides
    fun provideInputStreamSerializationRepoHelper(): IInputStreamSerializationRepoHelper {
        return InputStreamSerializationRepoHelper()
    }

    @Provides
    fun provideCommonSerializationRepoHelper(): ICommonSerializationRepoHelper {
        return CommonSerializationRepoHelper()
    }

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
    fun providesSerializationTableTemplateHelpers(): SerializeTableTemplateHelpers {
        return SerializeTableTemplateHelpers()
    }

    @Provides
    fun providesSerializationTableTemplateUpdateCore(
        serializeTableTemplateHelpers: SerializeTableTemplateHelpers,
        stringListRepository: ISingleItemMoshiJsonRepository<StringListMoshi>,
        commonSerializationRepoHelper: CommonSerializationRepoHelper)
    : SerializeTableTemplateUpdateCore {
        return SerializeTableTemplateUpdateCore(
            serializeTableTemplateHelpers,
            stringListRepository,
            commonSerializationRepoHelper)
    }

    @Provides
    fun providesCommonDeleteFileHelper(): ICommonDeleteFileHelper {
        return CommonDeleteFileHelper()
    }

    @Provides
    fun providesInputStreamStringList(
        moshiStringListRepository: ISingleItemMoshiJsonRepository<StringListMoshi>,
        commonSerializationRepoHelper: ICommonSerializationRepoHelper,
        inputStreamSerializationRepoHelper: IInputStreamSerializationRepoHelper
    ) : ISToJsonTypeToStorage<StringListMoshi> {
            return ISToJsonTypeToStorage<StringListMoshi>(
                moshiStringListRepository,
                commonSerializationRepoHelper,
                inputStreamSerializationRepoHelper)
    }

    @Provides
    fun providesSerializeTableTemplateHelpers(): ISerializeTableTemplateHelpers {
        return SerializeTableTemplateHelpers()
    }

    @Provides
    fun providesITableTemplateGroupValidator(serializeTableTemplateHelpers: ISerializeTableTemplateHelpers):
            ITableTemplateGroupValidator {
        return TableTemplateGroupValidator(serializeTableTemplateHelpers)
    }

    @Provides
    fun providesStringListJsonCache(cachesFactory: IMoshiJsonCachesFactory)
    : IStringListMoshiJsonCache {
        return cachesFactory.stringMoshiJson()
    }

    @Provides
    fun providesTableTemplateStringJsonCache(cachesFactory: IMoshiJsonCachesFactory)
    : ITableTemplateStringMoshiJsonCache {
        return cachesFactory.tableTemplateStringMoshiJson()
    }
}