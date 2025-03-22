package com.jonpeps.gamescms.dynamodb.mappers

import com.jonpeps.gamescms.data.ItemType
import com.jonpeps.gamescms.data.TableItem
import org.junit.Before
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

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
}