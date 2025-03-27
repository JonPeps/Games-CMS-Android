package com.jonpeps.gamescms.data.dataclasses

import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement

data class CreateTablePairData(
    val attDefinitions: List<AttributeDefinition>,
    val keySchema: List<KeySchemaElement>
)