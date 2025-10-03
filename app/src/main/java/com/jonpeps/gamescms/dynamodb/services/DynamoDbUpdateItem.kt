package com.jonpeps.gamescms.dynamodb.services

import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.AttributeValueUpdate
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest
import software.amazon.awssdk.services.dynamodb.model.UpdateItemResponse
import javax.inject.Inject

interface IDynamoDbUpdateItem {
    suspend fun updateItem(tableName: String, values: Map<String, AttributeValueUpdate>): UpdateItemResponse
}

class DynamoDbUpdateItem @Inject constructor(private val dynamoDbClient: DynamoDbClient) : IDynamoDbUpdateItem {
    override suspend fun updateItem(tableName: String, values: Map<String, AttributeValueUpdate>): UpdateItemResponse {
        val request = UpdateItemRequest.builder()
            .tableName(tableName)
            .attributeUpdates(values)
            .build()
        return dynamoDbClient.updateItem(request)
    }
}