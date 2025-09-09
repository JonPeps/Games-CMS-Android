package com.jonpeps.gamescms.dynamodb.services

import com.jonpeps.gamescms.dynamodb.services.core.DynamoDbRequest
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition
import software.amazon.awssdk.services.dynamodb.model.CreateTableRequest
import software.amazon.awssdk.services.dynamodb.model.CreateTableResponse
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement

interface IDynamoDbCreateTable {
    suspend fun create(): CreateTableResponse
}

class DynamoDbCreateTable(private val tableName: String,
                          private val attributes: List<AttributeDefinition>,
                          private val schemas: List<KeySchemaElement>) : IDynamoDbCreateTable {
    override suspend fun create(): CreateTableResponse {
        val request =
            CreateTableRequest
                .builder()
                .tableName(tableName)
                .attributeDefinitions(attributes)
                .keySchema(schemas)
        return DynamoDbRequest.getInstance().createTable(request.build())
    }
}