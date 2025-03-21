package com.jonpeps.gamescms.dynamodb.services

import com.jonpeps.gamescms.dynamodb.services.core.IDynamoDbRequest
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest
import software.amazon.awssdk.services.dynamodb.model.DeleteItemResponse
import javax.inject.Inject

interface IDynamoDbDeleteItem {
    suspend fun delete(tableName: String, key: Map<String, AttributeValue>): DeleteItemResponse
}

class DynamoDbDeleteItem @Inject constructor(private val dynamoDbClient: IDynamoDbRequest): IDynamoDbDeleteItem {
    override suspend fun delete(tableName: String, key: Map<String, AttributeValue>): DeleteItemResponse {
        return dynamoDbClient.getInstance().deleteItem(DeleteItemRequest.builder()
            .tableName(tableName)
            .key(key)
            .build())
    }
}