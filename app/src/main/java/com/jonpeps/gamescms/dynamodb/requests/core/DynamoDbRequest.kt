package com.jonpeps.gamescms.dynamodb.requests.core

import com.jonpeps.gamescms.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import software.amazon.awssdk.regions.Region

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