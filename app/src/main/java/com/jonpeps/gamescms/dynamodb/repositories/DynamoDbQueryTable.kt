package com.jonpeps.gamescms.dynamodb.repositories

import com.jonpeps.gamescms.dynamodb.repositories.core.IDynamoDbRequest
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import software.amazon.awssdk.services.dynamodb.model.Condition
import software.amazon.awssdk.services.dynamodb.model.QueryRequest
import software.amazon.awssdk.services.dynamodb.model.QueryResponse
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
class DynamoDbQueryTable @Inject constructor(private val dynamoDbClient: IDynamoDbRequest) {
    suspend fun query(tableName: String, queryFilter: Map<String, Condition>): QueryResponse {
        return dynamoDbClient.getInstance().query(
            QueryRequest
                .builder()
                .tableName(tableName)
                .queryFilter(queryFilter)
                .build())
    }
}