package com.jonpeps.gamescms.hilt.di

import com.jonpeps.gamescms.data.serialization.CommonSerializationRepoHelper
import com.jonpeps.gamescms.data.serialization.ICommonSerializationRepoHelper
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
    fun provideCreateTableViewModel(dynamoDbCreateTable: IDynamoDbCreateTable,
                                    dispatcher: CoroutineDispatcher
    ): CreateTableViewModel {
        return CreateTableViewModel(dynamoDbCreateTable, dispatcher)
    }
//    @Provides
//    fun provideTableTemplateGroupViewModel(tableTemplateFilesPath: String,
//                                           tableTemplateRepository: ITableTemplateFileRepository,
//                                           tableTemplateGroupVmRepoHelper: ITableTemplateGroupVmRepoHelper,
//                                           coroutineDispatcher: CoroutineDispatcher): TableTemplateGroupViewModel {
//        return TableTemplateGroupViewModel(tableTemplateFilesPath, tableTemplateRepository, tableTemplateGroupVmRepoHelper, coroutineDispatcher)
//    }
}

@Module
@InstallIn(ViewModelComponent::class)
abstract class ViewModelHelperDiBindings {
    @Binds
    abstract fun bindCommonSerializationRepoHelper(
        tableTemplateGroupVmRepoHelperImpl: CommonSerializationRepoHelper
    ): ICommonSerializationRepoHelper

    @Binds
    abstract fun bindTableTemplateGroupVmChangesCache(
        tableTemplateGroupVmChangesCacheImpl: TableTemplateGroupVmChangesCache
    ): ITableTemplateGroupVmChangesCache
}