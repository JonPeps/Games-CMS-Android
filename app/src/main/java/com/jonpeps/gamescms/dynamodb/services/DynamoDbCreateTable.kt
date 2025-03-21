package com.jonpeps.gamescms.dynamodb.services

import com.jonpeps.gamescms.dynamodb.services.core.IDynamoDbRequest
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition
import software.amazon.awssdk.services.dynamodb.model.CreateTableRequest
import software.amazon.awssdk.services.dynamodb.model.CreateTableResponse
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement
import javax.inject.Inject

interface IDynamoDbCreateTable {
    suspend fun create(tableName: String,
                       attributes: List<AttributeDefinition>,
                       schemas: List<KeySchemaElement>): CreateTableResponse
}

class DynamoDbCreateTable @Inject constructor(private val dynamoDbClient: IDynamoDbRequest): IDynamoDbCreateTable {
    override suspend fun create(tableName: String,
                                attributes: List<AttributeDefinition>,
                                schemas: List<KeySchemaElement>): CreateTableResponse {
        val request =
            CreateTableRequest
                .builder()
                .tableName(tableName)
                .attributeDefinitions(attributes)
                .keySchema(schemas)
        return dynamoDbClient.getInstance().createTable(request.build())
    }
}