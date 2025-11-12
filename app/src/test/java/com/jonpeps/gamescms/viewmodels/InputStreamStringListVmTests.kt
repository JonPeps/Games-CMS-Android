package com.jonpeps.gamescms.viewmodels

import com.jonpeps.gamescms.data.repositories.IMoshiStringListRepository
import com.jonpeps.gamescms.data.serialization.ICommonSerializationRepoHelper
import com.jonpeps.gamescms.data.serialization.debug.IInputStreamSerializationRepoHelper
import com.jonpeps.gamescms.data.viewmodels.InputStreamStringList
import com.jonpeps.gamescms.data.viewmodels.InputStreamToJsonTypeToStorage.Companion.FAILED_TO_CREATE_DIR
import com.jonpeps.gamescms.data.viewmodels.InputStreamToJsonTypeToStorage.Companion.FAILED_TO_LOAD_FILE
import com.jonpeps.gamescms.data.viewmodels.InputStreamToJsonTypeToStorage.Companion.FAILED_TO_WRITE_FILE
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Before
import org.junit.Test
import java.io.InputStream

@OptIn(ExperimentalCoroutinesApi::class)
class InputStreamStringListVmTests {
    private val dispatcher = UnconfinedTestDispatcher()
    @MockK
    private lateinit var mockInputStream: InputStream
    @MockK
    private lateinit var mockMoshiStringListRepository: IMoshiStringListRepository
    @MockK
    private lateinit var mockCommonSerializationRepoHelper: ICommonSerializationRepoHelper
    @MockK
    private lateinit var mockInputStreamSerializationRepoHelper: IInputStreamSerializationRepoHelper

    private lateinit var sut: InputStreamStringList

    private val directory = "directory"
    private val fileName = "fileName"


    @Before
    fun setup() {
        MockKAnnotations.init(this)

        sut = InputStreamStringList(
            mockMoshiStringListRepository,
            mockCommonSerializationRepoHelper,
            mockInputStreamSerializationRepoHelper
        )

        setupForReadingFiles()
        setupForWritingFiles()
    }

    @Test
    fun `process SUCCESS reading InputStream and saving to internal storage`() = runBlocking {
        coEvery { mockMoshiStringListRepository.serialize(any()) } returns true
        every { mockMoshiStringListRepository.getItem() } returns mockk()
        coEvery { mockMoshiStringListRepository.save(any()) } returns true
        every { mockCommonSerializationRepoHelper.createDirectory(any()) } returns true
        every { mockCommonSerializationRepoHelper.readAll(any()) } returns ""

        sut.processSuspend(mockInputStream, directory, fileName)

        assert(sut.status.success)
        assert(sut.status.item != null)
        assert(sut.status.errorMessage == "")
        assert(sut.status.exception == null)
    }

    @Test
    fun `process FAILURE reading InputStream and saving to internal storage due to SERIALIZE RETURNS FALSE `() = runBlocking {
        coEvery { mockMoshiStringListRepository.serialize(any()) } returns false
        every { mockCommonSerializationRepoHelper.readAll(any()) } returns ""

        sut.processSuspend(mockInputStream, directory, fileName)

        assert(!sut.status.success)
        assert(sut.status.item == null)
        assert(sut.status.errorMessage == FAILED_TO_LOAD_FILE)
        assert(sut.status.exception == null)
    }

    @Test
    fun `process FAILURE reading InputStream and saving to internal storage due to GET JSON ITEM IS NULL `() = runBlocking {
        coEvery { mockMoshiStringListRepository.serialize(any()) } returns true
        every { mockCommonSerializationRepoHelper.readAll(any()) } returns ""
        every { mockMoshiStringListRepository.getItem() } returns null

        sut.processSuspend(mockInputStream, directory, fileName)

        assert(!sut.status.success)
        assert(sut.status.item == null)
        assert(sut.status.errorMessage == FAILED_TO_LOAD_FILE)
        assert(sut.status.exception == null)
    }

