package com.jonpeps.gamescms.hilt.bindings

import com.jonpeps.gamescms.dynamodb.services.IDynamoDbCreateTable
import com.jonpeps.gamescms.dynamodb.services.core.IDynamoDbRequest
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DynamoDbServiceBindings {
    @Singleton
    @Binds
    abstract fun bindDynamoDbRequest(dynamoDbRequest: IDynamoDbRequest): IDynamoDbRequest

    @Singleton
    @Binds
    abstract fun bindCreateTable(createTable: IDynamoDbCreateTable): IDynamoDbCreateTable
}