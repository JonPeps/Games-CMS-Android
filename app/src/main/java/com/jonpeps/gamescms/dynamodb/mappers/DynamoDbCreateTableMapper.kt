package com.jonpeps.gamescms.dynamodb.mappers

import com.jonpeps.gamescms.data.CreateTablePairData
import com.jonpeps.gamescms.data.TableItem
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement
import javax.inject.Inject

interface IDynamoDbCreateTableMapper {
    fun mapToCreateTablePair(items: List<TableItem>): CreateTablePairData
}

class DynamoDbCreateTableMapper
    @Inject constructor(private val coreItemsMapper: ICoreDynamoDbItemsMapper)
        : IDynamoDbCreateTableMapper {

    override fun mapToCreateTablePair(items: List<TableItem>): CreateTablePairData {
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