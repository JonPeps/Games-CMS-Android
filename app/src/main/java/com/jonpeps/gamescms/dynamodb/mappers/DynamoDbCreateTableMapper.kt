package com.jonpeps.gamescms.dynamodb.mappers

import com.jonpeps.gamescms.data.dataclasses.createtemplate.CreateTableItemData
import com.jonpeps.gamescms.data.dataclasses.createtemplate.CreateTablePairData
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement

class DynamoDbCreateTableMapper {
    companion object {
        @JvmStatic
        fun mapToCreateTablePair(items: List<CreateTableItemData>): CreateTablePairData {
            val attDefItems = arrayListOf<AttributeDefinition>()
            val schemaItems = arrayListOf<KeySchemaElement>()
            items.forEach {
                val name = it.name
                attDefItems.add(CoreDynamoDbItemsMapper
                    .getAttributeDefinition(name, it.dataType))
                if (it.isPrimary) {
                    schemaItems.add(CoreDynamoDbItemsMapper
                        .getKeySchemaElementPrimary(name))
                }
                if (it.isSortKey) {
                    schemaItems.add(CoreDynamoDbItemsMapper
                        .getKeySchemaElementSort(name))
                }
            }
            return CreateTablePairData(attDefItems, schemaItems)
        }
    }
}