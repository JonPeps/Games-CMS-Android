package com.jonpeps.gamescms.repositories

import com.jonpeps.gamescms.data.dataclasses.moshi.StringListMoshi
import com.jonpeps.gamescms.data.repositories.BaseMoshiRepo.Companion.CONVERT_TO_JSON_FAILED
import com.jonpeps.gamescms.data.repositories.BaseMoshiRepo.Companion.EMPTY_JSON_CONTENTS
import com.jonpeps.gamescms.data.repositories.BaseMoshiRepo.Companion.WRITE_TO_FILE_FAILED
import com.jonpeps.gamescms.data.repositories.IMoshiStringListRepository
import com.jonpeps.gamescms.data.repositories.MoshiStringListRepository
import com.jonpeps.gamescms.data.repositories.StringListMoshiJsonAdapter
import com.jonpeps.gamescms.data.serialization.string.IStringFileStorageStrSerialisation
import com.squareup.moshi.JsonAdapter
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
class BasicMoshiRelatedRepositoryTests {
    @OptIn(ExperimentalCoroutinesApi::class)
    private val dispatcher = UnconfinedTestDispatcher()
    @MockK
    private lateinit var stringListMoshiJsonAdapter: StringListMoshiJsonAdapter
    @MockK
    private lateinit var mockMoshiJsonAdapter: JsonAdapter<StringListMoshi>
    @MockK
    private lateinit var stringFileStorageStrSerialisation: IStringFileStorageStrSerialisation
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

    private val templateName = "test"
    private val dummyData = StringListMoshi(listOf("test"))

    private lateinit var moshiStringListRepository: IMoshiStringListRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        moshiStringListRepository = MoshiStringListRepository(
            stringListMoshiJsonAdapter,
            stringFileStorageStrSerialisation)

        moshiStringListRepository.setAbsoluteFile(absolutePath)
        moshiStringListRepository.setFile(file)
        moshiStringListRepository.assignDirectoryFile(directoryFile)
        moshiStringListRepository.setBufferReader(bufferedReader)
        moshiStringListRepository.setFileWriter(fileWriter)

        every { stringListMoshiJsonAdapter.getJsonAdapter() } returns mockMoshiJsonAdapter
    }

    @Test
    fun `LOAD string list SUCCESS`() = runTest(dispatcher) {
        assert(moshiStringListRepository.getErrorMsg() == "")
        coEvery { stringFileStorageStrSerialisation.read(bufferedReader) } returns true
        every { stringFileStorageStrSerialisation.getContents() } returns "test"
        every { mockMoshiJsonAdapter.fromJson("test") } returns dummyData
        val result = moshiStringListRepository.load()
        assert(result)
    }

    @Test
    fun `LOAD string list FAILS`() = runTest(dispatcher) {
        assert(moshiStringListRepository.getErrorMsg() == "")
        every { stringFileStorageStrSerialisation.getErrorMsg() } returns "An error occurred!"
        coEvery { stringFileStorageStrSerialisation.read(bufferedReader) } returns false
        val result = moshiStringListRepository.load()
        assert(moshiStringListRepository.getErrorMsg() == stringFileStorageStrSerialisation.getErrorMsg())
        assert(!result)
    }

    @Test
    fun `LOAD string list AND READ JSON string RETURNS FALSE`() = runTest(dispatcher) {
        assert(moshiStringListRepository.getErrorMsg() == "")
        every { stringFileStorageStrSerialisation.getErrorMsg() } returns "An error occurred!"
        coEvery { stringFileStorageStrSerialisation.read(bufferedReader) } returns false
        val result = moshiStringListRepository.load()
        assert(stringFileStorageStrSerialisation.getErrorMsg() == moshiStringListRepository.getErrorMsg())
        assert(!result)
    }

    @Test
    fun `LOAD string list WITH READ JSON file SUCCESS BUT JSON parsing RETURNS FALSE`() = runTest(dispatcher) {
        assert(moshiStringListRepository.getErrorMsg() == "")
        every { stringFileStorageStrSerialisation.getContents() } returns "test"
        every { mockMoshiJsonAdapter.fromJson("test") } returns null
        coEvery { stringFileStorageStrSerialisation.read(bufferedReader) } returns true
        val result = moshiStringListRepository.load()
        assert(!result)
    }

    @Test
    fun `SAVE string list SUCCESS`() = runTest(dispatcher) {
        assert(moshiStringListRepository.getErrorMsg() == "")
        coEvery { stringFileStorageStrSerialisation.write(directoryFile, file, absolutePath, fileWriter, "test") } returns true
        every { mockMoshiJsonAdapter.toJson(dummyData) } returns "test"
        val result = moshiStringListRepository.save(dummyData)
        assert(result)
        assert(moshiStringListRepository.getErrorMsg() == "")
    }

    @Test
    fun `SAVE string list WITH CONVERT to JSON string RETURNS NULL`() = runTest(dispatcher) {
        assert(moshiStringListRepository.getErrorMsg() == "")
        every { mockMoshiJsonAdapter.toJson(dummyData) } returns null
        val result = moshiStringListRepository.save(dummyData)
        assert(!result)
        assert(moshiStringListRepository.getErrorMsg() == CONVERT_TO_JSON_FAILED)
    }

    @Test
    fun `SAVE string list WITH CONVERT to JSON RETURNS EMPTY STRING`() = runTest(dispatcher) {
        assert(moshiStringListRepository.getErrorMsg() == "")
        every { mockMoshiJsonAdapter.toJson(dummyData) } returns ""
        val result = moshiStringListRepository.save(dummyData)
        assert(!result)
        assert(moshiStringListRepository.getErrorMsg() == CONVERT_TO_JSON_FAILED)
    }

    @Test
    fun `SAVE to JSON item FAILS WHEN WRITING to file`() = runTest(dispatcher) {
        assert(moshiStringListRepository.getErrorMsg() == "")
        every { mockMoshiJsonAdapter.toJson(dummyData) } returns "test"
        coEvery { stringFileStorageStrSerialisation.write(directoryFile, file, absolutePath, fileWriter, "test") } returns false
        val result = moshiStringListRepository.save(dummyData)
        assert(!result)
        assert(moshiStringListRepository.getErrorMsg() == WRITE_TO_FILE_FAILED)
    }

    @Test
    fun `SERIALIZE string list SUCCESS`() = runTest(dispatcher) {
        assert(moshiStringListRepository.getErrorMsg() == "")
        every { mockMoshiJsonAdapter.fromJson("test") } returns dummyData
        val result = moshiStringListRepository.serialize("test")
        assert(result)
    }

    @Test
    fun `SERIALIZE string list FAILURE WHEN CONVERTING to JSON`() = runTest(dispatcher) {
        assert(moshiStringListRepository.getErrorMsg() == "")
        every { mockMoshiJsonAdapter.fromJson("test") } returns null
        val result = moshiStringListRepository.serialize("test")
        assert(!result)
        assert(moshiStringListRepository.getErrorMsg() == EMPTY_JSON_CONTENTS)
    }
}