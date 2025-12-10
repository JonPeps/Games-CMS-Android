package com.jonpeps.gamescms.data.serialization

import android.content.Context
import android.content.res.AssetManager
import com.jonpeps.gamescms.data.DataConstants.Companion.JSON_EXTENSION
import com.jonpeps.gamescms.data.dataclasses.ItemType
import com.jonpeps.gamescms.data.dataclasses.moshi.StringListMoshi
import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateItemListMoshi
import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateItemMoshi
import com.jonpeps.gamescms.data.helpers.InputStreamTableTemplate
import com.jonpeps.gamescms.data.helpers.StringListToSplitItemList
import com.jonpeps.gamescms.data.repositories.MoshiStringListRepository
import com.jonpeps.gamescms.data.repositories.MoshiTableTemplateRepository
import com.jonpeps.gamescms.data.repositories.MoshiTableTemplateStatusListRepository
import com.jonpeps.gamescms.ui.tabletemplates.serialization.SerializeTableTemplateUpdateCore
import com.jonpeps.gamescms.ui.tabletemplates.serialization.SerializeTableTemplates
import com.jonpeps.gamescms.ui.tabletemplates.serialization.SerializeTableTemplates.Companion.EXCEPTION_THROWN_MSG
import com.jonpeps.gamescms.ui.tabletemplates.serialization.SerializeTableTemplates.Companion.EXTERNAL_STORAGE_PATH_IS_NULL
import com.jonpeps.gamescms.ui.tabletemplates.serialization.SerializeTableTemplates.Companion.FAILED_TO_SAVE_TEMPLATE
import com.jonpeps.gamescms.ui.tabletemplates.serialization.SerializeTableTemplates.Companion.FILE_DOES_NOT_EXIST
import com.jonpeps.gamescms.ui.tabletemplates.serialization.SerializeTableTemplates.Companion.TABLE_TEMPLATES_ARE_NULL
import com.jonpeps.gamescms.ui.tabletemplates.serialization.SerializeTableTemplates.Companion.TEMPLATES_FOLDER
import com.jonpeps.gamescms.ui.tabletemplates.serialization.SerializeTableTemplates.Companion.TEMPLATE_INPUT_STREAM_IS_NULL
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.io.BufferedReader
import java.io.File
import java.io.FileWriter
import java.io.InputStream

class SerializeTableTemplatesTests {
    @MockK
    private lateinit var mockContext: Context
    @MockK
    private lateinit var mockAssetManager: AssetManager
    @MockK
    private lateinit var mockFile: File
    @MockK
    private lateinit var mockBufferReader: BufferedReader
    @MockK
    private lateinit var mockFileWriter: FileWriter
    @MockK
    private lateinit var mockStringListToSplitItemList: StringListToSplitItemList
    @MockK
    private lateinit var mockInputStreamTableTemplate: InputStreamTableTemplate
    @MockK
    private lateinit var mockMoshiTableTemplateRepository: MoshiTableTemplateRepository
    @MockK
    private lateinit var mockStringListRepository: MoshiStringListRepository
    @MockK
    private lateinit var mockMoshiTableTemplateStatusListRepository: MoshiTableTemplateStatusListRepository
    @MockK
    private lateinit var mockCommonSerializationRepoHelper: CommonSerializationRepoHelper
    @MockK
    private lateinit var mockSerializeTableTemplateUpdateCore: SerializeTableTemplateUpdateCore
    @MockK
    private lateinit var mockInputStream: InputStream

    private lateinit var tableTemplateItems: List<TableTemplateItemMoshi>

    private lateinit var tableTemplateItemListMoshi: TableTemplateItemListMoshi

    private lateinit var stringListMoshi: StringListMoshi

    private lateinit var sut: SerializeTableTemplates

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        sut = SerializeTableTemplates(mockContext,
            mockAssetManager,
            mockStringListToSplitItemList,
            mockInputStreamTableTemplate,
            mockMoshiTableTemplateRepository,
            mockCommonSerializationRepoHelper,
            mockSerializeTableTemplateUpdateCore)

        every { mockContext.getExternalFilesDir(any()) } returns mockFile
        coEvery { mockStringListToSplitItemList.loadSuspend(any()) } returns Unit
        every { mockAssetManager.open(any()) } returns mockInputStream
        every { mockSerializeTableTemplateUpdateCore.status.fileNames } returns listOf("test1.json", "test2.json")
        every { mockSerializeTableTemplateUpdateCore.status.names } returns listOf("test1", "test2")
        every { mockStringListToSplitItemList.status.fileNames } returns listOf("test1.json", "test2.json")
        every { mockStringListToSplitItemList.status.names } returns listOf("test1", "test2")
        every { mockFile.absolutePath } returns "testFolder/"
        every { mockSerializeTableTemplateUpdateCore.status.templateFilename } returns "templatesList.json"

