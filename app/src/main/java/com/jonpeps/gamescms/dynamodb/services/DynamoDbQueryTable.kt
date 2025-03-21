package com.jonpeps.gamescms.dynamodb.services

import com.jonpeps.gamescms.dynamodb.services.core.IDynamoDbRequest
import software.amazon.awssdk.services.dynamodb.model.Condition
import software.amazon.awssdk.services.dynamodb.model.QueryRequest
import software.amazon.awssdk.services.dynamodb.model.QueryResponse
import javax.inject.Inject

interface IDynamoDbQueryTable  {
    suspend fun query(tableName: String, queryFilter: Map<String, Condition>): QueryResponse
}

class DynamoDbQueryTable @Inject constructor(private val dynamoDbClient: IDynamoDbRequest): IDynamoDbQueryTable {
    override suspend fun query(tableName: String, queryFilter: Map<String, Condition>): QueryResponse {
        return dynamoDbClient.getInstance().query(
            QueryRequest
                .builder()
                .tableName(tableName)
                .queryFilter(queryFilter)
                .build())
    }
}