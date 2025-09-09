package com.jonpeps.gamescms.hilt.di

import com.jonpeps.gamescms.data.serialization.CommonSerializationRepoHelper
import com.jonpeps.gamescms.data.serialization.ICommonSerializationRepoHelper
import com.jonpeps.gamescms.data.serialization.debug.InputStreamSerializationRepoHelper
import com.jonpeps.gamescms.data.serialization.debug.IInputStreamSerializationRepoHelper
import com.jonpeps.gamescms.dynamodb.services.IDynamoDbCreateTable
import com.jonpeps.gamescms.ui.createtable.viewmodels.CreateTableViewModel
import com.jonpeps.gamescms.ui.tabletemplates.viewmodels.ITableTemplateGroupVmChangesCache
import com.jonpeps.gamescms.ui.tabletemplates.viewmodels.TableTemplateGroupVmChangesCache
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(ViewModelComponent::class)
class ViewModelDiProvider {
    @Provides
    fun provideCreateTableViewModel(dispatcher: CoroutineDispatcher
    ): CreateTableViewModel {
        return CreateTableViewModel(dispatcher)
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