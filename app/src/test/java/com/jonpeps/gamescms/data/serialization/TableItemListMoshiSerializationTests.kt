package com.jonpeps.gamescms.data.serialization

import com.jonpeps.gamescms.data.core.MoshiJsonAdapterCreator
import com.jonpeps.gamescms.data.dataclasses.TableItem
import com.jonpeps.gamescms.data.dataclasses.TableItemList
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
import java.io.BufferedReader
import java.io.FileWriter
import java.io.IOException

@Suppress("UNCHECKED_CAST")
@RunWith(MockitoJUnitRunner::class)
class TableItemListMoshiSerializationTests {
    @OptIn(ExperimentalCoroutinesApi::class)
    private val dispatcher = UnconfinedTestDispatcher()
    @Mock
    private lateinit var bufferedReader: BufferedReader
    @Mock
    private lateinit var fileWriter: FileWriter
    @Mock
    private lateinit var serializeString: StringSerialization
    @Mock
    private lateinit var moshiJsonAdapterCreator: MoshiJsonAdapterCreator
    @Mock
    private lateinit var moshiJsonAdapter: JsonAdapter<TableItemList>

    @Mock
    private lateinit var mockDummyData: TableItemList

    private lateinit var serializer: TableItemListMoshiSerialization

    private val dummyData = TableItemList("test", listOf(TableItem(1, "test1")))

    @Before
    fun setup() {
        bufferedReader = mock(BufferedReader::class.java)
        fileWriter = mock(FileWriter::class.java)
        serializeString = mock(StringSerialization::class.java)
        moshiJsonAdapterCreator = mock(MoshiJsonAdapterCreator::class.java)
        moshiJsonAdapter = mock(JsonAdapter::class.java) as JsonAdapter<TableItemList>
        mockDummyData = mock(TableItemList::class.java)
        serializer = TableItemListMoshiSerialization(moshiJsonAdapterCreator, serializeString, dispatcher)
    }

    @Test
    fun `test load Json and convert to table item template success`() = runTest(dispatcher) {
        Mockito.`when`(serializeString.read(bufferedReader)).thenReturn(true)
        Mockito.`when`(serializer.getMoshiAdapter()).thenReturn(moshiJsonAdapter)
        Mockito.`when`(moshiJsonAdapter.fromJson("")).thenReturn(dummyData)
        Mockito.`when`(serializeString.getContents()).thenReturn("")
        val success = serializer.load("", bufferedReader)
        assert(success)
        assert(serializer.getItem() == dummyData)
    }

    @Test
    fun `test load Json and failure to read string error`() = runTest(dispatcher) {
        Mockito.`when`(serializeString.read(bufferedReader)).thenReturn(false)
        val success = serializer.load("", bufferedReader)
        assert(!success)
        assert(serializer.getItem() == null)
        assert(serializer.getErrorMsg() != "")
    }

    @Test
    fun `test load Json fails due to IOException thrown when parsing Json`() = runTest(dispatcher) {
        Mockito.`when`(serializeString.read(bufferedReader)).thenReturn(true)
        Mockito.`when`(serializer.getMoshiAdapter()).thenReturn(moshiJsonAdapter)
        Mockito.`when`(moshiJsonAdapter.fromJson("")).thenThrow(mock(IOException::class.java))
        Mockito.`when`(serializeString.getContents()).thenReturn("")
        val success = serializer.load("", bufferedReader)
        assert(!success)
        assert(serializer.getItem() == null)
        assert(serializer.getErrorMsg() != "")
    }

    @Test
    fun `test save table item to Json string success`() = runTest(dispatcher) {
        Mockito.`when`(serializeString.write("test.txt", fileWriter, "test")).thenReturn(true)
        Mockito.`when`(serializer.getMoshiAdapter()).thenReturn(moshiJsonAdapter)
        Mockito.`when`(moshiJsonAdapter.toJson(mockDummyData)).thenReturn("test")
        val success = serializer.save("test.txt", mockDummyData, fileWriter)
        assert(success)
    }

    @Test
    fun `test save table item to Json string failure when write string fails`() = runTest(dispatcher) {
        Mockito.`when`(serializeString.write("test.txt", fileWriter, "test")).thenReturn(false)
        Mockito.`when`(serializer.getMoshiAdapter()).thenReturn(moshiJsonAdapter)
        Mockito.`when`(moshiJsonAdapter.toJson(mockDummyData)).thenReturn("test")
        Mockito.`when`(serializeString.getErrorMsg()).thenReturn("error")
        val success = serializer.save("test.txt", mockDummyData, fileWriter)
        assert(!success)
        assert(serializer.getErrorMsg() == "error")
    }

    @Test
    fun `test save table item to Json string failure when conversion to Json is empty string`() = runTest(dispatcher) {
        Mockito.`when`(serializer.getMoshiAdapter()).thenReturn(moshiJsonAdapter)
        Mockito.`when`(moshiJsonAdapter.toJson(mockDummyData)).thenReturn("")
        val success = serializer.save("test.txt", mockDummyData, fileWriter)
        assert(!success)
        assert(serializer.getErrorMsg() == "Failed to write to file test.txt!")
    }

    @Test
    fun `test save table item to Json string failure when conversion to Json is null string`() = runTest(dispatcher) {
        Mockito.`when`(serializer.getMoshiAdapter()).thenReturn(moshiJsonAdapter)
        Mockito.`when`(moshiJsonAdapter.toJson(mockDummyData)).thenReturn(null)
        val success = serializer.save("test.txt", mockDummyData, fileWriter)
        assert(!success)
        assert(serializer.getErrorMsg() == "Failed to write to file test.txt!")
    }

    @Test
    fun `test save table item to Json string failure when write IOException is thrown`() = runTest(dispatcher) {
        Mockito.`when`(serializer.getMoshiAdapter()).thenReturn(moshiJsonAdapter)
        Mockito.`when`(moshiJsonAdapter.toJson(mockDummyData)).thenThrow(mock(AssertionError::class.java))
        val success = serializer.save("test.txt", mockDummyData, fileWriter)
        assert(!success)
        assert(serializer.getErrorMsg() != "")
    }
}