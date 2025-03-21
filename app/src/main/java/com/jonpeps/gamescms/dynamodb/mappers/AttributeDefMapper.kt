package com.jonpeps.gamescms.dynamodb.mappers

import com.jonpeps.gamescms.data.ItemType
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition

class AttributeDefMapper {
    companion object {
        fun get(name: String, type: ItemType): AttributeDefinition  {
            return AttributeDefinition.builder()
                .attributeName(name)
                .attributeType(ItemTypeToAttType.get(type))
                .build()
        }
    }
}