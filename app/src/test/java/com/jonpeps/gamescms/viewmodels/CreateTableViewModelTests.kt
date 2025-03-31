package com.jonpeps.gamescms.viewmodels

import com.jonpeps.gamescms.data.dataclasses.CreateTableItemData
import com.jonpeps.gamescms.data.dataclasses.CreateTablePairData
import com.jonpeps.gamescms.data.dataclasses.ItemType
import com.jonpeps.gamescms.dynamodb.mappers.IDynamoDbCreateTableMapper
import com.jonpeps.gamescms.dynamodb.services.DynamoDbCreateTable
import com.jonpeps.gamescms.ui.createtable.viewmodels.CreateTableViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.exceptions.base.MockitoException
import org.mockito.junit.MockitoJUnitRunner
import software.amazon.awssdk.services.dynamodb.model.CreateTableResponse


@RunWith(MockitoJUnitRunner::class)
class CreateTableViewModelTests {
    @OptIn(ExperimentalCoroutinesApi::class)
    private val dispatcher = UnconfinedTestDispatcher()
    @Mock
    private lateinit var dynamoDbCreateTable: DynamoDbCreateTable
    @Mock
    private lateinit var dynamoDbCreateTableMapper: IDynamoDbCreateTableMapper

    private lateinit var viewModel: CreateTableViewModel
    private var testItems: List<CreateTableItemData>? = null

    @Before
    fun setup() {
        testItems =
            listOf(
                CreateTableItemData("id",
                    ItemType.INT,
                    isPrimary = true,
                    isSortKey = false
                ),
                CreateTableItemData(
                    "score",
                    ItemType.INT,
                    isPrimary = false,
                    isSortKey = true
                ),
                CreateTableItemData(
                    "player",
                    ItemType.STRING,
                    isPrimary = false,
                    isSortKey = false
                ),
                CreateTableItemData(
                    "email",
                    ItemType.STRING,
                    isPrimary = false,
                    isSortKey = false
                )
            )

        dynamoDbCreateTable = Mockito.mock(DynamoDbCreateTable::class.java)
        dynamoDbCreateTableMapper = Mockito.mock(IDynamoDbCreateTableMapper::class.java)
        viewModel = CreateTableViewModel(dynamoDbCreateTable, dynamoDbCreateTableMapper, dispatcher)
    }

    @Test
    fun `test create table success with valid API response`() = runTest(dispatcher) {
        val mapperResponse = Mockito.mock(CreateTablePairData::class.java)
        Mockito.`when`(dynamoDbCreateTableMapper.mapToCreateTablePair(testItems!!)).thenReturn(mapperResponse)
        val createTableResponse = Mockito.mock(CreateTableResponse::class.java)
        Mockito.`when`(dynamoDbCreateTable.create("testTable", listOf(), listOf())).thenReturn(createTableResponse)
        viewModel.createTable("testTable", testItems!!)
        val response = viewModel.state.value
        assert(response.success)
        assert(response.response == createTableResponse)
        assert(response.exception == null)
    }

    @Test
    fun `test create table fails with API exception response`() = runTest(dispatcher) {
        val mapperResponse = Mockito.mock(CreateTablePairData::class.java)
        Mockito.`when`(dynamoDbCreateTableMapper.mapToCreateTablePair(testItems!!)).thenReturn(mapperResponse)
        Mockito.`when`(dynamoDbCreateTable.create("testTable", listOf(), listOf())).thenThrow(
            MockitoException::class.java)
        viewModel.createTable("testTable", testItems!!)
        val response = viewModel.state.value
        assert(!response.success)
        assert(response.response == null)
        assert(response.exception != null)
    }
}

