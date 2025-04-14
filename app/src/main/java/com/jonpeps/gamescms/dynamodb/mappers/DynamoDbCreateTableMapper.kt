package com.jonpeps.gamescms.dynamodb.mappers

import com.jonpeps.gamescms.data.dataclasses.CreateTableItemData
import com.jonpeps.gamescms.data.dataclasses.CreateTablePairData
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement

interface IDynamoDbCreateTableMapper {
    fun mapToCreateTablePair(items: List<CreateTableItemData>): CreateTablePairData
}

class DynamoDbCreateTableMapper(private val coreItemsMapper: ICoreDynamoDbItemsMapper)
        : IDynamoDbCreateTableMapper {
    override fun mapToCreateTablePair(items: List<CreateTableItemData>): CreateTablePairData {
        val attDefItems = arrayListOf<AttributeDefinition>()
        val schemaItems = arrayListOf<KeySchemaElement>()
        items.forEach {
            val name = it.name
            attDefItems.add(coreItemsMapper
                .getAttributeDefinition(name, it.dataType))
            if (it.isPrimary) {
                schemaItems.add(coreItemsMapper
                    .getKeySchemaElementPrimary(name))
            }
            if (it.isSortKey) {
                schemaItems.add(coreItemsMapper
                    .getKeySchemaElementSort(name))
            }
        }
        return CreateTablePairData(attDefItems, schemaItems)
    }
}