    @Test
    fun `process FAILURE reading InputStream and saving to internal storage due to read all INPUT STREAM throws EXCEPTION `() = runBlocking {
        coEvery { mockMoshiStringListRepository.serialize(any()) } returns false
        every { mockCommonSerializationRepoHelper.readAll(any()) } throws Exception("Runtime error!")

        sut.processSuspend(mockInputStream, directory, fileName)

        assert(!sut.status.success)
        assert(sut.status.item == null)
        assert(sut.status.errorMessage == "Runtime error!")
        assert(sut.status.exception != null)
    }

    @Test
    fun `process FAILURE reading InputStream and saving to internal storage due to CREATE DIRECTORY FAILS`() = runBlocking {
        coEvery { mockMoshiStringListRepository.serialize(any()) } returns true
        every { mockMoshiStringListRepository.getItem() } returns mockk()
        coEvery { mockMoshiStringListRepository.save(any()) } returns true
        every { mockCommonSerializationRepoHelper.readAll(any()) } returns ""
        every { mockCommonSerializationRepoHelper.createDirectory(any()) } returns false

        sut.processSuspend(mockInputStream, directory, fileName)

        assert(!sut.status.success)
        assert(sut.status.errorMessage == FAILED_TO_CREATE_DIR + directory)
    }

    @Test
    fun `process FAILURE reading InputStream and saving to internal storage due to CREATE DIRECTORY throws EXCEPTION`() = runBlocking {
        coEvery { mockMoshiStringListRepository.serialize(any()) } returns true
        every { mockMoshiStringListRepository.getItem() } returns mockk()
        coEvery { mockMoshiStringListRepository.save(any()) } returns true
        every { mockCommonSerializationRepoHelper.readAll(any()) } returns ""
        every { mockCommonSerializationRepoHelper.createDirectory(any()) } throws SecurityException("An error occurred!")

        sut.processSuspend(mockInputStream, directory, fileName)

        assert(!sut.status.success)
        assert(sut.status.errorMessage == "An error occurred!")
        assert(sut.status.exception != null)
    }

    @Test
    fun `process FAILURE reading InputStream and saving to internal storage due to saving JSON RETURNS FALSE`() = runBlocking {
        coEvery { mockMoshiStringListRepository.serialize(any()) } returns true
        every { mockMoshiStringListRepository.getItem() } returns mockk()
        coEvery { mockMoshiStringListRepository.save(any()) } returns false
        every { mockCommonSerializationRepoHelper.readAll(any()) } returns ""
        every { mockCommonSerializationRepoHelper.createDirectory(any()) } returns true

        sut.processSuspend(mockInputStream, directory, fileName)

        assert(!sut.status.success)
        assert(sut.status.errorMessage == FAILED_TO_WRITE_FILE)
    }

    private fun setupForReadingFiles() {
        every { mockInputStreamSerializationRepoHelper.getBufferReader(mockInputStream) } returns mockk()
        every { mockMoshiStringListRepository.setBufferReader(any()) } returns Unit
        every { mockMoshiStringListRepository.setAbsoluteFile(any()) } returns Unit
        every { mockMoshiStringListRepository.setBufferReader(any()) } returns Unit
        every { mockCommonSerializationRepoHelper.getAbsoluteFile(any(), any()) } returns mockk()
        every { mockCommonSerializationRepoHelper.getFileWriter(any(), any()) } returns mockk()
        every { mockCommonSerializationRepoHelper.getDirectoryFile(any()) } returns mockk()
        every { mockCommonSerializationRepoHelper.getMainFile(any()) } returns mockk()
    }

    private fun setupForWritingFiles() {
        every { mockMoshiStringListRepository.setAbsoluteFile(any()) } returns Unit
        every { mockMoshiStringListRepository.assignDirectoryFile(any()) } returns Unit
        every { mockMoshiStringListRepository.setFile(any()) } returns Unit
        every { mockMoshiStringListRepository.setFileWriter(any()) } returns Unit
        every { mockMoshiStringListRepository.setItem(any()) } returns Unit
    }
}