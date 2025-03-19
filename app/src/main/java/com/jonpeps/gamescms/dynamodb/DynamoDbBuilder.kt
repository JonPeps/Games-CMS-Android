package com.jonpeps.gamescms.dynamodb

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient

sealed class DynamoDbBuilder {
    companion object {
        fun build(region: Region, accessKey: String, secretKey: String): DynamoDbClient
            = DynamoDbClient.builder()
            .region(region)
            .credentialsProvider(
                StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(accessKey, secretKey)
                )
            )
            .build()
    }
}