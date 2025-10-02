package com.jonpeps.gamescms.hilt.di

import com.jonpeps.gamescms.dynamodb.services.DynamoDbCreateTable
import com.jonpeps.gamescms.dynamodb.services.IDynamoDbCreateTable
import com.jonpeps.gamescms.dynamodb.services.core.DynamoDbRequest
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import software.amazon.awssdk.services.dynamodb.DynamoDbClient

@Module
@InstallIn(SingletonComponent::class)
class DynamoDbBindings {
    @Provides
    fun bindDynamoDbClient(): DynamoDbClient = DynamoDbRequest.getInstance()
}

@Module
@InstallIn(SingletonComponent::class)
object DynamoDbDiProvidings {
    @Provides
    fun provideDynamoDbCreateTable(dynamoDbClient: DynamoDbClient): IDynamoDbCreateTable {
        return DynamoDbCreateTable(dynamoDbClient)
    }
}

