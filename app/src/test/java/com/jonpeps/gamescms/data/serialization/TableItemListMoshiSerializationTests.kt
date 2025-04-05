package com.jonpeps.gamescms.data.serialization

import com.jonpeps.gamescms.data.serialization.moshi.MoshiJsonAdapterCreator
import com.jonpeps.gamescms.data.dataclasses.TableItem
import com.jonpeps.gamescms.data.dataclasses.TableItemList
import com.jonpeps.gamescms.data.serialization.moshi.TableItemListMoshiSerialization
import com.squareup.moshi.JsonAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import java.io.IOException

@Suppress("UNCHECKED_CAST")
@RunWith(MockitoJUnitRunner::class)
class TableItemListMoshiSerializationTests {
    @OptIn(ExperimentalCoroutinesApi::class)
    private val dispatcher = UnconfinedTestDispatcher()
    @Mock
    private lateinit var moshiJsonAdapterCreator: MoshiJsonAdapterCreator
    @Mock
    private lateinit var moshiJsonAdapter: JsonAdapter<TableItemList>

    private lateinit var serializer: TableItemListMoshiSerialization

    private val dummyData = TableItemList("test", listOf(TableItem(1, "test1")))

    @Before
    fun setup() {
        moshiJsonAdapter = mock(JsonAdapter::class.java) as JsonAdapter<TableItemList>
        serializer = TableItemListMoshiSerialization(moshiJsonAdapterCreator, dispatcher)
    }

    @Test
    fun `test content converted from Json to table item template success`() = runTest(dispatcher) {
        assert(serializer.getItem() == null)
        assert(serializer.getErrorMsg() == "")
        Mockito.`when`(serializer.getMoshiAdapter()).thenReturn(moshiJsonAdapter)
        Mockito.`when`(moshiJsonAdapter.fromJson("Dummy Json")).thenReturn(dummyData)
        val success = serializer.fromJson("Dummy Json")
        assert(success)
    }

    @Test
    fun `test content converted from Json to table item template failure due to exception`() = runTest(dispatcher) {
        assert(serializer.getItem() == null)
        assert(serializer.getErrorMsg() == "")
        Mockito.`when`(serializer.getMoshiAdapter()).thenReturn(moshiJsonAdapter)
        Mockito.`when`(moshiJsonAdapter.fromJson("Dummy Json")).thenThrow(IOException())
        val success = serializer.fromJson("Dummy Json")
        assert(!success)
        assert(serializer.getErrorMsg() != "")
    }

    @Test
    fun `test convert to table item template to Json success`() = runTest(dispatcher) {
        assert(serializer.getErrorMsg() == "")
        Mockito.`when`(serializer.getMoshiAdapter()).thenReturn(moshiJsonAdapter)
        Mockito.`when`(moshiJsonAdapter.toJson(dummyData)).thenReturn("Dummy Json")
        val success = serializer.toJson(dummyData)
        assert(success)
        assert(serializer.getErrorMsg() == "")
    }

    @Test
    fun `test convert to table item template to Json fails due to null string`()  = runTest(dispatcher) {
        Mockito.`when`(serializer.getMoshiAdapter()).thenReturn(moshiJsonAdapter)
        Mockito.`when`(moshiJsonAdapter.toJson(dummyData)).thenReturn(null)
        val success = serializer.toJson(dummyData)
        assert(!success)
    }

    @Test
    fun `test convert to table item template to Json fails due to empty string`()  = runTest(dispatcher) {
        assert(serializer.getErrorMsg() == "")
        Mockito.`when`(serializer.getMoshiAdapter()).thenReturn(moshiJsonAdapter)
        Mockito.`when`(moshiJsonAdapter.toJson(dummyData)).thenReturn("")
        val success = serializer.toJson(dummyData)
        assert(!success)
    }

    @Test
    fun `test convert to table item template to Json fails due to assertion exception`()  = runTest(dispatcher) {
        assert(serializer.getErrorMsg() == "")
        Mockito.`when`(serializer.getMoshiAdapter()).thenReturn(moshiJsonAdapter)
        Mockito.`when`(moshiJsonAdapter.toJson(dummyData)).thenThrow(AssertionError())
        val success = serializer.toJson(dummyData)
        assert(!success)
        assert(serializer.getErrorMsg() != "")
    }
}