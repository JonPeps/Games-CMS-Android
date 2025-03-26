package com.jonpeps.gamescms.dynamodb.mappers

import com.jonpeps.gamescms.data.ItemType
import com.jonpeps.gamescms.data.TableItem
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class AttValueMapperTests {
    private val mapper = CoreDynamoDbItemsMapper()

    @Test
    fun `test mapper attribute value when data type is string`() {
        val result = mapper
            .getAttributeValue(TableItem(id = -1,"name", ItemType.STRING, false, "value", false, false))
        assert(result.s() == "value")
    }

    @Test
    fun `test mapper attribute value when data type is int`() {
        val result = mapper
            .getAttributeValue(TableItem(id = -1, "name", ItemType.INT, false, "value", false, false))
        assert(result.n() == "value")
    }

    @Test
    fun `test mapper attribute value when data type is boolean true`() {
        val result = mapper
            .getAttributeValue(TableItem(id = -1, "name", ItemType.BOOLEAN, false, "true", false, false))
        assert(result.bool() == true)
    }

    @Test
    fun `test mapper attribute value when data type is boolean false`() {
        val result = mapper
            .getAttributeValue(TableItem(id = -1, "name", ItemType.BOOLEAN, false, "false", false, false))
        assert(result.bool() == false)
    }

    @Test
    fun `test mapper attribute value when data type is boolean 1`() {
        val result = mapper
            .getAttributeValue(TableItem(id = -1, "name", ItemType.BOOLEAN, false, "1", false, false))
        assert(result.bool() == true)
    }

    @Test
    fun `test mapper attribute value when data type is boolean 0`() {
        val result = mapper
            .getAttributeValue(TableItem(id = -1, "name", ItemType.BOOLEAN, false, "0", false, false))
        assert(result.bool() == false)
    }

    @Test
    fun `test mapper attribute value when data type is UTC date`() {
        val result = mapper
            .getAttributeValue(TableItem(id = -1, "name", ItemType.UTC_DATE, false, "2025-04-20", false, false))
        assert(result.s() == "2025-04-20")
    }
}