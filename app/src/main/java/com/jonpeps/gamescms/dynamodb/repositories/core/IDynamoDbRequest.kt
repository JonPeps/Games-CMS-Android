package com.jonpeps.gamescms.dynamodb.repositories.core

import software.amazon.awssdk.services.dynamodb.DynamoDbClient

interface IDynamoDbRequest {
    fun getInstance(): DynamoDbClient
}