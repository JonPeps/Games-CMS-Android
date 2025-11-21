package com.jonpeps.gamescms.data.serialization

import com.jonpeps.gamescms.data.repositories.MoshiStringListRepository
import com.jonpeps.gamescms.data.repositories.MoshiTableTemplateRepository
import com.jonpeps.gamescms.ui.tabletemplates.serialization.ISerializeTableTemplateUpdateCore
import com.jonpeps.gamescms.ui.tabletemplates.serialization.SerializeTableTemplateHelpers
import com.jonpeps.gamescms.ui.tabletemplates.serialization.SerializeTableTemplateUpdateCore
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.Before
import java.io.BufferedReader
import java.io.File
import java.io.FileWriter
import java.io.InputStream

class SerializeTableTemplateUpdateCoreTests {
    @MockK
    private lateinit var mockFile: File
    @MockK
    private lateinit var mockInputStream: InputStream
    @MockK
    private lateinit var mockBufferReader: BufferedReader
    @MockK
    private lateinit var mockFileWriter: FileWriter
    @MockK
    private lateinit var mockSerializeTableTemplateHelpers: SerializeTableTemplateHelpers
    @MockK
    private lateinit var mockStringListRepository: MoshiStringListRepository
    @MockK
    private lateinit var mockMoshiTableTemplateRepository: MoshiTableTemplateRepository
    @MockK
    private lateinit var mockCommonSerializationRepoHelper: CommonSerializationRepoHelper

    private lateinit var sut: ISerializeTableTemplateUpdateCore

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        sut = SerializeTableTemplateUpdateCore(
            mockSerializeTableTemplateHelpers,
            mockStringListRepository,
            mockMoshiTableTemplateRepository,
            mockCommonSerializationRepoHelper
        )

        initMockStrListRepoFiles()
        initMockTableTemplateRepoReadFiles()
    }

    private fun initMockStrListRepoFiles() {
        every { mockStringListRepository.setAbsoluteFile(mockFile) } returns Unit
        every { mockStringListRepository.assignDirectoryFile(any()) } returns Unit
        every { mockStringListRepository.setFileWriter(any()) } returns Unit
        every { mockStringListRepository.setFile(mockFile) } returns Unit
        every { mockStringListRepository.setFileWriter(any()) } returns Unit
        every { mockStringListRepository.assignDirectoryFile(any()) } returns Unit
    }

    private fun initMockTableTemplateRepoReadFiles() {
        every { mockCommonSerializationRepoHelper.getAbsoluteFile(any(), any()) } returns mockFile
        every { mockCommonSerializationRepoHelper.getBufferReader(any(), any()) } returns mockBufferReader
        every { mockCommonSerializationRepoHelper.getMainFile(any()) } returns mockFile
        every { mockCommonSerializationRepoHelper.getDirectoryFile(any()) } returns mockFile
        every { mockCommonSerializationRepoHelper.getFileWriter(any(), any()) } returns mockFileWriter
        every { mockCommonSerializationRepoHelper.getInputStream(any()) } returns mockInputStream
    }
}