package com.jonpeps.gamescms.dynamodb.repositories;

import com.jonpeps.gamescms.dynamodb.repositories.core.IDynamoDbRequest
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
class DynamoDbPutItem @Inject constructor(private val dynamoDbClient: IDynamoDbRequest) {
    suspend fun putItem(tableName: String,
                        attributes: Map<String, AttributeValue>): PutItemResponse  {
        return dynamoDbClient.getInstance()
            .putItem(PutItemRequest.builder()
                .tableName(tableName)
                .item(attributes)
                .build())
    }
}