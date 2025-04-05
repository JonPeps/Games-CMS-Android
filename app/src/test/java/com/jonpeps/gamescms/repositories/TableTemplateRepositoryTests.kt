package com.jonpeps.gamescms.repositories

import com.jonpeps.gamescms.data.dataclasses.TableItem
import com.jonpeps.gamescms.data.dataclasses.TableItemList
import com.jonpeps.gamescms.data.serialization.moshi.ITableItemListMoshiSerialization
import com.jonpeps.gamescms.data.serialization.string.IStringFileStorageStrSerialisation
import com.jonpeps.gamescms.ui.tabletemplates.repositories.TableTemplateRepository
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
class TableTemplateRepositoryTests {
    @OptIn(ExperimentalCoroutinesApi::class)
    private val dispatcher = UnconfinedTestDispatcher()
    @Mock
    private lateinit var tableItemListMoshiSerialization: ITableItemListMoshiSerialization
    @Mock
    private lateinit var stringFileStorageStrSerialisation: IStringFileStorageStrSerialisation
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

    private val dummyData = TableItemList("test", listOf(TableItem(1, "test1")))

    private lateinit var tableTemplateRepository: TableTemplateRepository

    @Before
    fun setup() {
        tableTemplateRepository = getRepository()
        tableTemplateRepository.setAbsolutePath(absolutePath)
        tableTemplateRepository.setFile(file)
        tableTemplateRepository.setFilePath(filePath)
        tableTemplateRepository.setBufferReader(bufferedReader)
        tableTemplateRepository.setFileWriter(fileWriter)
    }

    @Test
    fun `test load template with read Json string success and Json convert success`() = runTest(dispatcher) {
        assert(tableTemplateRepository.getErrorMsg() == "")
        `when`(stringFileStorageStrSerialisation.read(absolutePath, bufferedReader)).thenReturn(true)
        `when`(tableItemListMoshiSerialization
            .fromJson(stringFileStorageStrSerialisation.getContents())).thenReturn(true)
        `when`(tableItemListMoshiSerialization.getItem()).thenReturn(dummyData)
        val result = tableTemplateRepository.loadTemplate()
        verify(tableItemListMoshiSerialization, times(1)).getItem()
        assert(tableTemplateRepository.getItem() == dummyData)
        assert(result)
    }

    @Test
    fun `test load template with read Json string false`() = runTest(dispatcher) {
        assert(tableTemplateRepository.getErrorMsg() == "")
        `when`(stringFileStorageStrSerialisation.read(absolutePath, bufferedReader)).thenReturn(false)
        val result = tableTemplateRepository.loadTemplate()
        verify(stringFileStorageStrSerialisation, times(1)).getErrorMsg()
        assert(stringFileStorageStrSerialisation.getErrorMsg() == tableTemplateRepository.getErrorMsg())
        assert(!result)
    }

    @Test
    fun `test load template fails with read Json file returns false`() = runTest(dispatcher) {
        assert(tableTemplateRepository.getErrorMsg() == "")
        `when`(stringFileStorageStrSerialisation.read(absolutePath, bufferedReader)).thenReturn(false)
        val result = tableTemplateRepository.loadTemplate()
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
        val result = tableTemplateRepository.loadTemplate()
        verify(tableItemListMoshiSerialization, times(1)).getErrorMsg()
        assert(tableTemplateRepository.getErrorMsg() == tableItemListMoshiSerialization.getErrorMsg())
        assert(!result)
    }

    @Test
    fun `test load template with read Json string fails`() = runTest(dispatcher) {
        assert(tableTemplateRepository.getErrorMsg() == "")
        `when`(stringFileStorageStrSerialisation.read(absolutePath, bufferedReader)).thenReturn(false)
        val result = tableTemplateRepository.loadTemplate()
        verify(stringFileStorageStrSerialisation, times(1)).getErrorMsg()
        assert(tableTemplateRepository.getErrorMsg() == stringFileStorageStrSerialisation.getErrorMsg())
        assert(!result)
    }



    private fun getRepository(): TableTemplateRepository {
        return TableTemplateRepository(stringFileStorageStrSerialisation, tableItemListMoshiSerialization)
    }
}