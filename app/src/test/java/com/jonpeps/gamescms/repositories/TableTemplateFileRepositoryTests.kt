package com.jonpeps.gamescms.repositories

import com.jonpeps.gamescms.data.dataclasses.ItemType
import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateItemMoshi
import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateItemListMoshi
import com.jonpeps.gamescms.data.serialization.moshi.ITableItemListMoshiSerialization
import com.jonpeps.gamescms.data.serialization.string.IStringFileStorageStrSerialisation
import com.jonpeps.gamescms.ui.tabletemplates.repositories.ITableTemplateFileRepository
import com.jonpeps.gamescms.ui.tabletemplates.repositories.ITableTemplateStringListMoshiJsonCache
import com.jonpeps.gamescms.ui.tabletemplates.repositories.TableTemplateFileRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.BufferedReader
import java.io.File
import java.io.FileWriter

@RunWith(JUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
class TableTemplateFileRepositoryTests {
    private val dispatcher = UnconfinedTestDispatcher()
    @MockK
    private lateinit var stringFileStorageStrSerialisation: IStringFileStorageStrSerialisation
    @MockK
    private lateinit var tableItemListMoshiSerialization: ITableItemListMoshiSerialization
    @MockK
    private lateinit var tableTemplateStringListMoshiJsonCache: ITableTemplateStringListMoshiJsonCache
    @MockK
    private lateinit var directoryFile: File
    @MockK
    private lateinit var file: File
    @MockK
    private lateinit var absolutePath: File
    @MockK
    private lateinit var bufferedReader: BufferedReader
    @MockK
    private lateinit var fileWriter: FileWriter

    private lateinit var tableTemplateRepository: ITableTemplateFileRepository

    private val templateName = "test"
    private val dummyData = TableTemplateItemListMoshi("test", listOf(TableTemplateItemMoshi("test", dataType = ItemType.STRING)))

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        tableTemplateRepository = TableTemplateFileRepository(
            tableItemListMoshiSerialization,
            tableTemplateStringListMoshiJsonCache,
            stringFileStorageStrSerialisation)

        tableTemplateRepository.setAbsoluteFile(absolutePath)
        tableTemplateRepository.setFile(file)
        tableTemplateRepository.setDirectoryFile(directoryFile)
        tableTemplateRepository.setBufferReader(bufferedReader)
        tableTemplateRepository.setFileWriter(fileWriter)
    }

    @Test
    fun `test load template success all paths`() = runTest(dispatcher) {
        assert(tableTemplateRepository.getItem(templateName) == null)
        assert(tableTemplateRepository.getErrorMsg() == "")
        every { stringFileStorageStrSerialisation.getContents() } returns "test"
        coEvery { tableItemListMoshiSerialization.fromJson("test") } returns true
        every { tableItemListMoshiSerialization.getItem() } returns dummyData
        coEvery { stringFileStorageStrSerialisation.read(absolutePath, bufferedReader) } returns true
        val result = tableTemplateRepository.load(templateName)
        assert(result)
    }

    @Test
    fun `test load template with read Json string false`() = runTest(dispatcher) {
        assert(tableTemplateRepository.getErrorMsg() == "")
        every { stringFileStorageStrSerialisation.getErrorMsg() } returns "An error occurred!"
        every { tableItemListMoshiSerialization.getErrorMsg() } returns stringFileStorageStrSerialisation.getErrorMsg()
        every { stringFileStorageStrSerialisation.getContents() } returns "test"
        coEvery { tableItemListMoshiSerialization.fromJson("test") } returns false
        every { tableItemListMoshiSerialization.getItem() } returns dummyData
        coEvery { stringFileStorageStrSerialisation.read(absolutePath, bufferedReader) } returns true
        val result = tableTemplateRepository.load(templateName)
        assert(tableTemplateRepository.getErrorMsg() == stringFileStorageStrSerialisation.getErrorMsg())
        assert(!result)
        assert(tableTemplateRepository.getItem(templateName) == null)
    }

    @Test
    fun `test load template fails with read Json file returns false`() = runTest(dispatcher) {
        assert(tableTemplateRepository.getErrorMsg() == "")
        coEvery { stringFileStorageStrSerialisation.read(absolutePath, bufferedReader) } returns false
        every { stringFileStorageStrSerialisation.getErrorMsg() } returns "An error occurred!"
        val result = tableTemplateRepository.load(templateName)
        assert(tableTemplateRepository.getErrorMsg() == stringFileStorageStrSerialisation.getErrorMsg())
        assert(!result)
    }

    @Test
    fun `test load template with read Json file success but Json convert fails`() = runTest(dispatcher) {
        assert(tableTemplateRepository.getErrorMsg() == "")
        coEvery { (stringFileStorageStrSerialisation.read(absolutePath, bufferedReader)) } returns true
        every { stringFileStorageStrSerialisation.getContents() } returns "test"
        coEvery { tableItemListMoshiSerialization.fromJson("test") } returns false
        every { tableItemListMoshiSerialization.getErrorMsg() } returns "An error occurred!"
        val result = tableTemplateRepository.load(templateName)
        assert(tableTemplateRepository.getErrorMsg() == tableItemListMoshiSerialization.getErrorMsg())
        assert(!result)
    }

    @Test
    fun `test save template success all paths`() = runTest(dispatcher) {
        assert(tableTemplateRepository.getErrorMsg() == "")
        coEvery { tableItemListMoshiSerialization.toJson(dummyData) } returns true
        every { tableItemListMoshiSerialization.getToJsonItem() } returns "test"
        coEvery { stringFileStorageStrSerialisation.write(directoryFile, file, absolutePath, fileWriter, "test") } returns true
        val result = tableTemplateRepository.save(templateName, dummyData)
        assert(result)
        assert(tableTemplateRepository.getErrorMsg() == "")
    }

    @Test
    fun `test save template with read Json string failure`() = runTest(dispatcher) {
        assert(tableTemplateRepository.getErrorMsg() == "")
        coEvery { tableItemListMoshiSerialization.toJson(dummyData) } returns true
        every { tableItemListMoshiSerialization.getToJsonItem() } returns "test"
        coEvery {
            stringFileStorageStrSerialisation.write(
                directoryFile,
                file,
                absolutePath,
                fileWriter,
                "test"
            )
        } returns false
        every { stringFileStorageStrSerialisation.getErrorMsg() } returns "An error occurred!"
        val result = tableTemplateRepository.save(templateName, dummyData)
        assert(!result)
    }

    @Test
    fun `test save template with write Json string failure`() = runTest(dispatcher) {
        assert(tableTemplateRepository.getErrorMsg() == "")
        coEvery { tableItemListMoshiSerialization.toJson(dummyData) } returns true
        every { tableItemListMoshiSerialization.getToJsonItem() } returns "test"
        coEvery { stringFileStorageStrSerialisation.write(directoryFile, file, absolutePath, fileWriter, "test") } returns false
        every { stringFileStorageStrSerialisation.getErrorMsg() } returns "An error occurred!"
        val result = tableTemplateRepository.save(templateName, dummyData)
        assert(!result)
        assert(tableTemplateRepository.getErrorMsg() == stringFileStorageStrSerialisation.getErrorMsg())
    }

    @Test
    fun `test save template Json string convert failure`() = runTest(dispatcher) {
        assert(tableTemplateRepository.getErrorMsg() == "")
        coEvery { tableItemListMoshiSerialization.toJson(dummyData) } returns false
        every { tableItemListMoshiSerialization.getErrorMsg() } returns "An error occurred!"
        val result = tableTemplateRepository.save(templateName, dummyData)
        assert(!result)
        assert(tableTemplateRepository.getErrorMsg() == tableItemListMoshiSerialization.getErrorMsg())
    }

    @Test
    fun `test save template with write Json string failure and get Json item is null`() = runTest(dispatcher) {
        assert(tableTemplateRepository.getErrorMsg() == "")
        coEvery { tableItemListMoshiSerialization.toJson(dummyData) } returns true
        every { tableItemListMoshiSerialization.getToJsonItem() } returns null
        every { tableItemListMoshiSerialization.getErrorMsg() } returns "An error occurred!"
        val result = tableTemplateRepository.save(templateName, dummyData)
        assert(!result)
        assert(tableTemplateRepository.getErrorMsg() == tableItemListMoshiSerialization.getErrorMsg())
    }

    @Test
    fun `test delete template success`() = runTest(dispatcher) {
        assert(tableTemplateRepository.getErrorMsg() == "")
        every { absolutePath.delete() } returns true
        val result = tableTemplateRepository.deleteTemplate()
        assert(result)
    }

    @Test
    fun `test delete template failure`() = runTest(dispatcher) {
        assert(tableTemplateRepository.getErrorMsg() == "")
        every { absolutePath.delete() } returns false
        val result = tableTemplateRepository.deleteTemplate()
        assert(!result)
        assert(tableTemplateRepository.getErrorMsg() == TableTemplateFileRepository.FAILED_TO_DELETE_FILE + absolutePath)
    }
}