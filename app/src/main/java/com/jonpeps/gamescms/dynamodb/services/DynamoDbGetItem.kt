package com.jonpeps.gamescms.dynamodb.services

import com.jonpeps.gamescms.dynamodb.services.core.IDynamoDbRequest
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse
import javax.inject.Inject

interface IDynamoDbGetItem {
    suspend fun getItem(tableName: String, key: Map<String, AttributeValue>): GetItemResponse
}

class DynamoDbGetItem @Inject constructor(private val dynamoDbClient: IDynamoDbRequest): IDynamoDbGetItem {
    override suspend fun getItem(tableName: String, key: Map<String, AttributeValue>): GetItemResponse {
        return dynamoDbClient.getInstance().getItem(
            GetItemRequest.builder()
            .tableName(tableName)
            .key(key)
            .build())
    }
}