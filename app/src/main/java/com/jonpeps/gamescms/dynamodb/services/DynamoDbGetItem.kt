package com.jonpeps.gamescms.dynamodb.services

import com.jonpeps.gamescms.dynamodb.services.core.DynamoDbRequest
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse

interface IDynamoDbGetItem {
    suspend fun getItem(): GetItemResponse
}

class DynamoDbGetItem(private val tableName: String, private val key: Map<String, AttributeValue>) : IDynamoDbGetItem {
    override suspend fun getItem(): GetItemResponse {
        return DynamoDbRequest.getInstance().getItem(
            GetItemRequest.builder()
            .tableName(tableName)
            .key(key)
            .build())
    }
}