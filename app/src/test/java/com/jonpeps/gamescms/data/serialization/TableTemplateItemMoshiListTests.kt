package com.jonpeps.gamescms.data.serialization

import com.jonpeps.gamescms.data.dataclasses.ItemType
import com.jonpeps.gamescms.data.serialization.moshi.MoshiJsonAdapterCreator
import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateItemMoshi
import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateItemListMoshi
import com.jonpeps.gamescms.data.serialization.moshi.TableItemListMoshiSerialization
import com.squareup.moshi.JsonAdapter
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.IOException

@RunWith(JUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
class TableTemplateItemMoshiListTests {
    private val dispatcher = UnconfinedTestDispatcher()
    @MockK
    private lateinit var moshiJsonAdapterCreator: MoshiJsonAdapterCreator
    @MockK
    private lateinit var moshiJsonAdapter: JsonAdapter<TableTemplateItemListMoshi>

    private lateinit var serializer: TableItemListMoshiSerialization

    private val dummyData = TableTemplateItemListMoshi("test", listOf(TableTemplateItemMoshi("test", dataType = ItemType.STRING)))

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        moshiJsonAdapter = mockk<JsonAdapter<TableTemplateItemListMoshi>>()
        serializer = TableItemListMoshiSerialization(moshiJsonAdapterCreator, dispatcher)
    }

    @Test
    fun `test content converted from Json to table item template success`() = runTest(dispatcher) {
        assert(serializer.getItem() == null)
        assert(serializer.getErrorMsg() == "")
        every { serializer.getMoshiAdapter() } returns moshiJsonAdapter
        every { moshiJsonAdapter.fromJson("Dummy Json") } returns dummyData
        val success = serializer.fromJson("Dummy Json")
        assert(success)
    }

    @Test
    fun `test content converted from Json to table item template failure due to exception`() = runTest(dispatcher) {
        assert(serializer.getItem() == null)
        assert(serializer.getErrorMsg() == "")
        every { serializer.getMoshiAdapter() } returns moshiJsonAdapter
        every { moshiJsonAdapter.fromJson("Dummy Json") } throws IOException()
        val success = serializer.fromJson("Dummy Json")
        assert(!success)
        assert(serializer.getErrorMsg() != "")
    }

    @Test
    fun `test convert to table item template to Json success`() = runTest(dispatcher) {
        assert(serializer.getErrorMsg() == "")
        every { serializer.getMoshiAdapter() } returns moshiJsonAdapter
        every { moshiJsonAdapter.toJson(dummyData) } returns "Dummy Json"
        val success = serializer.toJson(dummyData)
        assert(success)
        assert(serializer.getErrorMsg() == "")
    }

    @Test
    fun `test convert to table item template to Json fails due to null string`()  = runTest(dispatcher) {
        every { serializer.getMoshiAdapter() } returns moshiJsonAdapter
        every { moshiJsonAdapter.toJson(dummyData) } returns null
        val success = serializer.toJson(dummyData)
        assert(!success)
    }

    @Test
    fun `test convert to table item template to Json fails due to empty string`()  = runTest(dispatcher) {
        assert(serializer.getErrorMsg() == "")
        every { serializer.getMoshiAdapter() } returns moshiJsonAdapter
        every { moshiJsonAdapter.toJson(dummyData) } returns ""
        val success = serializer.toJson(dummyData)
        assert(!success)
    }

    @Test
    fun `test convert to table item template to Json fails due to assertion exception`()  = runTest(dispatcher) {
        assert(serializer.getErrorMsg() == "")
        every { serializer.getMoshiAdapter() } returns moshiJsonAdapter
        every { moshiJsonAdapter.toJson(dummyData) } throws AssertionError()
        val success = serializer.toJson(dummyData)
        assert(!success)
        assert(serializer.getErrorMsg() != "")
    }
}