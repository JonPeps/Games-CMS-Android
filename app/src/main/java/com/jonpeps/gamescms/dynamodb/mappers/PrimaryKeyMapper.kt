package com.jonpeps.gamescms.dynamodb.mappers

import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement
import software.amazon.awssdk.services.dynamodb.model.KeyType

class PrimaryKeyMapper {
    companion object {
        fun get(name: String): KeySchemaElement {
            return KeySchemaElement.builder().attributeName(name).keyType(KeyType.HASH).build()
        }
    }
}