        tableTemplateItems = arrayListOf(TableTemplateItemMoshi(
            "test1",
            ItemType.STRING,
            true,
            "value",
            editable = true,
            isSortKey = true
        ), TableTemplateItemMoshi(
            "test2",
            ItemType.BOOLEAN,
            true,
            "true",
            editable = true,
            isSortKey = false
        ))

        tableTemplateItemListMoshi = TableTemplateItemListMoshi("test", tableTemplateItems)
        stringListMoshi = StringListMoshi(listOf("test1:test1.json", "test2:test2.json"))

        initMockMoshiTableTemplateInputFiles()
        initMockTableTemplateOutputFiles()
    }

    @Test
    fun `serialize from ASSETS SUCCESS`() = runBlocking {
        every { mockContext.getExternalFilesDir(any()) } returns mockFile
        every { mockStringListToSplitItemList.status.success } returns true
        coEvery { mockInputStreamTableTemplate.processSuspend(any(), any(), any()) } returns Unit
        every { mockInputStreamTableTemplate.status.success } returns true
        every { mockInputStreamTableTemplate.status.errorMessage } returns ""
        coEvery { mockMoshiTableTemplateStatusListRepository.save(any()) } returns true
        every { mockInputStreamTableTemplate.status.success } returns true
        every { mockInputStreamTableTemplate.status.item } returns mockk()
        every { mockInputStreamTableTemplate.status.errorMessage } returns ""
        coEvery {mockMoshiTableTemplateRepository.save(any()) } returns true

        sut.serializeFromAssets("test1")

        assert(sut.serializeTableTemplatesStatus.success)
        assert(sut.serializeTableTemplatesStatus.items?.get(0)?.name == "test1")
        assert(sut.serializeTableTemplatesStatus.items?.get(0)?.file == "test1.json")
        assert(sut.serializeTableTemplatesStatus.errorMessage == "")
    }

    @Test
    fun `serialize from ASSETS FAILS due to StringListToSplitItemList RETURNS FALSE`() = runBlocking {
        every { mockStringListToSplitItemList.status.success } returns false
        every { mockStringListToSplitItemList.status.errorMessage } returns "error"

        sut.serializeFromAssets("test")

        assert(!sut.serializeTableTemplatesStatus.success)
        assert(sut.serializeTableTemplatesStatus.errorMessage == "error")
    }

    @Test
    fun `serialize from ASSETS FAILS due to EXCEPTION THROWN by StringListToSplitItemList`() = runBlocking {
        every { mockAssetManager.open(any()) } returns mockInputStream
        coEvery { mockStringListToSplitItemList.loadSuspend(any()) } throws Exception("error")

        sut.serializeFromAssets("test")

        assert(!sut.serializeTableTemplatesStatus.success)
        assert(sut.serializeTableTemplatesStatus.errorMessage == EXCEPTION_THROWN_MSG + "error")
    }

    @Test
    fun `serialize from ASSETS FAILS due to saving of Table Template list FAILS`() = runBlocking {
        every { mockStringListToSplitItemList.status.success } returns true
        every { mockStringListToSplitItemList.status.fileNames } returns listOf("test.json")
        every { mockStringListToSplitItemList.status.names } returns listOf("test")
        every { mockContext.getExternalFilesDir(any()) } returns mockFile
        every { mockFile.absolutePath } returns "testFolder/"
        coEvery { mockInputStreamTableTemplate.processSuspend(any(), any(), any()) } returns Unit
        every { mockInputStreamTableTemplate.status.success } returns true
        every { mockInputStreamTableTemplate.status.item } returns mockk()
        every { mockInputStreamTableTemplate.status.errorMessage } returns ""
        coEvery {mockMoshiTableTemplateRepository.save(any()) } returns false

        sut.serializeFromAssets("test")

        assert(!sut.serializeTableTemplatesStatus.success)
        assert(sut.serializeTableTemplatesStatus.errorMessage == FAILED_TO_SAVE_TEMPLATE)
    }

    @Test
    fun `serialize from ASSETS FAILS due to Table Template list IS NULL`() = runBlocking {
        every { mockStringListToSplitItemList.status.success } returns true
        every { mockStringListToSplitItemList.status.fileNames } returns listOf("test.json")
        every { mockStringListToSplitItemList.status.names } returns listOf("test")
        every { mockContext.getExternalFilesDir(any()) } returns mockFile
        every { mockFile.absolutePath } returns "testFolder/"
        coEvery { mockInputStreamTableTemplate.processSuspend(any(), any(), any()) } returns Unit
        every { mockInputStreamTableTemplate.status.success } returns true
        every { mockInputStreamTableTemplate.status.item } returns null
        every { mockInputStreamTableTemplate.status.errorMessage } returns TABLE_TEMPLATES_ARE_NULL

        sut.serializeFromAssets("test")

        assert(!sut.serializeTableTemplatesStatus.success)
        assert(sut.serializeTableTemplatesStatus.errorMessage == TABLE_TEMPLATES_ARE_NULL)
    }

    @Test
    fun `serialize from ASSETS FAILS due to EXTERNAL STORAGE PATH IS NULL`() = runBlocking {
        every { mockStringListToSplitItemList.status.success } returns true
        every { mockStringListToSplitItemList.status.fileNames } returns listOf("test.json")
        every { mockStringListToSplitItemList.status.names } returns listOf("test")
        every { mockContext.getExternalFilesDir(any()) } returns null

        sut.serializeFromAssets("test")

        assert(!sut.serializeTableTemplatesStatus.success)
        assert(sut.serializeTableTemplatesStatus.errorMessage == EXTERNAL_STORAGE_PATH_IS_NULL)
    }

    @Test
    fun `serialize from readItems SUCCESS`() = runBlocking {
        every { mockCommonSerializationRepoHelper.getInputStream(any()) } returns mockInputStream
        every {mockCommonSerializationRepoHelper.getInputStreamFromStr(any()) } returns mockInputStream
        every { mockStringListToSplitItemList.status.success } returns true
        every { mockStringListToSplitItemList.status.fileNames } returns listOf("test.json")
        every { mockStringListToSplitItemList.status.names } returns listOf("test")
        every { mockContext.getExternalFilesDir(any()) } returns mockFile
        every { mockFile.absolutePath } returns "testFolder/"
        coEvery { mockInputStreamTableTemplate.processSuspend(any()) } returns Unit
        every { mockInputStreamTableTemplate.status.success } returns true
        every { mockInputStreamTableTemplate.status.errorMessage } returns ""

        sut.readItems("test")

        assert(sut.serializeTableTemplatesStatus.success)
        assert(sut.serializeTableTemplatesStatus.errorMessage == "")
        assert(sut.serializeTableTemplatesStatus.items?.get(0)?.name == "test")
        assert(sut.serializeTableTemplatesStatus.items?.get(0)?.file == "test.json")
    }

    @Test
    fun `serialize from readItems FAILS due to INPUT STREAM of main FILE is NULL`() = runBlocking {
        every { mockFile.path } returns "test"
        every { mockFile.absolutePath } returns "testFolder/path"
        every { mockCommonSerializationRepoHelper.getInputStream(any()) } returns null
        every { mockStringListToSplitItemList.status.errorMessage } returns FILE_DOES_NOT_EXIST + "test"

        sut.readItems("test")

        assert(!sut.serializeTableTemplatesStatus.success)
        assert(sut.serializeTableTemplatesStatus.errorMessage == "File does not exist: test")
    }

    @Test
    fun `serialize from readItems FAILS due to INPUT STREAM for Table Template list item FILE is NULL`() = runBlocking {
        every { mockFile.path } returns "test"
        every { mockFile.absolutePath } returns "testFolder/path"
        every { mockCommonSerializationRepoHelper.getInputStream(any()) } returns mockInputStream
        coEvery { mockStringListToSplitItemList.loadSuspend(any()) } returns Unit
        every { mockStringListToSplitItemList.status.success } returns true
        every { mockStringListToSplitItemList.status.fileNames } returns listOf("test.json")
        every { mockStringListToSplitItemList.status.names } returns listOf("test")
        every { mockCommonSerializationRepoHelper.getInputStreamFromStr(any()) } returns null

        sut.readItems("test")

        assert(!sut.serializeTableTemplatesStatus.success)
        assert(sut.serializeTableTemplatesStatus.errorMessage == TEMPLATE_INPUT_STREAM_IS_NULL)
    }

    @Test
    fun `serialize from readItems FAILS due to INPUT STREAM throwing EXCEPTION`() = runBlocking {
        every { mockCommonSerializationRepoHelper.getInputStream(any()) } throws Exception("error")

        sut.readItems("test")

        assert(!sut.serializeTableTemplatesStatus.success)
        assert(sut.serializeTableTemplatesStatus.errorMessage == EXCEPTION_THROWN_MSG + "error")
    }

    @Test
    fun `serialize from readItems FAILS due to StringListToSplitItemList RETURNS FALSE`() = runBlocking {
        every { mockCommonSerializationRepoHelper.getInputStream(any()) } returns mockInputStream
        every { mockStringListToSplitItemList.status.success } returns false
        every { mockStringListToSplitItemList.status.errorMessage } returns FILE_DOES_NOT_EXIST + "test"

        sut.readItems("test")

        assert(!sut.serializeTableTemplatesStatus.success)
        assert(sut.serializeTableTemplatesStatus.errorMessage == FILE_DOES_NOT_EXIST + "test")
    }

    @Test
    fun `serialize from readItems FAILS due to EXTERNAL FILE PATH is NULL`() = runBlocking {
        every { mockContext.getExternalFilesDir(any()) } returns null

        sut.readItems("test")

        assert(!sut.serializeTableTemplatesStatus.success)
        assert(sut.serializeTableTemplatesStatus.errorMessage == EXTERNAL_STORAGE_PATH_IS_NULL)
    }

    @Test
    fun `update table items SUCCESS`() = runBlocking {
        coEvery { mockMoshiTableTemplateRepository.save(any()) } returns true
        every { mockCommonSerializationRepoHelper.getInputStream(any()) } returns mockInputStream
        every { mockStringListToSplitItemList.status.success } returns true
        coEvery { mockStringListRepository.load() } returns true
        every { mockStringListRepository.getItem() } returns stringListMoshi
        coEvery { mockStringListRepository.save(any()) } returns true
        every { mockSerializeTableTemplateUpdateCore.status.success } returns true
        coEvery { mockSerializeTableTemplateUpdateCore.update(any(),
            any(), any(),
            any(), any()) } returns Unit
        every { mockSerializeTableTemplateUpdateCore.status.success } returns true
        every { mockSerializeTableTemplateUpdateCore.status.errorMessage } returns ""
        coEvery { mockMoshiTableTemplateRepository.save(any()) } returns true

        sut.updateTableTemplate("testTemplate",
            TableTemplateItemListMoshi("test",
                tableTemplateItems),
            "folder/templatesListFilename")

        assert(sut.serializeUpdateTableTemplateStatus.success)
    }

    @Test
    fun `update table items FAIL due to EXTERNAL FILE PATH is NULL`() = runBlocking {
        every { mockContext.getExternalFilesDir(any()) } returns null

        sut.updateTableTemplate("testTemplate",
            TableTemplateItemListMoshi("test",
                tableTemplateItems),
            "folder/templatesListFilename")

        assert(!sut.serializeUpdateTableTemplateStatus.success)
        assert(sut.serializeUpdateTableTemplateStatus.errorMessage == EXTERNAL_STORAGE_PATH_IS_NULL)
    }

    @Test
    fun `update table items FAIL due to INPUT STREAM is NULL`() = runBlocking {
        every { mockCommonSerializationRepoHelper.getInputStream(any()) } returns null

        sut.updateTableTemplate("testTemplate",
            TableTemplateItemListMoshi("test",
                tableTemplateItems),
            "folder/templatesListFilename")

        val path = TEMPLATES_FOLDER + "folder/templatesListFilename" + JSON_EXTENSION
        assert(!sut.serializeUpdateTableTemplateStatus.success)
        assert(sut.serializeUpdateTableTemplateStatus.errorMessage == FILE_DOES_NOT_EXIST + path)
    }

    @Test
    fun `update table items FAIL due to INPUT STREAM THROWS EXCEPTION`() = runBlocking {
        every { mockCommonSerializationRepoHelper.getInputStream(any()) } throws Exception("error!")

        sut.updateTableTemplate("testTemplate",
            TableTemplateItemListMoshi("test",
                tableTemplateItems),
            "folder/templatesListFilename")

        assert(!sut.serializeUpdateTableTemplateStatus.success)
        assert(sut.serializeUpdateTableTemplateStatus.errorMessage == EXCEPTION_THROWN_MSG + "error!")
    }

    @Test
    fun `update table items FAIL due to StringListToSplitItemList RETURNS FALSE`() = runBlocking {
        every { mockContext.getExternalFilesDir(any()) } returns mockFile
        every { mockCommonSerializationRepoHelper.getInputStream(any()) } returns mockInputStream
        every { mockStringListToSplitItemList.status.success } returns false
        every { mockStringListToSplitItemList.status.errorMessage } returns "error!"

        sut.updateTableTemplate("testTemplate",
            TableTemplateItemListMoshi("test",
                tableTemplateItems),
            "folder/templatesListFilename")

        assert(!sut.serializeUpdateTableTemplateStatus.success)
        assert(sut.serializeUpdateTableTemplateStatus.errorMessage == "error!")
    }

    @Test
    fun `update table items FAIL due to SerializeTableTemplateUpdateCore RETURNS FALSE`() = runBlocking {
        every { mockContext.getExternalFilesDir(any()) } returns mockFile
        every { mockCommonSerializationRepoHelper.getInputStream(any()) } returns mockInputStream
        every { mockStringListToSplitItemList.status.success } returns true
        coEvery { mockSerializeTableTemplateUpdateCore.update(any(),
            any(), any(),
            any(), any()) } returns Unit
        every { mockSerializeTableTemplateUpdateCore.status.success } returns false
        every { mockSerializeTableTemplateUpdateCore.status.errorMessage } returns "error!"

        sut.updateTableTemplate("testTemplate",
            TableTemplateItemListMoshi("test",
                tableTemplateItems),
            "folder/templatesListFilename")

        assert(!sut.serializeUpdateTableTemplateStatus.success)
        assert(sut.serializeUpdateTableTemplateStatus.errorMessage == "error!")
    }

    @Test
    fun `update table items FAIL due to MoshiTableTemplateRepository RETURNS FALSE`() = runBlocking {
        every { mockContext.getExternalFilesDir(any()) } returns mockFile
        every { mockCommonSerializationRepoHelper.getInputStream(any()) } returns mockInputStream
        every { mockStringListToSplitItemList.status.success } returns true
        coEvery { mockSerializeTableTemplateUpdateCore.update(any(),
            any(), any(),
            any(), any()) } returns Unit
        every { mockSerializeTableTemplateUpdateCore.status.success } returns true
        coEvery { mockMoshiTableTemplateRepository.save(any()) } returns false

        sut.updateTableTemplate("testTemplate",
            TableTemplateItemListMoshi("test",
                tableTemplateItems),
            "folder/templatesListFilename")

        assert(!sut.serializeUpdateTableTemplateStatus.success)
        assert(sut.serializeUpdateTableTemplateStatus.errorMessage == FAILED_TO_SAVE_TEMPLATE)
    }

    private fun initMockMoshiTableTemplateInputFiles() {
        every { mockMoshiTableTemplateRepository.setAbsoluteFile(mockFile) } returns Unit
        every { mockMoshiTableTemplateRepository.setBufferReader(any()) } returns Unit
        every { mockMoshiTableTemplateRepository.setFile(any()) } returns Unit
        every { mockMoshiTableTemplateRepository.assignDirectoryFile(any()) } returns Unit
        every { mockMoshiTableTemplateRepository.setFileWriter(any()) } returns Unit
        every { mockMoshiTableTemplateRepository.setItem(any()) } returns Unit
        every { mockStringListRepository.setAbsoluteFile(mockFile) } returns Unit
        every { mockStringListRepository.setFile(any()) } returns Unit
        every { mockStringListRepository.assignDirectoryFile(any()) } returns Unit
        every { mockStringListRepository.setFileWriter(any()) } returns Unit
        every { mockStringListRepository.setItem(any()) } returns Unit
    }

    private fun initMockTableTemplateOutputFiles() {
        every { mockCommonSerializationRepoHelper.getAbsoluteFile(any(), any()) } returns mockFile
        every { mockCommonSerializationRepoHelper.getBufferReader(any(), any()) } returns mockBufferReader
        every { mockCommonSerializationRepoHelper.getMainFile(any()) } returns mockFile
        every { mockCommonSerializationRepoHelper.getDirectoryFile(any()) } returns mockFile
        every { mockCommonSerializationRepoHelper.getFileWriter(any(), any()) } returns mockFileWriter
        every { mockCommonSerializationRepoHelper.getInputStream(any()) } returns mockInputStream
        every { mockMoshiTableTemplateRepository.getItem() } returns tableTemplateItemListMoshi
        every { mockStringListRepository.getItem() } returns stringListMoshi
    }
}