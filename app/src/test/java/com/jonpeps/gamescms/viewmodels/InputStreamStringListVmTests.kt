package com.jonpeps.gamescms.viewmodels

import com.jonpeps.gamescms.data.repositories.IMoshiStringListRepository
import com.jonpeps.gamescms.data.serialization.ICommonSerializationRepoHelper
import com.jonpeps.gamescms.data.serialization.debug.IInputStreamSerializationRepoHelper
import com.jonpeps.gamescms.data.viewmodels.InputStreamStringListViewModel
import com.jonpeps.gamescms.data.viewmodels.InputStreamToJsonTypeToStorageVm.Companion.FAILED_TO_CREATE_DIR
import com.jonpeps.gamescms.data.viewmodels.InputStreamToJsonTypeToStorageVm.Companion.FAILED_TO_LOAD_FILE
import com.jonpeps.gamescms.data.viewmodels.InputStreamToJsonTypeToStorageVm.Companion.FAILED_TO_WRITE_FILE
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

    private lateinit var viewModel: InputStreamStringListViewModel

    private val directory = "directory"
    private val fileName = "fileName"


    @Before
    fun setup() {
        MockKAnnotations.init(this)

        viewModel = InputStreamStringListViewModel(
            mockMoshiStringListRepository,
            mockCommonSerializationRepoHelper,
            mockInputStreamSerializationRepoHelper,
            dispatcher
        )

        setupForReadingFiles()
        setupForWritingFiles()
    }

    @Test
    fun `process SUCCESS reading InputStream and saving to internal storage`() {
        coEvery { mockMoshiStringListRepository.serialize(any()) } returns true
        every { mockMoshiStringListRepository.getItem() } returns mockk()
        coEvery { mockMoshiStringListRepository.save(any()) } returns true
        every { mockCommonSerializationRepoHelper.createDirectory(any()) } returns true
        every { mockCommonSerializationRepoHelper.readAll(any()) } returns ""

        viewModel.process(mockInputStream, directory, fileName)

        assert(viewModel.status.value.success)
        assert(viewModel.status.value.item != null)
        assert(viewModel.status.value.errorMessage == "")
        assert(viewModel.status.value.exception == null)
    }

    @Test
    fun `process FAILURE reading InputStream and saving to internal storage due to SERIALIZE RETURNS FALSE `() {
        coEvery { mockMoshiStringListRepository.serialize(any()) } returns false
        every { mockCommonSerializationRepoHelper.readAll(any()) } returns ""

        viewModel.process(mockInputStream, directory, fileName)

        assert(!viewModel.status.value.success)
        assert(viewModel.status.value.item == null)
        assert(viewModel.status.value.errorMessage == FAILED_TO_LOAD_FILE)
        assert(viewModel.status.value.exception == null)
    }

    @Test
    fun `process FAILURE reading InputStream and saving to internal storage due to GET JSON ITEM IS NULL `() {
        coEvery { mockMoshiStringListRepository.serialize(any()) } returns true
        every { mockCommonSerializationRepoHelper.readAll(any()) } returns ""
        every { mockMoshiStringListRepository.getItem() } returns null

        viewModel.process(mockInputStream, directory, fileName)

        assert(!viewModel.status.value.success)
        assert(viewModel.status.value.item == null)
        assert(viewModel.status.value.errorMessage == FAILED_TO_LOAD_FILE)
        assert(viewModel.status.value.exception == null)
    }

    @Test
    fun `process FAILURE reading InputStream and saving to internal storage due to read all INPUT STREAM throws EXCEPTION `() {
        coEvery { mockMoshiStringListRepository.serialize(any()) } returns false
        every { mockCommonSerializationRepoHelper.readAll(any()) } throws Exception("Runtime error!")

        viewModel.process(mockInputStream, directory, fileName)

        assert(!viewModel.status.value.success)
        assert(viewModel.status.value.item == null)
        assert(viewModel.status.value.errorMessage == "Runtime error!")
        assert(viewModel.status.value.exception != null)
    }

    @Test
    fun `process FAILURE reading InputStream and saving to internal storage due to CREATE DIRECTORY FAILS`() {
        coEvery { mockMoshiStringListRepository.serialize(any()) } returns true
        every { mockMoshiStringListRepository.getItem() } returns mockk()
        coEvery { mockMoshiStringListRepository.save(any()) } returns true
        every { mockCommonSerializationRepoHelper.readAll(any()) } returns ""
        every { mockCommonSerializationRepoHelper.createDirectory(any()) } returns false

        viewModel.process(mockInputStream, directory, fileName)

        assert(!viewModel.status.value.success)
        assert(viewModel.status.value.errorMessage == FAILED_TO_CREATE_DIR + directory)
    }

    @Test
    fun `process FAILURE reading InputStream and saving to internal storage due to CREATE DIRECTORY throws EXCEPTION`() {
        coEvery { mockMoshiStringListRepository.serialize(any()) } returns true
        every { mockMoshiStringListRepository.getItem() } returns mockk()
        coEvery { mockMoshiStringListRepository.save(any()) } returns true
        every { mockCommonSerializationRepoHelper.readAll(any()) } returns ""
        every { mockCommonSerializationRepoHelper.createDirectory(any()) } throws SecurityException("An error occurred!")

        viewModel.process(mockInputStream, directory, fileName)

        assert(!viewModel.status.value.success)
        assert(viewModel.status.value.errorMessage == "An error occurred!")
        assert(viewModel.status.value.exception != null)
    }

    @Test
    fun `process FAILURE reading InputStream and saving to internal storage due to saving JSON RETURNS FALSE`() {
        coEvery { mockMoshiStringListRepository.serialize(any()) } returns true
        every { mockMoshiStringListRepository.getItem() } returns mockk()
        coEvery { mockMoshiStringListRepository.save(any()) } returns false
        every { mockCommonSerializationRepoHelper.readAll(any()) } returns ""
        every { mockCommonSerializationRepoHelper.createDirectory(any()) } returns true

        viewModel.process(mockInputStream, directory, fileName)

        assert(!viewModel.status.value.success)
        assert(viewModel.status.value.errorMessage == FAILED_TO_WRITE_FILE)
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