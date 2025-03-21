package com.jonpeps.gamescms.dynamodb.requests.core

import software.amazon.awssdk.services.dynamodb.DynamoDbClient

interface IDynamoDbRequest {
    fun getInstance(): DynamoDbClient
}