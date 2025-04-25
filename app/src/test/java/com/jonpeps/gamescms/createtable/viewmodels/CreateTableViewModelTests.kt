package com.jonpeps.gamescms.createtable.viewmodels

import com.jonpeps.gamescms.data.dataclasses.createtemplate.CreateTableItemData
import com.jonpeps.gamescms.data.dataclasses.ItemType
import com.jonpeps.gamescms.data.dataclasses.createtemplate.CreateTablePairData
import com.jonpeps.gamescms.dynamodb.mappers.DynamoDbCreateTableMapper
import com.jonpeps.gamescms.dynamodb.services.DynamoDbCreateTable
import com.jonpeps.gamescms.ui.createtable.viewmodels.CreateTableViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Before
import org.junit.Test
import software.amazon.awssdk.services.dynamodb.model.CreateTableResponse
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class CreateTableViewModelTests {
    private val dispatcher = UnconfinedTestDispatcher()
    @MockK
    private lateinit var dynamoDbCreateTable: DynamoDbCreateTable

    private lateinit var viewModel: CreateTableViewModel
    private lateinit var testItems: List<CreateTableItemData>

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        testItems =
            listOf(
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

        viewModel = CreateTableViewModel(dynamoDbCreateTable, dispatcher)

        mockkObject(DynamoDbCreateTableMapper.Companion) {
            val mockMapperResult = mockk<CreateTablePairData>()
            every { DynamoDbCreateTableMapper.mapToCreateTablePair(any()) } returns mockMapperResult
        }
    }

    @Test
    fun `test create table success with valid API response`() {
        coEvery { dynamoDbCreateTable.create(any(), any(), any()) } returns mockk<CreateTableResponse>()
        viewModel.createTable("test", testItems)
        assert(viewModel.state.value.success)
        assert(viewModel.state.value.response != null)
        assert(viewModel.state.value.exception == null)
    }

    @Test
    fun `test create table fails with API exception response`() {
        coEvery { dynamoDbCreateTable.create(any(), any(), any()) } throws mockk<IOException>()
        viewModel.createTable("test", testItems)
        assert(!viewModel.state.value.success)
        assert(viewModel.state.value.response == null)
        assert(viewModel.state.value.exception != null)
    }
}

