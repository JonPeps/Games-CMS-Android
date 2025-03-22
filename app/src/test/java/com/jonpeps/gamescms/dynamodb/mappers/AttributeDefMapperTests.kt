package com.jonpeps.gamescms.dynamodb.mappers

import com.jonpeps.gamescms.data.ItemType
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class AttributeDefMapperTests {
    private val mapper = CoreDynamoDbItemsMapper()

    @Test
    fun `test mapper name is same as argument and item type is string`() {
        val result = mapper.getAttributeDefinition("name", ItemType.STRING)
        assert(result.attributeName() == "name")
        assert(result.attributeType().name == "S")
    }

    @Test
    fun `test mapper name is same as argument and item type is int`() {
        val result = mapper.getAttributeDefinition("name", ItemType.INT)
        assert(result.attributeName() == "name")
        assert(result.attributeType().name == "N")
    }

    @Test
    fun `test mapper name is same as argument and item type is boolean`() {
        val result = mapper.getAttributeDefinition("name", ItemType.BOOLEAN)
        assert(result.attributeName() == "name")
        assert(result.attributeType().name == "S")
    }

    @Test
    fun `test mapper name is same as argument and item type is UTC date`() {
        val result = mapper.getAttributeDefinition("name", ItemType.UTC_DATE)
        assert(result.attributeName() == "name")
        assert(result.attributeType().name == "S")
    }
}