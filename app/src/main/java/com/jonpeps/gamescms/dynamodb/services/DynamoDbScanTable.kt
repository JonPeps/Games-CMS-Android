package com.jonpeps.gamescms.dynamodb.services

import com.jonpeps.gamescms.dynamodb.services.core.DynamoDbRequest
import software.amazon.awssdk.services.dynamodb.model.Condition
import software.amazon.awssdk.services.dynamodb.model.ScanRequest
import software.amazon.awssdk.services.dynamodb.model.ScanResponse

interface IDynamoDbScanTable {
    suspend fun scan(tableName: String, scanFilter: Map<String, Condition>): ScanResponse
}

class DynamoDbScanTable: IDynamoDbScanTable {
    override suspend fun scan(tableName: String, scanFilter: Map<String, Condition>): ScanResponse {
        return DynamoDbRequest.getInstance().scan(
            ScanRequest
                .builder()
                .tableName(tableName)
                .scanFilter(scanFilter)
                .build())
    }
}