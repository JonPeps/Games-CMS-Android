package com.jonpeps.gamescms.dynamodb.repositories

import com.jonpeps.gamescms.dynamodb.repositories.core.IDynamoDbRequest
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import software.amazon.awssdk.services.dynamodb.model.DeleteTableRequest
import software.amazon.awssdk.services.dynamodb.model.DeleteTableResponse
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
class DynamoDbDeleteTable @Inject constructor(private val dynamoDbClient: IDynamoDbRequest) {
    suspend fun delete(tableName: String): DeleteTableResponse {
        return dynamoDbClient.getInstance()
            .deleteTable(DeleteTableRequest.builder().tableName(tableName).build())
        }
}