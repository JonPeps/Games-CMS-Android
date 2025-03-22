package com.jonpeps.gamescms.dynamodb.mappers

import com.jonpeps.gamescms.data.ItemType
import com.jonpeps.gamescms.data.TableItem
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import software.amazon.awssdk.services.dynamodb.model.KeyType

@RunWith(JUnit4::class)
class DynamoDbCreateTableMapperTests {
    private val mapper = DynamoDbCreateTableMapper(CoreDynamoDbItemsMapper())
    private var testItems: List<TableItem>? = null

    @Before
    fun setup() {
        testItems =
            listOf(
                TableItem("id",
                    ItemType.INT,
                    true,
                    "1",
                    editable = false,
                    isSortKey = false
                ),
                TableItem(
                    "score",
                    ItemType.INT,
                    false,
                    "1000",
                    editable = false,
                    isSortKey = true
                ),
                TableItem(
                    "player",
                    ItemType.STRING,
                    false,
                    "Peps",
                    editable = true,
                    isSortKey = false
                ),
                TableItem(
                    "email",
                    ItemType.STRING,
                    false,
                    "jon.l.peplow@gmail.com",
                    editable = true,
                    isSortKey = false
                ))

    }

    @Test
    fun `test table items are parsed to attribute definitions as expected`() {
        val mapperResult = mapper.mapToCreateTablePair(testItems!!)
        val attributeDefinitions = mapperResult.attDefinitions
        var item = attributeDefinitions[0]
        assert(item.attributeName() == "id")
        assert(item.attributeType().name == "N")
        item = attributeDefinitions[1]
        assert(item.attributeName() == "score")
        assert(item.attributeType().name == "N")
        item = attributeDefinitions[2]
        assert(item.attributeName() == "player")
        assert(item.attributeType().name == "S")
        item = attributeDefinitions[3]
        assert(item.attributeName() == "email")
        assert(item.attributeType().name == "S")
    }

    @Test
    fun `test table items are parsed and key schemas are generated as expected`() {
        val mapperResult = mapper.mapToCreateTablePair(testItems!!)
        val keySchemaElement = mapperResult.keySchema
        assert(keySchemaElement[0].attributeName() == "id")
        assert(keySchemaElement[0].keyType() == KeyType.HASH)
        assert(keySchemaElement[1].attributeName() == "score")
        assert(keySchemaElement[1].keyType() == KeyType.RANGE)
    }
}