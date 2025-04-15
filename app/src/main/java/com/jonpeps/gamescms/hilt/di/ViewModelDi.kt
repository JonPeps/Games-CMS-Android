package com.jonpeps.gamescms.hilt.di

import com.jonpeps.gamescms.dynamodb.mappers.IDynamoDbCreateTableMapper
import com.jonpeps.gamescms.dynamodb.services.IDynamoDbCreateTable
import com.jonpeps.gamescms.ui.createtable.viewmodels.CreateTableViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(SingletonComponent::class)
class ViewModelDi {
    @Provides
    fun provideCreateTableViewModel(dynamoDbCreateTable: IDynamoDbCreateTable,
                                    dynamoDbCreateTableMapper: IDynamoDbCreateTableMapper,
                                    dispatcher: CoroutineDispatcher
    ): CreateTableViewModel {
        return CreateTableViewModel(dynamoDbCreateTable, dynamoDbCreateTableMapper, dispatcher)
    }
}