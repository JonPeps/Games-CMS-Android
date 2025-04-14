package com.jonpeps.gamescms.dynamodb.services

import com.jonpeps.gamescms.dynamodb.services.core.DynamoDbRequest
import software.amazon.awssdk.services.dynamodb.model.Condition
import software.amazon.awssdk.services.dynamodb.model.QueryRequest
import software.amazon.awssdk.services.dynamodb.model.QueryResponse

interface IDynamoDbQueryTable  {
    suspend fun query(tableName: String, queryFilter: Map<String, Condition>): QueryResponse
}

class DynamoDbQueryTable : IDynamoDbQueryTable {
    override suspend fun query(tableName: String, queryFilter: Map<String, Condition>): QueryResponse {
        return DynamoDbRequest.getInstance().query(
            QueryRequest
                .builder()
                .tableName(tableName)
                .queryFilter(queryFilter)
                .build())
    }
}