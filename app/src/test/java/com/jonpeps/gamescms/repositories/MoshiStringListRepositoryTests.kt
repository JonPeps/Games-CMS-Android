package com.jonpeps.gamescms.repositories

import com.jonpeps.gamescms.data.dataclasses.StringListData
import com.jonpeps.gamescms.data.repositories.IMoshiStringListRepository
import com.jonpeps.gamescms.data.repositories.MoshiStringListRepository
import com.jonpeps.gamescms.data.serialization.moshi.IStringListMoshiSerialization
import com.jonpeps.gamescms.data.serialization.string.IStringFileStorageStrSerialisation
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
class MoshiStringListRepositoryTests {
    @OptIn(ExperimentalCoroutinesApi::class)
    private val dispatcher = UnconfinedTestDispatcher()
    @MockK
    private lateinit var stringFileStorageStrSerialisation: IStringFileStorageStrSerialisation
    @MockK
    private lateinit var strListMoshiSerialization: IStringListMoshiSerialization
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

    private val dummyData = StringListData(arrayListOf("test1", "test2"))

    private lateinit var moshiStringListRepository: IMoshiStringListRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        moshiStringListRepository = MoshiStringListRepository(strListMoshiSerialization, stringFileStorageStrSerialisation)
        moshiStringListRepository.setAbsoluteFile(absolutePath)
        moshiStringListRepository.setFile(file)
        moshiStringListRepository.setDirectoryFile(directoryFile)
        moshiStringListRepository.setBufferReader(bufferedReader)
        moshiStringListRepository.setFileWriter(fileWriter)
    }

    @Test
    fun `test load string list success all paths`() = runTest(dispatcher) {
        assert(moshiStringListRepository.getErrorMsg() == "")
        coEvery { stringFileStorageStrSerialisation.read(absolutePath, bufferedReader) } returns true
        coEvery { strListMoshiSerialization.fromJson("") } returns true
        every { stringFileStorageStrSerialisation.getContents() } returns ""
        every { strListMoshiSerialization.getItem() } returns dummyData
        val result = moshiStringListRepository.load()
        assert(result)
        assert(strListMoshiSerialization.getItem() == dummyData)
        assert(result)
    }

    @Test
    fun `test load string list with read Json string false`() = runTest(dispatcher) {
        assert(moshiStringListRepository.getErrorMsg() == "")
        every { stringFileStorageStrSerialisation.getErrorMsg() } returns "An error occurred!"
        coEvery { stringFileStorageStrSerialisation.read(absolutePath, bufferedReader) } returns false
        val result = moshiStringListRepository.load()
        assert(stringFileStorageStrSerialisation.getErrorMsg() == moshiStringListRepository.getErrorMsg())
        assert(!result)
    }

    @Test
    fun `test load string list with read Json file success but Json convert fails`() = runTest(dispatcher) {
        assert(moshiStringListRepository.getErrorMsg() == "")
        every { strListMoshiSerialization.getErrorMsg() } returns "An error occurred!"
        every { stringFileStorageStrSerialisation.getErrorMsg() } returns strListMoshiSerialization.getErrorMsg()
        every { stringFileStorageStrSerialisation.getContents() } returns ""
        coEvery { stringFileStorageStrSerialisation.read(absolutePath, bufferedReader) } returns true
        coEvery { strListMoshiSerialization
            .fromJson(stringFileStorageStrSerialisation.getContents()) } returns false
        val result = moshiStringListRepository.load()
        assert(moshiStringListRepository.getErrorMsg() == strListMoshiSerialization.getErrorMsg())
        assert(!result)
    }

    @Test
    fun `test save string list success all paths`() = runTest(dispatcher) {
        assert(moshiStringListRepository.getErrorMsg() == "")
        coEvery { strListMoshiSerialization.toJson(dummyData) } returns true
        every { strListMoshiSerialization.getToJsonItem() } returns "test"
        coEvery { stringFileStorageStrSerialisation.write(directoryFile, file, absolutePath, fileWriter, "test") } returns true
        val result = moshiStringListRepository.save(dummyData)
        assert(result)
        assert(moshiStringListRepository.getErrorMsg() == "")
    }

    @Test
    fun `test save string list with write Json string to file success`() = runTest(dispatcher) {
        assert(moshiStringListRepository.getErrorMsg() == "")
        coEvery { strListMoshiSerialization.toJson(dummyData) } returns true
        every { strListMoshiSerialization.getToJsonItem() } returns "test"
        every { strListMoshiSerialization.getErrorMsg() } returns "An error occurred!"
        every { stringFileStorageStrSerialisation.getErrorMsg() } returns strListMoshiSerialization.getErrorMsg()
        coEvery { stringFileStorageStrSerialisation.write(directoryFile, file, absolutePath, fileWriter, "test") } returns true
        val result = moshiStringListRepository.save(dummyData)
        assert(result)
    }

    @Test
    fun `test save string list with convert to Json string fails`() = runTest(dispatcher) {
        assert(moshiStringListRepository.getErrorMsg() == "")
        coEvery { strListMoshiSerialization.toJson(dummyData) } returns false
        every { strListMoshiSerialization.getErrorMsg() } returns "An error occurred!"
        every { stringFileStorageStrSerialisation.getErrorMsg() } returns strListMoshiSerialization.getErrorMsg()
        val result = moshiStringListRepository.save(dummyData)
        assert(!result)
        assert(stringFileStorageStrSerialisation.getErrorMsg() == moshiStringListRepository.getErrorMsg())
    }

    @Test
    fun `test save string list Json string convert failure`() = runTest(dispatcher) {
        assert(moshiStringListRepository.getErrorMsg() == "")
        coEvery { strListMoshiSerialization.toJson(dummyData) } returns true
        every { strListMoshiSerialization.getToJsonItem() } returns null
        every { strListMoshiSerialization.getErrorMsg() } returns "An error occurred!"
        every { stringFileStorageStrSerialisation.getErrorMsg() } returns strListMoshiSerialization.getErrorMsg()
        val result = moshiStringListRepository.save(dummyData)
        assert(!result)
        assert(moshiStringListRepository.getErrorMsg() == strListMoshiSerialization.getErrorMsg())
    }

    @Test
    fun `test save string list with write Json string failure`() = runTest(dispatcher) {
        assert(moshiStringListRepository.getErrorMsg() == "")
        coEvery { strListMoshiSerialization.toJson(dummyData) } returns true
        every { strListMoshiSerialization.getToJsonItem() } returns "test"
        every { strListMoshiSerialization.getErrorMsg() } returns "An error occurred!"
        every { stringFileStorageStrSerialisation.getErrorMsg() } returns strListMoshiSerialization.getErrorMsg()
        coEvery { stringFileStorageStrSerialisation.write(directoryFile, file, absolutePath, fileWriter, "test") } returns false
        val result = moshiStringListRepository.save(dummyData)
        assert(!result)
        assert(moshiStringListRepository.getErrorMsg() == stringFileStorageStrSerialisation.getErrorMsg())
    }

    @Test
    fun `test save string list with write Json string failure and get Json item is null`() = runTest(dispatcher) {
        assert(moshiStringListRepository.getErrorMsg() == "")
        coEvery { strListMoshiSerialization.toJson(dummyData) } returns true
        every { strListMoshiSerialization.getToJsonItem() } returns null
        every { strListMoshiSerialization.getErrorMsg() } returns "An error occurred!"
        val result = moshiStringListRepository.save(dummyData)
        assert(!result)
        assert(moshiStringListRepository.getErrorMsg() == strListMoshiSerialization.getErrorMsg())
    }
}