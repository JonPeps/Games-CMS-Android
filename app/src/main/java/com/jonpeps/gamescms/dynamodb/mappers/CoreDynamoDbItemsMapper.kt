package com.jonpeps.gamescms.dynamodb.mappers

import com.jonpeps.gamescms.data.dataclasses.ItemType
import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateItemMoshi
import software.amazon.awssdk.services.dynamodb.model.AttributeAction
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.AttributeValueUpdate
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement
import software.amazon.awssdk.services.dynamodb.model.KeyType

class CoreDynamoDbItemsMapper {
    companion object {
        fun getAttributeDefinition(name: String, type: ItemType): AttributeDefinition {
            return AttributeDefinition.builder()
                .attributeName(name)
                .attributeType(getStringType(type))
                .build()
        }

        fun getAttributeValue(item: TableTemplateItemMoshi): AttributeValue {
            val builder = AttributeValue.builder()
            when (item.dataType) {
                ItemType.STRING -> builder.s(item.value)
                ItemType.UTC_DATE -> builder.s(item.value)
                ItemType.INT -> builder.n(item.value)
                ItemType.BOOLEAN -> builder.bool(MappersConstants.toBoolean(item.value))
            }
            return builder.build()
        }

        fun getAttributeValueUpdate(item: TableTemplateItemMoshi, action: AttributeAction): AttributeValueUpdate {
            return AttributeValueUpdate.builder().value(getAttributeValue(item)).action(action).build()
        }

        fun getKeySchemaElementPrimary(name: String): KeySchemaElement {
            return KeySchemaElement.builder().attributeName(name).keyType(KeyType.HASH).build()
        }

        fun getKeySchemaElementSort(name: String): KeySchemaElement {
            return KeySchemaElement.builder().attributeName(name).keyType(KeyType.RANGE).build()
        }

        fun getStringType(type: ItemType): String {
            return when (type) {
                ItemType.STRING -> "S"
                ItemType.BOOLEAN -> "S"
                ItemType.UTC_DATE -> "S"
                ItemType.INT -> "N"
            }
        }

        fun getAttValue(item: TableTemplateItemMoshi): AttributeValue {
            val builder = AttributeValue.builder()
            when (item.dataType) {
                ItemType.STRING -> builder.s(item.value)
                ItemType.UTC_DATE -> builder.s(item.value)
                ItemType.INT -> builder.n(item.value)
                ItemType.BOOLEAN -> builder.bool(MappersConstants.toBoolean(item.value))
            }
            return builder.build()
        }
    }
}