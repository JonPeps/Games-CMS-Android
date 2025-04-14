package com.jonpeps.gamescms.dynamodb.services

import com.jonpeps.gamescms.dynamodb.services.core.DynamoDbRequest
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse

interface IDynamoDbGetItem {
    suspend fun getItem(tableName: String, key: Map<String, AttributeValue>): GetItemResponse
}

class DynamoDbGetItem : IDynamoDbGetItem {
    override suspend fun getItem(tableName: String, key: Map<String, AttributeValue>): GetItemResponse {
        return DynamoDbRequest.getInstance().getItem(
            GetItemRequest.builder()
            .tableName(tableName)
            .key(key)
            .build())
    }
}