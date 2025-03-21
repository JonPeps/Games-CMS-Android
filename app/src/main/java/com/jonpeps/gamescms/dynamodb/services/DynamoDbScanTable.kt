package com.jonpeps.gamescms.dynamodb.services

import com.jonpeps.gamescms.dynamodb.services.core.IDynamoDbRequest
import software.amazon.awssdk.services.dynamodb.model.Condition
import software.amazon.awssdk.services.dynamodb.model.ScanRequest
import software.amazon.awssdk.services.dynamodb.model.ScanResponse
import javax.inject.Inject

interface IDynamoDbScanTable {
    suspend fun scan(tableName: String, scanFilter: Map<String, Condition>): ScanResponse
}

class DynamoDbScanTable @Inject constructor(private val dynamoDbClient: IDynamoDbRequest): IDynamoDbScanTable {
    override suspend fun scan(tableName: String, scanFilter: Map<String, Condition>): ScanResponse {
        return dynamoDbClient.getInstance().scan(
            ScanRequest
                .builder()
                .tableName(tableName)
                .scanFilter(scanFilter)
                .build())
    }
}