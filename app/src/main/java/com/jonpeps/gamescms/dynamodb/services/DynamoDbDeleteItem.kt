package com.jonpeps.gamescms.dynamodb.services

import com.jonpeps.gamescms.dynamodb.services.core.DynamoDbRequest
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest
import software.amazon.awssdk.services.dynamodb.model.DeleteItemResponse

interface IDynamoDbDeleteItem {
    suspend fun delete(tableName: String, key: Map<String, AttributeValue>): DeleteItemResponse
}

class DynamoDbDeleteItem : IDynamoDbDeleteItem {
    override suspend fun delete(tableName: String, key: Map<String, AttributeValue>): DeleteItemResponse {
        return DynamoDbRequest.getInstance().deleteItem(DeleteItemRequest.builder()
            .tableName(tableName)
            .key(key)
            .build())
    }
}