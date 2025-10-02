package com.jonpeps.gamescms.dynamodb.services

import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.DeleteTableRequest
import software.amazon.awssdk.services.dynamodb.model.DeleteTableResponse
import javax.inject.Inject

interface IDynamoDbDeleteTable {
    suspend fun delete(tableName: String): DeleteTableResponse
}

class DynamoDbDeleteTable @Inject constructor(private val dynamoDbClient: DynamoDbClient): IDynamoDbDeleteTable {
    override suspend fun delete(tableName: String): DeleteTableResponse {
        return dynamoDbClient
            .deleteTable(DeleteTableRequest.builder().tableName(tableName).build())
        }
}