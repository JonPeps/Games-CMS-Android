package com.jonpeps.gamescms.dynamodb.services

import com.jonpeps.gamescms.dynamodb.services.core.DynamoDbRequest
import software.amazon.awssdk.services.dynamodb.model.DeleteTableRequest
import software.amazon.awssdk.services.dynamodb.model.DeleteTableResponse

interface IDynamoDbDeleteTable {
    suspend fun delete(tableName: String): DeleteTableResponse
}

class DynamoDbDeleteTable: IDynamoDbDeleteTable {
    override suspend fun delete(tableName: String): DeleteTableResponse {
        return DynamoDbRequest.getInstance()
            .deleteTable(DeleteTableRequest.builder().tableName(tableName).build())
        }
}