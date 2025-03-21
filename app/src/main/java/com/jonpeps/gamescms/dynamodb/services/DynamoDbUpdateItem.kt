package com.jonpeps.gamescms.dynamodb.services

import com.jonpeps.gamescms.dynamodb.services.core.IDynamoDbRequest
import software.amazon.awssdk.services.dynamodb.model.AttributeValueUpdate
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest
import software.amazon.awssdk.services.dynamodb.model.UpdateItemResponse
import javax.inject.Inject

interface IDynamoDbUpdateItem {
    suspend fun updateItem(tableName: String, values: Map<String, AttributeValueUpdate>): UpdateItemResponse
}

class DynamoDbUpdateItem @Inject constructor(private val dynamoDbClient: IDynamoDbRequest): IDynamoDbUpdateItem {
    override suspend fun updateItem(tableName: String, values: Map<String, AttributeValueUpdate>): UpdateItemResponse {
        return dynamoDbClient.getInstance()
            .updateItem(
                UpdateItemRequest.builder()
                .tableName(tableName)
                .attributeUpdates(values)
                .build())
    }
}