package com.jonpeps.gamescms.dynamodb.services;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse
import javax.inject.Inject

interface IDynamoDbPutItem {
    suspend fun putItem(tableName: String,
                        attributes: Map<String, AttributeValue>): PutItemResponse
}

class DynamoDbPutItem @Inject constructor(private val dynamoDbClient: DynamoDbClient) : IDynamoDbPutItem {
    override suspend fun putItem(tableName: String,
                                 attributes: Map<String, AttributeValue>): PutItemResponse  {
        return dynamoDbClient
            .putItem(PutItemRequest.builder()
                .tableName(tableName)
                .item(attributes)
                .build())
    }
}