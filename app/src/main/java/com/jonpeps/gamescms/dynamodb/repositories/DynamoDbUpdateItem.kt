package com.jonpeps.gamescms.dynamodb.repositories

import com.jonpeps.gamescms.dynamodb.repositories.core.IDynamoDbRequest
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import software.amazon.awssdk.services.dynamodb.model.AttributeValueUpdate
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest
import software.amazon.awssdk.services.dynamodb.model.UpdateItemResponse
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
class DynamoDbUpdateItem @Inject constructor(private val dynamoDbClient: IDynamoDbRequest) {
    suspend fun updateItem(tableName: String, values: Map<String, AttributeValueUpdate>): UpdateItemResponse {
        return dynamoDbClient.getInstance()
            .updateItem(
                UpdateItemRequest.builder()
                .tableName(tableName)
                .attributeUpdates(values)
                .build())
    }
}