package com.jonpeps.gamescms.dynamodb.services.core

import com.jonpeps.gamescms.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient

interface IDynamoDbRequest {
    fun getInstance(): DynamoDbClient
}

@Module
@InstallIn(SingletonComponent::class)
class DynamoDbRequest : IDynamoDbRequest {
    @Provides
    override fun getInstance()
        = DynamoDbBuilder.build(
        Region.EU_WEST_2,
        BuildConfig.DYNAMO_DB_ACCESS_KEY,
        BuildConfig.DYNAMO_DB_SECRET_KEY
    )
}