package com.jonpeps.gamescms.dynamodb.mappers

import com.jonpeps.gamescms.data.CreateTablePairData
import com.jonpeps.gamescms.data.TableItem
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement

class PreCreateTablePairMapper {
    companion object {
        fun map(items: List<TableItem>): CreateTablePairData {
            val attDefItems = arrayListOf<AttributeDefinition>()
            val schemaItems = arrayListOf<KeySchemaElement>()
            items.forEach {
                attDefItems.add(AttributeDefMapper.get(it.name, it.dataType))
                if (it.isPrimary) {
                    schemaItems.add(PrimaryKeyMapper.get(it.name))
                }
                if (it.isSortKey) {
                    schemaItems.add(SortKeyMapper.get(it.name))
                }
            }
            return CreateTablePairData(attDefItems, schemaItems)
        }
    }
}