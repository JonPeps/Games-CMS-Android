package com.jonpeps.gamescms.dynamodb.requests

import com.jonpeps.gamescms.dynamodb.services.DynamoDbCreateTable
import com.jonpeps.gamescms.dynamodb.services.DynamoDbDeleteItem
import com.jonpeps.gamescms.dynamodb.services.DynamoDbDeleteTable
import com.jonpeps.gamescms.dynamodb.services.DynamoDbGetItem
import com.jonpeps.gamescms.dynamodb.services.DynamoDbPutItem
import com.jonpeps.gamescms.dynamodb.services.DynamoDbQueryTable
import com.jonpeps.gamescms.dynamodb.services.DynamoDbScanTable
import com.jonpeps.gamescms.dynamodb.services.DynamoDbUpdateItem
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.AttributeValueUpdate
import software.amazon.awssdk.services.dynamodb.model.CreateTableRequest
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest
import software.amazon.awssdk.services.dynamodb.model.DeleteTableRequest
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest
import software.amazon.awssdk.services.dynamodb.model.QueryRequest
import software.amazon.awssdk.services.dynamodb.model.Condition
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest

@RunWith(JUnit4::class)
class DynamoDbRequestTests {
    @MockK
    private lateinit var dynamoDbClient: DynamoDbClient

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `dynamoDb create table test`() = runBlocking {
        val dynamoDbCreateTable = DynamoDbCreateTable(dynamoDbClient)
        val attributeDefinitions = listOf(mockk<AttributeDefinition>())
        val schemas = listOf(mockk<KeySchemaElement>())
        val request =
            CreateTableRequest
                .builder()
                .tableName("table")
                .attributeDefinitions(attributeDefinitions)
                .keySchema(schemas)
                .build()
        coEvery { dynamoDbCreateTable.create("table", attributeDefinitions, schemas) } returns mockk()

        dynamoDbCreateTable.create("table", attributeDefinitions, schemas)

        verify { dynamoDbClient.createTable(request) }
    }

    @Test
    fun `dynamoDb delete item test`() = runBlocking {
        val dynamoDbDeleteItem = DynamoDbDeleteItem(dynamoDbClient)
        val key = mapOf<String, AttributeValue>()
        val request = DeleteItemRequest
            .builder()
            .tableName("table")
            .key(key)
            .build()
        coEvery { dynamoDbDeleteItem.delete("table", key) } returns mockk()

        dynamoDbDeleteItem.delete("table", key)

        verify { dynamoDbClient.deleteItem(request) }
    }

    @Test
    fun `delete table request test`() = runBlocking {
        val dynamoDbDeleteTable = DynamoDbDeleteTable(dynamoDbClient)
        val request = DeleteTableRequest.builder().tableName("table").build()
        coEvery { dynamoDbDeleteTable.delete("table") } returns mockk()

        dynamoDbDeleteTable.delete("table")

        verify { dynamoDbClient.deleteTable(request) }
    }

    @Test
    fun `get item request test`() = runBlocking {
        val dynamoDbGetItem = DynamoDbGetItem(dynamoDbClient)
        val key = mapOf<String, AttributeValue>()
        val request = GetItemRequest.builder()
            .tableName("table")
            .key(key)
            .build()
        coEvery { dynamoDbGetItem.getItem("table", key) } returns mockk()

        dynamoDbGetItem.getItem("table", key)

        verify { dynamoDbClient.getItem(request) }
    }

    @Test
    fun `put item request test`() = runBlocking {
        val dynamoDbPutItem = DynamoDbPutItem(dynamoDbClient)
        val attributes = mapOf<String, AttributeValue>()
        val request = PutItemRequest.builder()
            .tableName("table")
            .item(attributes)
            .build()
        coEvery { dynamoDbPutItem.putItem("table", attributes) } returns mockk()

        dynamoDbPutItem.putItem("table", attributes)

        verify { dynamoDbClient.putItem(request) }
    }

    @Test
    fun `query table request test`() = runBlocking {
        val dynamoDbQueryTable = DynamoDbQueryTable(dynamoDbClient)
        val queryFilter = mapOf<String, Condition>()
        val request = QueryRequest.builder()
            .tableName("table")
            .queryFilter(queryFilter)
            .build()
        coEvery { dynamoDbQueryTable.query("table", queryFilter) } returns mockk()

        dynamoDbQueryTable.query("table", queryFilter)

        verify { dynamoDbClient.query(request) }
    }

    @Test
    fun `scan table request test`() = runBlocking {
        val dynamoDbScanTable = DynamoDbScanTable(dynamoDbClient)
        val scanFilter = mapOf<String, Condition>()
        val request = software.amazon.awssdk.services.dynamodb.model.ScanRequest.builder()
            .tableName("table")
            .scanFilter(scanFilter)
            .build()

        coEvery { dynamoDbScanTable.scan("table", scanFilter) } returns mockk()

        dynamoDbScanTable.scan("table", scanFilter)

        verify { dynamoDbClient.scan(request) }
    }

    @Test
    fun `update item request test`() = runBlocking {
        val dynamoDbUpdateItem = DynamoDbUpdateItem(dynamoDbClient)
        val values = mapOf<String, AttributeValueUpdate>()
        val request = UpdateItemRequest.builder()
            .tableName("table")
            .attributeUpdates(values)
            .build()
        coEvery { dynamoDbUpdateItem.updateItem("table", values) } returns mockk()

        dynamoDbUpdateItem.updateItem("table", values)

        verify { dynamoDbClient.updateItem(request) }
    }
}