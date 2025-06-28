package com.jonpeps.gamescms.repositories

import com.jonpeps.gamescms.data.dataclasses.ItemType
import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateItemMoshi
import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateItemListMoshi
import com.jonpeps.gamescms.data.serialization.moshi.ITableItemListMoshiSerialization
import com.jonpeps.gamescms.data.serialization.string.IStringFileStorageStrSerialisation
import com.jonpeps.gamescms.ui.tabletemplates.repositories.ITableTemplateFileRepository
import com.jonpeps.gamescms.ui.tabletemplates.repositories.ITableTemplateStringMoshiJsonCache
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
    private lateinit var tableTemplateStringMoshiJsonCache: ITableTemplateStringMoshiJsonCache
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
    private val dummyData = TableTemplateItemListMoshi(templateName, listOf(TableTemplateItemMoshi("test", dataType = ItemType.STRING)))

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        tableTemplateRepository = TableTemplateFileRepository(
            tableItemListMoshiSerialization,
            tableTemplateStringMoshiJsonCache,
            stringFileStorageStrSerialisation)

        tableTemplateRepository.setAbsoluteFile(absolutePath)
        tableTemplateRepository.setFile(file)
        tableTemplateRepository.setDirectoryFile(directoryFile)
        tableTemplateRepository.setBufferReader(bufferedReader)
        tableTemplateRepository.setFileWriter(fileWriter)

        every { tableTemplateStringMoshiJsonCache.set(templateName, dummyData) } returns Unit
    }

    @Test
    fun `LOAD table template FAILS WHEN READ Json file RETURNS FALSE`() = runTest(dispatcher) {
        assert(tableTemplateRepository.getErrorMsg() == "")
        coEvery { stringFileStorageStrSerialisation.read(bufferedReader) } returns false
        every { stringFileStorageStrSerialisation.getErrorMsg() } returns "An error occurred!"
        val result = tableTemplateRepository.load(templateName)
        assert(tableTemplateRepository.getErrorMsg() == stringFileStorageStrSerialisation.getErrorMsg())
        assert(!result)
    }

    @Test
    fun `LOAD table template AND read JSON file SUCCESS BUT JSON convert RETURNS FALSE`() = runTest(dispatcher) {
        assert(tableTemplateRepository.getErrorMsg() == "")
        coEvery { (stringFileStorageStrSerialisation.read(bufferedReader)) } returns true
        every { stringFileStorageStrSerialisation.getContents() } returns "test"
        coEvery { tableItemListMoshiSerialization.fromJson("test") } returns false
        every { tableItemListMoshiSerialization.getErrorMsg() } returns "An error occurred!"
        val result = tableTemplateRepository.load(templateName)
        assert(tableTemplateRepository.getErrorMsg() == tableItemListMoshiSerialization.getErrorMsg())
        assert(!result)
    }

    @Test
    fun `SAVE table template SUCCESS`() = runTest(dispatcher) {
        assert(tableTemplateRepository.getErrorMsg() == "")
        coEvery { tableItemListMoshiSerialization.toJson(dummyData) } returns true
        every { tableItemListMoshiSerialization.getToJsonItem() } returns "test"
        coEvery { stringFileStorageStrSerialisation.write(directoryFile, file, absolutePath, fileWriter, "test") } returns true
        val result = tableTemplateRepository.save(templateName, dummyData)
        assert(result)
        assert(tableTemplateRepository.getErrorMsg() == "")
    }

    @Test
    fun `SAVE table template FAILS WHEN WRITE JSON string RETURNS FALSE`() = runTest(dispatcher) {
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
    fun `SAVE table template FAILS WHEN CONVERT to JSON string RETURNS FALSE`() = runTest(dispatcher) {
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
    fun `SAVE table template JSON string CONVERT to JSON item RETURNS FALSE`() = runTest(dispatcher) {
        assert(tableTemplateRepository.getErrorMsg() == "")
        coEvery { tableItemListMoshiSerialization.toJson(dummyData) } returns false
        every { tableItemListMoshiSerialization.getErrorMsg() } returns "An error occurred!"
        val result = tableTemplateRepository.save(templateName, dummyData)
        assert(!result)
        assert(tableTemplateRepository.getErrorMsg() == tableItemListMoshiSerialization.getErrorMsg())
    }

    @Test
    fun `SAVE table template FAILS WHEN CONVERT to JSON item RETURNS NULL`() = runTest(dispatcher) {
        assert(tableTemplateRepository.getErrorMsg() == "")
        coEvery { tableItemListMoshiSerialization.toJson(dummyData) } returns true
        every { tableItemListMoshiSerialization.getToJsonItem() } returns null
        every { tableItemListMoshiSerialization.getErrorMsg() } returns "An error occurred!"
        val result = tableTemplateRepository.save(templateName, dummyData)
        assert(!result)
        assert(tableTemplateRepository.getErrorMsg() == tableItemListMoshiSerialization.getErrorMsg())
    }
}