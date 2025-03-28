package com.jonpeps.gamescms.data.serialization

import com.jonpeps.gamescms.data.core.StringSerialization
import com.jonpeps.gamescms.data.core.IoSerializationStringStatus
import com.jonpeps.gamescms.data.core.SerializeStringStatusGroup
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
import org.mockito.junit.MockitoJUnitRunner
import java.io.BufferedReader
import java.io.FileWriter

@Suppress("UNCHECKED_CAST")
@RunWith(MockitoJUnitRunner::class)
class MoshiSerializationTests {
    @OptIn(ExperimentalCoroutinesApi::class)
    private val dispatcher = UnconfinedTestDispatcher()
    @Mock
    private lateinit var bufferedReader: BufferedReader
    @Mock
    private lateinit var fileWriter: FileWriter
    @Mock
    private lateinit var serializeString: StringSerialization
    @Mock
    private lateinit var moshiJsonAdapter: JsonAdapter<TableItemList>

    private lateinit var serializer: MoshiSerialization

    @Before
    fun setup() {
        bufferedReader = Mockito.mock(BufferedReader::class.java)
        fileWriter = Mockito.mock(FileWriter::class.java)
        serializeString = Mockito.mock(StringSerialization::class.java)
        moshiJsonAdapter = Mockito.mock(JsonAdapter::class.java) as JsonAdapter<TableItemList>
        serializer = MoshiSerialization(moshiJsonAdapter, serializeString, dispatcher)
    }

    @Test
    fun `test load Json and convert to table item template success`() = runTest(dispatcher) {
        Mockito.`when`(serializeString.read(bufferedReader)).thenReturn("test Json")
        Mockito.`when`(serializeString.getStatus()).thenReturn(SerializeStringStatusGroup(
            IoSerializationStringStatus.SUCCESS, ""))
        Mockito.`when`(moshiJsonAdapter.fromJson("test Json")).thenReturn(TableItemList("test", arrayListOf()))
        serializer.load("testFile.txt", bufferedReader)
        Mockito.verify(serializeString).read(bufferedReader)
        Mockito.verify(serializeString).getStatus()
        Mockito.verify(moshiJsonAdapter).fromJson("test Json")
    }


}