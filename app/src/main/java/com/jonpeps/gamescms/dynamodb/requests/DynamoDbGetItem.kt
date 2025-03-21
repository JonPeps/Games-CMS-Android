package com.jonpeps.gamescms.dynamodb.requests

import com.jonpeps.gamescms.dynamodb.requests.core.IDynamoDbRequest
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
class DynamoDbGetItem @Inject constructor(private val dynamoDbClient: IDynamoDbRequest) {
    suspend fun getItem(tableName: String, key: Map<String, AttributeValue>): GetItemResponse {
        return dynamoDbClient.getInstance().getItem(
            GetItemRequest.builder()
            .tableName(tableName)
            .key(key)
            .build())
    }
}