package com.jonpeps.gamescms.dynamodb.repositories

import com.jonpeps.gamescms.dynamodb.repositories.core.IDynamoDbRequest
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest
import software.amazon.awssdk.services.dynamodb.model.DeleteItemResponse
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
class DynamoDbDeleteItem @Inject constructor(private val dynamoDbClient: IDynamoDbRequest) {
    suspend fun delete(tableName: String, key: Map<String, AttributeValue>): DeleteItemResponse {
        return dynamoDbClient.getInstance().deleteItem(DeleteItemRequest.builder()
            .tableName(tableName)
            .key(key)
            .build())
    }
}