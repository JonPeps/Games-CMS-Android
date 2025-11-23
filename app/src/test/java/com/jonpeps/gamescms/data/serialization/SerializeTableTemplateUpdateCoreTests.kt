package com.jonpeps.gamescms.data.serialization

import com.jonpeps.gamescms.data.dataclasses.moshi.StringListMoshi
import com.jonpeps.gamescms.data.repositories.MoshiStringListRepository
import com.jonpeps.gamescms.data.repositories.MoshiTableTemplateRepository
import com.jonpeps.gamescms.ui.tabletemplates.serialization.ISerializeTableTemplateUpdateCore
import com.jonpeps.gamescms.ui.tabletemplates.serialization.SerializeTableTemplateHelpers
import com.jonpeps.gamescms.ui.tabletemplates.serialization.SerializeTableTemplateUpdateCore
import com.jonpeps.gamescms.ui.tabletemplates.serialization.SerializeTableTemplateUpdateCore.Companion.EXCEPTION_THROWN_MSG
import com.jonpeps.gamescms.ui.tabletemplates.serialization.SerializeTableTemplateUpdateCore.Companion.FAILED_TO_SAVE_TEMPLATE_LIST
import com.jonpeps.gamescms.ui.tabletemplates.serialization.SerializeTableTemplates.Companion.STRING_LIST_ITEM_IS_NULL
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
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

    private lateinit var stringListMoshi: StringListMoshi

    private lateinit var sut: ISerializeTableTemplateUpdateCore

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        sut = SerializeTableTemplateUpdateCore(
            mockSerializeTableTemplateHelpers,
            mockStringListRepository,
            mockCommonSerializationRepoHelper
        )

        stringListMoshi = StringListMoshi(listOf("test1:test1.json", "test2:test2.json"))

        initMockMoshiTableTemplateSetRepoFiles()
        initMockStringListRepositorySetRepoFiles()
        initMockCommonSerializationRepoHelperGetFiles()
    }

    @Test
    fun `update SUCCESS WITH overwritten files`() = runBlocking {
        every { mockCommonSerializationRepoHelper.getInputStream(any()) } returns mockInputStream
        every { mockSerializeTableTemplateHelpers.getFilename("test1") } returns "test1.json"
        coEvery { mockStringListRepository.load() } returns true
        every { mockStringListRepository.getItem() } returns stringListMoshi
        coEvery { mockStringListRepository.save(any()) } returns true

        sut.update("templatesListFilename",
            "absolutePath/",
            "test1",
            listOf("test1", "test2"),
            listOf("test1.json", "test2.json"))

        assert(sut.status.success)
        assert(sut.status.overwrittenFilename)
        assert(sut.status.names[0] == "test1")
        assert(sut.status.names[1] == "test2")
        assert(sut.status.fileNames[0] == "test1.json")
        assert(sut.status.fileNames[1] == "test2.json")
    }

    @Test
    fun `update SUCCESS WITHOUT overwritten files`() = runBlocking {
        every { mockCommonSerializationRepoHelper.getInputStream(any()) } returns mockInputStream
        every { mockSerializeTableTemplateHelpers.getFilename("test3") } returns "test3.json"
        coEvery { mockStringListRepository.load() } returns true
        every { mockStringListRepository.getItem() } returns stringListMoshi
        coEvery { mockStringListRepository.save(any()) } returns true

        sut.update("templatesListFilename",
            "absolutePath/",
            "test3",
            listOf("test1", "test2"),
            listOf("test1.json", "test2.json"))

        assert(sut.status.success)
        assert(!sut.status.overwrittenFilename)
        assert(sut.status.names[0] == "test1")
        assert(sut.status.names[1] == "test2")
        assert(sut.status.names[2] == "test3")
        assert(sut.status.fileNames[0] == "test1.json")
        assert(sut.status.fileNames[1] == "test2.json")
        assert(sut.status.fileNames[2] == "test3.json")
    }

    @Test
    fun `update FAILS due to NULL InputStream`() = runBlocking {
        every { mockCommonSerializationRepoHelper.getInputStream(any()) } returns null

        sut.update("templatesListFilename",
            "absolutePath/",
            "test1",
            listOf("test1", "test2"),
            listOf("test1.json", "test2.json"))

        assert(!sut.status.success)
        assert(!sut.status.overwrittenFilename)
        assert(sut.status.errorMessage == "File does not exist: absolutePath/templatesListFilename.json")
    }

    @Test
    fun `update FAILS due to StringListRepository LOAD RETURNS FALSE`() = runBlocking {
        every { mockCommonSerializationRepoHelper.getInputStream(any()) } returns mockInputStream
        coEvery { mockStringListRepository.load() } returns false
        every { mockStringListRepository.getErrorMsg() } returns "error!"

        sut.update("templatesListFilename",
            "absolutePath/",
            "test1",
            listOf("test1", "test2"),
            listOf("test1.json", "test2.json"))

        assert(!sut.status.success)
        assert(!sut.status.overwrittenFilename)
        assert(sut.status.errorMessage == "error!")
    }

    @Test
    fun `update FAILS due to StringListRepository getItem RETURNS NULL`() = runBlocking {
        every { mockCommonSerializationRepoHelper.getInputStream(any()) } returns mockInputStream
        coEvery { mockStringListRepository.load() } returns true
        every { mockStringListRepository.getItem() } returns null

        sut.update("templatesListFilename",
            "absolutePath/",
            "test1",
            listOf("test1", "test2"),
            listOf("test1.json", "test2.json"))

        assert(!sut.status.success)
        assert(sut.status.errorMessage == STRING_LIST_ITEM_IS_NULL)
    }

    @Test
    fun `update FAILS due to StringListRepository save RETURNS FALSE`() = runBlocking {
        every { mockSerializeTableTemplateHelpers.getFilename("test1") } returns "test1.json"
        every { mockCommonSerializationRepoHelper.getInputStream(any()) } returns mockInputStream
        coEvery { mockStringListRepository.load() } returns true
        every { mockStringListRepository.getItem() } returns stringListMoshi
        coEvery { mockStringListRepository.save(any()) } returns false

        sut.update("templatesListFilename",
            "absolutePath/",
            "test1",
            listOf("test1", "test2"),
            listOf("test1.json", "test2.json"))

        assert(!sut.status.success)
        assert(sut.status.errorMessage == FAILED_TO_SAVE_TEMPLATE_LIST)
    }

    @Test
    fun `update FAILS due to InputStream THROWING EXCEPTION`() = runBlocking {
        every { mockCommonSerializationRepoHelper.getInputStream(any()) } throws Exception("error!")

        sut.update("templatesListFilename",
            "absolutePath/",
            "test1",
            listOf("test1", "test2"),
            listOf("test1.json", "test2.json"))

        assert(!sut.status.success)
        assert(sut.status.errorMessage == EXCEPTION_THROWN_MSG + "error!")
    }

    private fun initMockMoshiTableTemplateSetRepoFiles() {
        every { mockMoshiTableTemplateRepository.setAbsoluteFile(any()) } returns Unit
        every { mockMoshiTableTemplateRepository.setBufferReader(any()) } returns Unit
        every { mockMoshiTableTemplateRepository.setFile(any()) } returns Unit
        every { mockMoshiTableTemplateRepository.assignDirectoryFile(any()) } returns Unit
        every { mockMoshiTableTemplateRepository.setFileWriter(any()) } returns Unit
        every { mockMoshiTableTemplateRepository.setItem(any()) } returns Unit
    }

    private fun initMockStringListRepositorySetRepoFiles() {
        every { mockStringListRepository.setItem(any()) } returns Unit
        every { mockStringListRepository.setAbsoluteFile(any()) } returns Unit
        every { mockStringListRepository.assignDirectoryFile(any()) } returns Unit
        every { mockStringListRepository.setFileWriter(any()) } returns Unit
        every { mockStringListRepository.setFile(any()) } returns Unit
        every { mockStringListRepository.setFileWriter(any()) } returns Unit
        every { mockStringListRepository.assignDirectoryFile(any()) } returns Unit
    }

    private fun initMockCommonSerializationRepoHelperGetFiles() {
        every { mockCommonSerializationRepoHelper.getDirectoryFile(any()) } returns mockFile
        every { mockCommonSerializationRepoHelper.getAbsoluteFile(any(), any()) } returns mockFile
        every { mockCommonSerializationRepoHelper.getBufferReader(any(), any()) } returns mockBufferReader
        every { mockCommonSerializationRepoHelper.getMainFile(any()) } returns mockFile
        every { mockCommonSerializationRepoHelper.getDirectoryFile(any()) } returns mockFile
        every { mockCommonSerializationRepoHelper.getFileWriter(any(), any()) } returns mockFileWriter
    }
}