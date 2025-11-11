package com.jonpeps.gamescms.hilt.di

import com.jonpeps.gamescms.data.helpers.JsonStringListHelper
import com.jonpeps.gamescms.data.repositories.MoshiStringListRepository
import com.jonpeps.gamescms.data.serialization.CommonSerializationRepoHelper
import com.jonpeps.gamescms.data.serialization.ICommonSerializationRepoHelper
import com.jonpeps.gamescms.data.serialization.debug.InputStreamSerializationRepoHelper
import com.jonpeps.gamescms.data.serialization.debug.IInputStreamSerializationRepoHelper
import com.jonpeps.gamescms.data.viewmodels.InputStreamStringListViewModel
import com.jonpeps.gamescms.ui.viewmodels.dynamodb.CreateTableViewModel
import com.jonpeps.gamescms.ui.tabletemplates.viewmodels.ITableTemplateGroupVmChangesCache
import com.jonpeps.gamescms.ui.tabletemplates.viewmodels.TableTemplateGroupVmChangesCache
import com.jonpeps.gamescms.ui.viewmodels.defaults.StringListToSplitItemListVm
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.CoroutineDispatcher
import software.amazon.awssdk.services.dynamodb.DynamoDbClient

@Module
@InstallIn(ViewModelComponent::class)
class ViewModelDiProvider {
    @Provides
    fun provideCreateTableViewModel(dynamoDbClient: DynamoDbClient, dispatcher: CoroutineDispatcher
    ): CreateTableViewModel {
        return CreateTableViewModel(dynamoDbClient, dispatcher)
    }

    @Provides
    fun provideInputStreamStringListViewModel(moshiStringListRepository: MoshiStringListRepository,
                                              commonSerializationRepoHelper: ICommonSerializationRepoHelper,
                                              inputStreamSerializationRepoHelper: IInputStreamSerializationRepoHelper,
                                              coroutineDispatcher: CoroutineDispatcher)
    : InputStreamStringListViewModel {
        return InputStreamStringListViewModel(moshiStringListRepository,
                                              commonSerializationRepoHelper,
                                              inputStreamSerializationRepoHelper,
                                              coroutineDispatcher)
    }

    @Provides
    fun provideTableTemplateListAssetToStorageVm(viewModel: InputStreamStringListViewModel,
                                                 jsonStringListHelper: JsonStringListHelper,
                                                 coroutineDispatcher: CoroutineDispatcher)
    : StringListToSplitItemListVm {
        return StringListToSplitItemListVm(viewModel, jsonStringListHelper, coroutineDispatcher)
    }
}

@Module
@InstallIn(ViewModelComponent::class)
abstract class ViewModelHelperDiBindings {
    @Binds
    abstract fun bindCommonSerializationRepoHelper(
        commonSerializationRepoHelper: CommonSerializationRepoHelper
    ): ICommonSerializationRepoHelper

    @Binds
    abstract fun bindInputStreamSerializationRepoHelper(
        inputStreamSerializationRepoHelper: InputStreamSerializationRepoHelper
    ): IInputStreamSerializationRepoHelper

    @Binds
    abstract fun bindTableTemplateGroupVmChangesCache(
        tableTemplateGroupVmChangesCacheImpl: TableTemplateGroupVmChangesCache
    ): ITableTemplateGroupVmChangesCache
}