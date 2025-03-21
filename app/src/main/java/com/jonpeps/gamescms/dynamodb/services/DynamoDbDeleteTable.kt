package com.jonpeps.gamescms.dynamodb.services

import com.jonpeps.gamescms.dynamodb.services.core.IDynamoDbRequest
import software.amazon.awssdk.services.dynamodb.model.DeleteTableRequest
import software.amazon.awssdk.services.dynamodb.model.DeleteTableResponse
import javax.inject.Inject

interface IDynamoDbDeleteTable {
    suspend fun delete(tableName: String): DeleteTableResponse
}

class DynamoDbDeleteTable @Inject constructor(private val dynamoDbClient: IDynamoDbRequest): IDynamoDbDeleteTable {
    override suspend fun delete(tableName: String): DeleteTableResponse {
        return dynamoDbClient.getInstance()
            .deleteTable(DeleteTableRequest.builder().tableName(tableName).build())
        }
}