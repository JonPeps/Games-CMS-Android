package com.jonpeps.gamescms.dynamodb.services

import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.Condition
import software.amazon.awssdk.services.dynamodb.model.ScanRequest
import software.amazon.awssdk.services.dynamodb.model.ScanResponse
import javax.inject.Inject

interface IDynamoDbScanTable {
    suspend fun scan(tableName: String, scanFilter: Map<String, Condition>): ScanResponse
}

class DynamoDbScanTable @Inject constructor(private val dynamoDbClient: DynamoDbClient): IDynamoDbScanTable {
    override suspend fun scan(tableName: String, scanFilter: Map<String, Condition>): ScanResponse {
        return dynamoDbClient.scan(
            ScanRequest
                .builder()
                .tableName(tableName)
                .scanFilter(scanFilter)
                .build())
    }
}