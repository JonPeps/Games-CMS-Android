package com.jonpeps.gamescms.hilt.di

import com.jonpeps.gamescms.dynamodb.services.DynamoDbCreateTable
import com.jonpeps.gamescms.dynamodb.services.IDynamoDbCreateTable
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement

@Module
@InstallIn(SingletonComponent::class)
object DynamoDbDiProvidings {
    @Provides
    fun provideDynamoDbCreateTable(tableName: String,
                                   attributes: List<AttributeDefinition>,
                                   schemas: List<KeySchemaElement>): IDynamoDbCreateTable {
        return DynamoDbCreateTable(tableName, attributes, schemas)
    }
}

