package com.jonpeps.gamescms.dynamodb.mappers

import com.jonpeps.gamescms.data.dataclasses.ItemType
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ItemTypeToAttTypeTests {
    private val mapper = CoreDynamoDbItemsMapper()

    @Test
    fun `test string is S when item type arg is STRING`() {
        val result = mapper.getStringType(ItemType.STRING)
        assert(result == "S")
    }

    @Test
    fun `test string is N when item type arg is INT`() {
        val result = mapper.getStringType(ItemType.INT)
        assert(result == "N")
    }

    @Test
    fun `test string is S when item type arg is UTC DATE`() {
        val result = mapper.getStringType(ItemType.UTC_DATE)
        assert(result == "S")
    }

    @Test
    fun `test string is S when item type arg is BOOLEAN`() {
        val result = mapper.getStringType(ItemType.BOOLEAN)
        assert(result == "S")
    }
}