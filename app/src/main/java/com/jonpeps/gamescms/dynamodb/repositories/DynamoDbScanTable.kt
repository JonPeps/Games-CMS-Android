package com.jonpeps.gamescms.dynamodb.repositories

import com.jonpeps.gamescms.dynamodb.repositories.core.IDynamoDbRequest
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import software.amazon.awssdk.services.dynamodb.model.Condition
import software.amazon.awssdk.services.dynamodb.model.ScanRequest
import software.amazon.awssdk.services.dynamodb.model.ScanResponse
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
class DynamoDbScanTable @Inject constructor(private val dynamoDbClient: IDynamoDbRequest) {
    suspend fun scan(tableName: String, scanFilter: Map<String, Condition>): ScanResponse {
        return dynamoDbClient.getInstance().scan(
            ScanRequest
                .builder()
                .tableName(tableName)
                .scanFilter(scanFilter)
                .build())
    }
}