package com.jonpeps.gamescms.hilt.di

import com.jonpeps.gamescms.dynamodb.mappers.CoreDynamoDbItemsMapper
import com.jonpeps.gamescms.dynamodb.mappers.DynamoDbCreateTableMapper
import com.jonpeps.gamescms.dynamodb.mappers.ICoreDynamoDbItemsMapper
import com.jonpeps.gamescms.dynamodb.mappers.IDynamoDbCreateTableMapper
import com.jonpeps.gamescms.dynamodb.services.DynamoDbCreateTable
import com.jonpeps.gamescms.dynamodb.services.IDynamoDbCreateTable
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DynamoDbDiProvidings {
    @Provides
    fun provideDynamoDbCreateTable(): IDynamoDbCreateTable {
        return DynamoDbCreateTable()
    }
    @Provides
    fun provideCoreDynamoDbItemsMapper(): ICoreDynamoDbItemsMapper {
        return CoreDynamoDbItemsMapper()
    }
    @Provides
    fun provideDynamoDbCreateTableMapper(coreItemsMapper: ICoreDynamoDbItemsMapper): IDynamoDbCreateTableMapper {
        return DynamoDbCreateTableMapper(coreItemsMapper)
    }
}

