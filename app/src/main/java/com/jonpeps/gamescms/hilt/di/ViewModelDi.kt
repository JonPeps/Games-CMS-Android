package com.jonpeps.gamescms.hilt.di

import com.jonpeps.gamescms.dynamodb.mappers.IDynamoDbCreateTableMapper
import com.jonpeps.gamescms.dynamodb.services.IDynamoDbCreateTable
import com.jonpeps.gamescms.ui.createtable.helpers.ITableTemplateGroupVmRepoHelper
import com.jonpeps.gamescms.ui.createtable.helpers.TableTemplateGroupVmRepoHelper
import com.jonpeps.gamescms.ui.createtable.viewmodels.CreateTableViewModel
import com.jonpeps.gamescms.ui.createtable.viewmodels.TableTemplateGroupViewModel
import com.jonpeps.gamescms.ui.tabletemplates.repositories.ITableTemplateFileRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(SingletonComponent::class)
class ViewModelDiProvider {
    @Provides
    fun provideCreateTableViewModel(dynamoDbCreateTable: IDynamoDbCreateTable,
                                    dynamoDbCreateTableMapper: IDynamoDbCreateTableMapper,
                                    dispatcher: CoroutineDispatcher
    ): CreateTableViewModel {
        return CreateTableViewModel(dynamoDbCreateTable, dynamoDbCreateTableMapper, dispatcher)
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
@InstallIn(SingletonComponent::class)
abstract class ViewModelHelperDiBindings {
    @Binds
    abstract fun bindTableTemplateGroupVmRepoHelper(tableTemplateGroupVmRepoHelper: ITableTemplateGroupVmRepoHelper): ITableTemplateGroupVmRepoHelper
}