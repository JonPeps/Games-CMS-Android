package com.jonpeps.gamescms.hilt.di

import android.content.Context
import android.content.res.AssetManager
import com.jonpeps.gamescms.data.repositories.StringListMoshiJsonAdapter
import com.jonpeps.gamescms.data.serialization.CommonDeleteFileHelper
import com.jonpeps.gamescms.data.serialization.ICommonDeleteFileHelper
import com.jonpeps.gamescms.data.serialization.string.IStringFileStorageStrSerialisation
import com.jonpeps.gamescms.data.serialization.string.IStringSerialization
import com.jonpeps.gamescms.data.serialization.string.StringFileStorageStrSerialisation
import com.jonpeps.gamescms.data.serialization.string.StringSerialization
import com.jonpeps.gamescms.data.repositories.TableTemplateMoshiJsonAdapter
import com.jonpeps.gamescms.data.helpers.IStringListItemsVmChangesCache
import com.jonpeps.gamescms.data.helpers.IStringListToSplitItemList
import com.jonpeps.gamescms.data.helpers.InputStreamTableTemplate
import com.jonpeps.gamescms.data.helpers.JsonStringListHelper
import com.jonpeps.gamescms.data.helpers.StringListItemsVmChangesCache
import com.jonpeps.gamescms.data.helpers.StringListToSplitItemList
import com.jonpeps.gamescms.data.repositories.DefaultTableTemplateStatusMoshiAdapter
import com.jonpeps.gamescms.data.repositories.IMoshiStringListRepository
import com.jonpeps.gamescms.data.repositories.MoshiStringListRepository
import com.jonpeps.gamescms.data.repositories.MoshiTableTemplateRepository
import com.jonpeps.gamescms.data.serialization.CommonSerializationRepoHelper
import com.jonpeps.gamescms.data.serialization.ICommonSerializationRepoHelper
import com.jonpeps.gamescms.data.serialization.debug.IInputStreamSerializationRepoHelper
import com.jonpeps.gamescms.data.serialization.debug.InputStreamSerializationRepoHelper
import com.jonpeps.gamescms.data.serialization.moshi.InputStreamStringList
import com.jonpeps.gamescms.ui.tabletemplates.serialization.ISerializeTableTemplates
import com.jonpeps.gamescms.ui.tabletemplates.serialization.SerializeTableTemplateHelpers
import com.jonpeps.gamescms.ui.tabletemplates.serialization.SerializeTableTemplateUpdateCore
import com.jonpeps.gamescms.ui.tabletemplates.serialization.SerializeTableTemplates
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(SingletonComponent::class)
class SerializationDiProvider {
    @Provides
    fun bindInputStreamSerializationRepoHelper(): IInputStreamSerializationRepoHelper {
        return InputStreamSerializationRepoHelper()
    }

    @Provides
    fun bindStringListToSplitItemList(inputStreamStringList: InputStreamStringList,
                                      jsonStringListHelper: JsonStringListHelper): IStringListToSplitItemList {
        return StringListToSplitItemList(inputStreamStringList, jsonStringListHelper)
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
    fun providesStringListMoshiJsonAdapter(): StringListMoshiJsonAdapter {
        return StringListMoshiJsonAdapter()
    }

    @Provides
    fun providesTableTemplateItemListMoshiAdapter(): TableTemplateMoshiJsonAdapter {
        return TableTemplateMoshiJsonAdapter()
    }

    @Provides
    fun providesDefaultTableTemplateStatusMoshiAdapter(): DefaultTableTemplateStatusMoshiAdapter {
        return DefaultTableTemplateStatusMoshiAdapter()
    }

    @Provides
    fun providesMoshiTableTemplateRepository(tableTemplateMoshiJsonAdapter: TableTemplateMoshiJsonAdapter,
                                             stringFileStorageStrSerialisation: IStringFileStorageStrSerialisation)
    : MoshiTableTemplateRepository {
        return MoshiTableTemplateRepository(tableTemplateMoshiJsonAdapter,
            stringFileStorageStrSerialisation)
    }

    @Provides
    fun providesSerializationTableTemplateHelpers(): SerializeTableTemplateHelpers {
        return SerializeTableTemplateHelpers()
    }

    @Provides
    fun providesSerializationTableTemplateUpdateCore(serializeTableTemplateHelpers: SerializeTableTemplateHelpers,
                                                     stringListRepository: MoshiStringListRepository,
                                                     commonSerializationRepoHelper: CommonSerializationRepoHelper)
    : SerializeTableTemplateUpdateCore {
        return SerializeTableTemplateUpdateCore(serializeTableTemplateHelpers,
            stringListRepository,
            commonSerializationRepoHelper)
    }

    @Provides
    fun providesCommonDeleteFileHelper(): ICommonDeleteFileHelper {
        return CommonDeleteFileHelper()
    }

    @Provides
    fun providesInputStreamStringList(moshiStringListRepository: IMoshiStringListRepository,
                                      commonSerializationRepoHelper: ICommonSerializationRepoHelper,
                                      inputStreamSerializationRepoHelper: IInputStreamSerializationRepoHelper): InputStreamStringList {
        return InputStreamStringList(moshiStringListRepository,
            commonSerializationRepoHelper,
            inputStreamSerializationRepoHelper)
    }

    @Provides
    fun providesStringListToSplitItemList(inputStreamStringList: InputStreamStringList,
                                          jsonStringListHelper: JsonStringListHelper): StringListToSplitItemList {
        return StringListToSplitItemList(inputStreamStringList, jsonStringListHelper)
    }

    @Provides
    fun providesSerializeTableTemplates(@ApplicationContext context: Context,
                                        assetManager: AssetManager,
                                        stringListToSplitItemList: StringListToSplitItemList,
                                        inputStreamTableTemplate: InputStreamTableTemplate,
                                        moshiTableTemplateRepository: MoshiTableTemplateRepository,
                                        commonSerializationRepoHelper: CommonSerializationRepoHelper,
                                        serializeTableTemplateUpdateCore: SerializeTableTemplateUpdateCore)
    : ISerializeTableTemplates {
         return SerializeTableTemplates(
             context,
             assetManager,
             stringListToSplitItemList,
             inputStreamTableTemplate,
             moshiTableTemplateRepository,
             commonSerializationRepoHelper,
             serializeTableTemplateUpdateCore
         )
    }
}