package com.jonpeps.gamescms.repositories

import com.jonpeps.gamescms.data.dataclasses.StringListData
import com.jonpeps.gamescms.data.repositories.IMoshiStringListRepository
import com.jonpeps.gamescms.data.repositories.MoshiStringListRepository
import com.jonpeps.gamescms.data.serialization.moshi.IStringListMoshiSerialization
import com.jonpeps.gamescms.data.serialization.string.IStringFileStorageStrSerialisation
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import java.io.BufferedReader
import java.io.File
import java.io.FileWriter

@RunWith(MockitoJUnitRunner::class)
class MoshiStringListRepositoryTests {
    @OptIn(ExperimentalCoroutinesApi::class)
    private val dispatcher = UnconfinedTestDispatcher()
    @Mock
    private lateinit var stringFileStorageStrSerialisation: IStringFileStorageStrSerialisation
    @Mock
    private lateinit var strListMoshiSerialization: IStringListMoshiSerialization
    @Mock
    private lateinit var directoryFile: File
    @Mock
    private lateinit var file: File
    @Mock
    private lateinit var absolutePath: File
    @Mock
    private lateinit var bufferedReader: BufferedReader
    @Mock
    private lateinit var fileWriter: FileWriter

    private val dummyData = StringListData(arrayListOf("test1", "test2"))

    private lateinit var moshiStringListRepository: IMoshiStringListRepository

    @Before
    fun setup() {
        moshiStringListRepository = MoshiStringListRepository(strListMoshiSerialization, stringFileStorageStrSerialisation)
        moshiStringListRepository.setAbsolutePath(absolutePath)
        moshiStringListRepository.setFile(file)
        moshiStringListRepository.setDirectoryFile(directoryFile)
        moshiStringListRepository.setBufferReader(bufferedReader)
        moshiStringListRepository.setFileWriter(fileWriter)
    }

    @Test
    fun `test load string list success all paths`() = runTest(dispatcher) {
        assert(moshiStringListRepository.getErrorMsg() == "")
        `when`(stringFileStorageStrSerialisation.read(absolutePath, bufferedReader)).thenReturn(true)
        `when`(strListMoshiSerialization.fromJson("")).thenReturn(true)
        `when`(stringFileStorageStrSerialisation.getContents()).thenReturn("")
        `when`(strListMoshiSerialization.getItem()).thenReturn(dummyData)
        val result = moshiStringListRepository.load()
        verify(strListMoshiSerialization, times(1)).getItem()
        assert(strListMoshiSerialization.getItem() == dummyData)
        assert(result)
    }

    @Test
    fun `test load string list with read Json string false`() = runTest(dispatcher) {
        assert(moshiStringListRepository.getErrorMsg() == "")
        `when`(stringFileStorageStrSerialisation.read(absolutePath, bufferedReader)).thenReturn(false)
        val result = moshiStringListRepository.load()
        verify(stringFileStorageStrSerialisation, times(1)).getErrorMsg()
        assert(stringFileStorageStrSerialisation.getErrorMsg() == moshiStringListRepository.getErrorMsg())
        assert(!result)
    }

    @Test
    fun `test load string list fails with read Json file returns false`() = runTest(dispatcher) {
        assert(moshiStringListRepository.getErrorMsg() == "")
        `when`(stringFileStorageStrSerialisation.read(absolutePath, bufferedReader)).thenReturn(false)
        val result = moshiStringListRepository.load()
        verify(stringFileStorageStrSerialisation, times(1)).getErrorMsg()
        assert(moshiStringListRepository.getErrorMsg() == stringFileStorageStrSerialisation.getErrorMsg())
        assert(!result)
    }

    @Test
    fun `test load string list with read Json file success but Json convert fails`() = runTest(dispatcher) {
        assert(moshiStringListRepository.getErrorMsg() == "")
        `when`(stringFileStorageStrSerialisation.read(absolutePath, bufferedReader)).thenReturn(true)
        `when`(strListMoshiSerialization
            .fromJson(stringFileStorageStrSerialisation.getContents())).thenReturn(false)
        val result = moshiStringListRepository.load()
        verify(strListMoshiSerialization, times(1)).getErrorMsg()
        assert(moshiStringListRepository.getErrorMsg() == strListMoshiSerialization.getErrorMsg())
        assert(!result)
    }

    @Test
    fun `test save string list success all paths`() = runTest(dispatcher) {
        assert(moshiStringListRepository.getErrorMsg() == "")
        `when`(strListMoshiSerialization.toJson(dummyData)).thenReturn(true)
        `when`(strListMoshiSerialization.getToJsonItem()).thenReturn("test")
        `when`(stringFileStorageStrSerialisation.write(directoryFile, file, fileWriter, "test")).thenReturn(true)
        val result = moshiStringListRepository.save(dummyData)
        verify(stringFileStorageStrSerialisation, times(1)).write(directoryFile, file, fileWriter, "test")
        assert(result)
        assert(moshiStringListRepository.getErrorMsg() == "")
    }

    @Test
    fun `test save string list Json string convert failure`() = runTest(dispatcher) {
        assert(moshiStringListRepository.getErrorMsg() == "")
        `when`(strListMoshiSerialization.toJson(dummyData)).thenReturn(false)
        val result = moshiStringListRepository.save(dummyData)
        assert(!result)
        verify(strListMoshiSerialization, times(1)).getErrorMsg()
        assert(moshiStringListRepository.getErrorMsg() == strListMoshiSerialization.getErrorMsg())
    }

    @Test
    fun `test save string list with write Json string failure`() = runTest(dispatcher) {
        assert(moshiStringListRepository.getErrorMsg() == "")
        `when`(strListMoshiSerialization.toJson(dummyData)).thenReturn(true)
        `when`(strListMoshiSerialization.getToJsonItem()).thenReturn("test")
        `when`(stringFileStorageStrSerialisation.write(directoryFile, file, fileWriter, "test")).thenReturn(false)
        val result = moshiStringListRepository.save(dummyData)
        assert(!result)
        verify(stringFileStorageStrSerialisation, times(1)).getErrorMsg()
        assert(moshiStringListRepository.getErrorMsg() == stringFileStorageStrSerialisation.getErrorMsg())
    }

    @Test
    fun `test save string list with write Json string failure and get Json item is null`() = runTest(dispatcher) {
        assert(moshiStringListRepository.getErrorMsg() == "")
        `when`(strListMoshiSerialization.toJson(dummyData)).thenReturn(true)
        `when`(strListMoshiSerialization.getToJsonItem()).thenReturn(null)
        val result = moshiStringListRepository.save(dummyData)
        assert(!result)
        verify(strListMoshiSerialization, times(1)).getErrorMsg()
        assert(moshiStringListRepository.getErrorMsg() == strListMoshiSerialization.getErrorMsg())
    }
}