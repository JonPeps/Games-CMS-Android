package com.jonpeps.gamescms.dynamodb.services

import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.Condition
import software.amazon.awssdk.services.dynamodb.model.QueryRequest
import software.amazon.awssdk.services.dynamodb.model.QueryResponse
import javax.inject.Inject

interface IDynamoDbQueryTable  {
    suspend fun query(tableName: String, queryFilter: Map<String, Condition>): QueryResponse
}

class DynamoDbQueryTable @Inject constructor(private val dynamoDbClient: DynamoDbClient) : IDynamoDbQueryTable {
    override suspend fun query(tableName: String, queryFilter: Map<String, Condition>): QueryResponse {
        return dynamoDbClient.query(
            QueryRequest
                .builder()
                .tableName(tableName)
                .queryFilter(queryFilter)
                .build())
    }
}