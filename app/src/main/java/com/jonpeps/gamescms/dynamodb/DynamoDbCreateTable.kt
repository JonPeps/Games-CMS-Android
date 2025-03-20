package com.jonpeps.gamescms.dynamodb

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition
import software.amazon.awssdk.services.dynamodb.model.CreateTableRequest
import software.amazon.awssdk.services.dynamodb.model.CreateTableResponse
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
class DynamoDbCreateTable @Inject constructor(private val dynamoDbClient: DynamoDbClient) {
    fun create(tableName: String,
               attributes: List<AttributeDefinition>,
               schemas: List<KeySchemaElement>): CreateTableResponse {
        val request =
            CreateTableRequest
                .builder()
                .tableName(tableName)
                .attributeDefinitions(attributes)
                .keySchema(schemas)
        return dynamoDbClient.createTable(request.build())
    }
}