package com.jonpeps.gamescms.dynamodb.services.core

import com.jonpeps.gamescms.BuildConfig
import software.amazon.awssdk.regions.Region

class DynamoDbRequest {
    companion object {
        fun getInstance()
                = DynamoDbBuilder.build(
            Region.EU_WEST_2,
            BuildConfig.DYNAMO_DB_ACCESS_KEY,
            BuildConfig.DYNAMO_DB_SECRET_KEY)
    }
}