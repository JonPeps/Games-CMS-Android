package com.jonpeps.gamescms.viewmodels

import com.jonpeps.gamescms.data.repositories.IMoshiStringListRepository
import com.jonpeps.gamescms.data.serialization.ICommonSerializationRepoHelper
import com.jonpeps.gamescms.data.serialization.debug.IInputStreamSerializationRepoHelper
import com.jonpeps.gamescms.data.viewmodels.InputStreamStringListViewModel
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Before
import org.mockito.ArgumentMatchers.any
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



    @Before
    fun setup() {
        MockKAnnotations.init(this)

        viewModel = InputStreamStringListViewModel(
            mockInputStream,
            directory,
            mockMoshiStringListRepository,
            mockCommonSerializationRepoHelper,
            mockInputStreamSerializationRepoHelper,
            dispatcher
        )
    }



    private fun setupForReadingFiles() {
        mockMoshiStringListRepository.setBufferReader(
            mockInputStreamSerializationRepoHelper.getBufferReader(any())
        )
        every { mockMoshiStringListRepository.setAbsoluteFile(any()) } returns Unit
        every { mockMoshiStringListRepository.setBufferReader(any()) } returns Unit
    }

    private fun setupForWritingFiles() {
        every { mockMoshiStringListRepository.setAbsoluteFile(any()) } returns Unit
        every { mockMoshiStringListRepository.assignDirectoryFile(any()) } returns Unit
        every { mockMoshiStringListRepository.setFile(any()) } returns Unit
        every { mockMoshiStringListRepository.setFileWriter(any()) } returns Unit
        every { mockMoshiStringListRepository.setItem(any()) } returns Unit
    }

    private fun setupCommonFiles() {
        every { mockCommonSerializationRepoHelper.getAbsoluteFile(any(), any()) } returns mockk()
        every { mockCommonSerializationRepoHelper.getFileWriter(any(), any()) } returns mockk()
        every { mockCommonSerializationRepoHelper.getDirectoryFile(any()) } returns mockk()
        every { mockCommonSerializationRepoHelper.getMainFile(any()) } returns mockk()
    }
}