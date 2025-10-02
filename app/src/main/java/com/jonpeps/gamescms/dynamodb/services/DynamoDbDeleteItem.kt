package com.jonpeps.gamescms.dynamodb.services

import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest
import software.amazon.awssdk.services.dynamodb.model.DeleteItemResponse
import javax.inject.Inject

interface IDynamoDbDeleteItem {
    suspend fun delete(tableName: String, key: Map<String, AttributeValue>): DeleteItemResponse
}

class DynamoDbDeleteItem @Inject constructor(private val dynamoDbClient: DynamoDbClient) : IDynamoDbDeleteItem {
    override suspend fun delete(tableName: String, key: Map<String, AttributeValue>): DeleteItemResponse {
        return dynamoDbClient.deleteItem(DeleteItemRequest.builder()
            .tableName(tableName)
            .key(key)
            .build())
    }
}