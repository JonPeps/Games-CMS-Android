package com.jonpeps.gamescms.dynamodb.services

import com.jonpeps.gamescms.dynamodb.services.core.DynamoDbRequest
import software.amazon.awssdk.services.dynamodb.model.AttributeValueUpdate
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest
import software.amazon.awssdk.services.dynamodb.model.UpdateItemResponse

interface IDynamoDbUpdateItem {
    suspend fun updateItem(): UpdateItemResponse
}

class DynamoDbUpdateItem(private val tableName: String, private val values: Map<String, AttributeValueUpdate>) : IDynamoDbUpdateItem {
    override suspend fun updateItem(): UpdateItemResponse {
        return DynamoDbRequest.getInstance()
            .updateItem(
                UpdateItemRequest.builder()
                .tableName(tableName)
                .attributeUpdates(values)
                .build())
    }
}