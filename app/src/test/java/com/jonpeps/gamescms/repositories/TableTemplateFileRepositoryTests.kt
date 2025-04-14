package com.jonpeps.gamescms.repositories

import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateItemMoshi
import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateItemListMoshi
import com.jonpeps.gamescms.data.serialization.moshi.ITableItemListMoshiSerialization
import com.jonpeps.gamescms.data.serialization.string.IStringFileStorageStrSerialisation
import com.jonpeps.gamescms.ui.tabletemplates.repositories.ITableTemplateFileRepository
import com.jonpeps.gamescms.ui.tabletemplates.repositories.TableTemplateFileRepository
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
class TableTemplateFileRepositoryTests {
    @OptIn(ExperimentalCoroutinesApi::class)
    private val dispatcher = UnconfinedTestDispatcher()
    @Mock
    private lateinit var stringFileStorageStrSerialisation: IStringFileStorageStrSerialisation
    @Mock
    private lateinit var tableItemListMoshiSerialization: ITableItemListMoshiSerialization
    @Mock
    private lateinit var filePath: File
    @Mock
    private lateinit var file: File
    @Mock
    private lateinit var absolutePath: File
    @Mock
    private lateinit var bufferedReader: BufferedReader
    @Mock
    private lateinit var fileWriter: FileWriter

    private val dummyData = TableTemplateItemListMoshi("test", listOf(TableTemplateItemMoshi(1, "test1")))

    private lateinit var tableTemplateRepository: ITableTemplateFileRepository

    @Before
    fun setup() {
        tableTemplateRepository = TableTemplateFileRepository(tableItemListMoshiSerialization, stringFileStorageStrSerialisation)
        tableTemplateRepository.setAbsolutePath(absolutePath)
        tableTemplateRepository.setFile(file)
        tableTemplateRepository.setFilePath(filePath)
        tableTemplateRepository.setBufferReader(bufferedReader)
        tableTemplateRepository.setFileWriter(fileWriter)
    }

    @Test
    fun `test load template success all paths`() = runTest(dispatcher) {
        assert(tableTemplateRepository.getItem() == null)
        assert(tableTemplateRepository.getErrorMsg() == "")
        `when`(stringFileStorageStrSerialisation.read(absolutePath, bufferedReader)).thenReturn(true)
        `when`(tableItemListMoshiSerialization.fromJson("")).thenReturn(true)
        `when`(stringFileStorageStrSerialisation.getContents()).thenReturn("")
        `when`(tableItemListMoshiSerialization.getItem()).thenReturn(dummyData)
        val result = tableTemplateRepository.load()
        verify(tableItemListMoshiSerialization, times(1)).getItem()
        assert(tableTemplateRepository.getItem() == dummyData)
        assert(result)
    }

    @Test
    fun `test load template with read Json string false`() = runTest(dispatcher) {
        assert(tableTemplateRepository.getErrorMsg() == "")
        `when`(stringFileStorageStrSerialisation.read(absolutePath, bufferedReader)).thenReturn(false)
        val result = tableTemplateRepository.load()
        verify(stringFileStorageStrSerialisation, times(1)).getErrorMsg()
        assert(stringFileStorageStrSerialisation.getErrorMsg() == tableTemplateRepository.getErrorMsg())
        assert(!result)
        assert(tableTemplateRepository.getItem() == null)
    }

    @Test
    fun `test load template fails with read Json file returns false`() = runTest(dispatcher) {
        assert(tableTemplateRepository.getErrorMsg() == "")
        `when`(stringFileStorageStrSerialisation.read(absolutePath, bufferedReader)).thenReturn(false)
        val result = tableTemplateRepository.load()
        verify(stringFileStorageStrSerialisation, times(1)).getErrorMsg()
        assert(tableTemplateRepository.getErrorMsg() == stringFileStorageStrSerialisation.getErrorMsg())
        assert(!result)
    }

    @Test
    fun `test load template with read Json file success but Json convert fails`() = runTest(dispatcher) {
        assert(tableTemplateRepository.getErrorMsg() == "")
        `when`(stringFileStorageStrSerialisation.read(absolutePath, bufferedReader)).thenReturn(true)
        `when`(tableItemListMoshiSerialization
            .fromJson(stringFileStorageStrSerialisation.getContents())).thenReturn(false)
        val result = tableTemplateRepository.load()
        verify(tableItemListMoshiSerialization, times(1)).getErrorMsg()
        assert(tableTemplateRepository.getErrorMsg() == tableItemListMoshiSerialization.getErrorMsg())
        assert(!result)
    }

    @Test
    fun `test save template success all paths`() = runTest(dispatcher) {
        assert(tableTemplateRepository.getErrorMsg() == "")
        `when`(tableItemListMoshiSerialization.toJson(dummyData)).thenReturn(true)
        `when`(tableItemListMoshiSerialization.getToJsonItem()).thenReturn("test")
        `when`(stringFileStorageStrSerialisation.write(filePath, file, fileWriter, "test")).thenReturn(true)
        val result = tableTemplateRepository.save(dummyData)
        verify(stringFileStorageStrSerialisation, times(1)).write(filePath, file, fileWriter, "test")
        assert(result)
        assert(tableTemplateRepository.getErrorMsg() == "")
    }

    @Test
    fun `test save template Json string convert failure`() = runTest(dispatcher) {
        assert(tableTemplateRepository.getErrorMsg() == "")
        `when`(tableItemListMoshiSerialization.toJson(dummyData)).thenReturn(false)
        val result = tableTemplateRepository.save(dummyData)
        assert(!result)
        verify(tableItemListMoshiSerialization, times(1)).getErrorMsg()
        assert(tableTemplateRepository.getErrorMsg() == tableItemListMoshiSerialization.getErrorMsg())
    }

    @Test
    fun `test save template with write Json string failure and get Json item is null`() = runTest(dispatcher) {
        assert(tableTemplateRepository.getErrorMsg() == "")
        `when`(tableItemListMoshiSerialization.toJson(dummyData)).thenReturn(true)
        `when`(tableItemListMoshiSerialization.getToJsonItem()).thenReturn(null)
        val result = tableTemplateRepository.save(dummyData)
        assert(!result)
        verify(tableItemListMoshiSerialization, times(1)).getErrorMsg()
        assert(tableTemplateRepository.getErrorMsg() == tableItemListMoshiSerialization.getErrorMsg())
    }

    @Test
    fun `test delete template success`() = runTest(dispatcher) {
        assert(tableTemplateRepository.getErrorMsg() == "")
        `when`(absolutePath.delete()).thenReturn(true)
        val result = tableTemplateRepository.deleteTemplate()
        assert(result)
    }

    @Test
    fun `test delete template failure`() = runTest(dispatcher) {
        assert(tableTemplateRepository.getErrorMsg() == "")
        `when`(absolutePath.delete()).thenReturn(false)
        val result = tableTemplateRepository.deleteTemplate()
        assert(!result)
        assert(tableTemplateRepository.getErrorMsg() == TableTemplateFileRepository.FAILED_TO_DELETE_FILE + absolutePath)
    }
}