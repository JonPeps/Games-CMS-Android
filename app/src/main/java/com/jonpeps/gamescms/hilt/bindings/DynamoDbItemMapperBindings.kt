package com.jonpeps.gamescms.hilt.bindings

import com.jonpeps.gamescms.dynamodb.mappers.ICoreDynamoDbItemsMapper
import com.jonpeps.gamescms.dynamodb.mappers.IDynamoDbCreateTableMapper
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DynamoDbItemMapperBindings {
    @Binds
    @Singleton
    abstract fun bindCoreItemsMapper(mapper: ICoreDynamoDbItemsMapper): ICoreDynamoDbItemsMapper

    @Binds
    @Singleton
    abstract fun bindCreateTableMapper(mapper: IDynamoDbCreateTableMapper): IDynamoDbCreateTableMapper
}