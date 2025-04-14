package com.jonpeps.gamescms.dynamodb.services

import com.jonpeps.gamescms.dynamodb.services.core.DynamoDbRequest
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition
import software.amazon.awssdk.services.dynamodb.model.CreateTableRequest
import software.amazon.awssdk.services.dynamodb.model.CreateTableResponse
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement

interface IDynamoDbCreateTable {
    suspend fun create(tableName: String,
                       attributes: List<AttributeDefinition>,
                       schemas: List<KeySchemaElement>): CreateTableResponse
}

class DynamoDbCreateTable : IDynamoDbCreateTable {
    override suspend fun create(tableName: String,
                                attributes: List<AttributeDefinition>,
                                schemas: List<KeySchemaElement>): CreateTableResponse {
        val request =
            CreateTableRequest
                .builder()
                .tableName(tableName)
                .attributeDefinitions(attributes)
                .keySchema(schemas)
        return DynamoDbRequest.getInstance().createTable(request.build())
    }
}