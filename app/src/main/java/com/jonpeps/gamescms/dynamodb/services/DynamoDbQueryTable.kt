package com.jonpeps.gamescms.dynamodb.services

import com.jonpeps.gamescms.dynamodb.services.core.DynamoDbRequest
import software.amazon.awssdk.services.dynamodb.model.Condition
import software.amazon.awssdk.services.dynamodb.model.QueryRequest
import software.amazon.awssdk.services.dynamodb.model.QueryResponse

interface IDynamoDbQueryTable  {
    suspend fun query(): QueryResponse
}

class DynamoDbQueryTable(private val tableName: String, private val queryFilter: Map<String, Condition>) : IDynamoDbQueryTable {
    override suspend fun query(): QueryResponse {
        return DynamoDbRequest.getInstance().query(
            QueryRequest
                .builder()
                .tableName(tableName)
                .queryFilter(queryFilter)
                .build())
    }
}