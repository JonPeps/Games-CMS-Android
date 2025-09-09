package com.jonpeps.gamescms.dynamodb.services;

import com.jonpeps.gamescms.dynamodb.services.core.DynamoDbRequest
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse

interface IDynamoDbPutItem {
    suspend fun putItem(): PutItemResponse
}

class DynamoDbPutItem(private val tableName: String,
                      private val attributes: Map<String, AttributeValue>) : IDynamoDbPutItem {
    override suspend fun putItem(): PutItemResponse  {
        return DynamoDbRequest.getInstance()
            .putItem(PutItemRequest.builder()
                .tableName(tableName)
                .item(attributes)
                .build())
    }
}