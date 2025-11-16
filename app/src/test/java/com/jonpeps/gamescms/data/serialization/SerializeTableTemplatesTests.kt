package com.jonpeps.gamescms.data.serialization

import android.content.Context
import android.content.res.AssetManager
import com.jonpeps.gamescms.data.helpers.InputStreamTableTemplate
import com.jonpeps.gamescms.data.helpers.StringListToSplitItemList
import com.jonpeps.gamescms.data.repositories.MoshiTableTemplateStatusListRepository
import com.jonpeps.gamescms.ui.viewmodels.SerializeTableTemplates
import com.jonpeps.gamescms.ui.viewmodels.SerializeTableTemplates.Companion.EXCEPTION_THROWN_MSG
import com.jonpeps.gamescms.ui.viewmodels.SerializeTableTemplates.Companion.EXTERNAL_STORAGE_PATH_IS_NULL
import com.jonpeps.gamescms.ui.viewmodels.SerializeTableTemplates.Companion.FAILED_TO_SAVE_TEMPLATES_STATUS
import com.jonpeps.gamescms.ui.viewmodels.SerializeTableTemplates.Companion.FILE_DOES_NOT_EXIST
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.io.File
import java.io.InputStream

class SerializeTableTemplatesTests {
    @MockK
    private lateinit var mockContext: Context
    @MockK
    private lateinit var mockAssetManager: AssetManager
    @MockK
    private lateinit var mockFile: File
    @MockK
    private lateinit var mockStringListToSplitItemList: StringListToSplitItemList
    @MockK
    private lateinit var mockInputStreamTableTemplate: InputStreamTableTemplate
    @MockK
    private lateinit var mockMoshiTableTemplateStatusListRepository: MoshiTableTemplateStatusListRepository
    @MockK
    private lateinit var mockCommonSerializationRepoHelper: CommonSerializationRepoHelper
    @MockK
    private lateinit var mockInputStream: InputStream

    private lateinit var sut: SerializeTableTemplates

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        sut = SerializeTableTemplates(mockContext,
            mockAssetManager,
            mockStringListToSplitItemList,
            mockInputStreamTableTemplate,
            mockMoshiTableTemplateStatusListRepository,
            mockCommonSerializationRepoHelper)

        coEvery { mockStringListToSplitItemList.loadSuspend(any()) } returns Unit
        every { mockAssetManager.open(any()) } returns mockInputStream

        every { mockStringListToSplitItemList.status.fileNames } returns listOf("test.json")
        every { mockStringListToSplitItemList.status.names } returns listOf("test")
        every { mockFile.absolutePath } returns "testFolder/"
    }

    @Test
    fun `serialize from ASSETS SUCCESS`() = runBlocking {
        every { mockContext.getExternalFilesDir(any()) } returns mockFile
        every { mockStringListToSplitItemList.status.success } returns true
        every { mockStringListToSplitItemList.status.fileNames } returns listOf("test.json")
        every { mockStringListToSplitItemList.status.names } returns listOf("test")
        every { mockContext.getExternalFilesDir(any()) } returns mockFile
        every { mockFile.absolutePath } returns "testFolder/"
        coEvery { mockInputStreamTableTemplate.processSuspend(any(), any(), any()) } returns Unit
        every { mockInputStreamTableTemplate.status.success } returns true
        every { mockInputStreamTableTemplate.status.errorMessage } returns ""
        coEvery { mockMoshiTableTemplateStatusListRepository.save(any()) } returns true

        sut.serializeFromAssets("test")

        assert(sut.serializeTableTemplatesStatus.success)
        assert(sut.serializeTableTemplatesStatus.items?.get(0)?.name == "test")
        assert(sut.serializeTableTemplatesStatus.items?.get(0)?.file == "test.json")
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
    fun `serialize from ASSETS FAILS due to saving of status list FAILS`() = runBlocking {
        every { mockStringListToSplitItemList.status.success } returns true
        every { mockStringListToSplitItemList.status.fileNames } returns listOf("test.json")
        every { mockStringListToSplitItemList.status.names } returns listOf("test")
        every { mockContext.getExternalFilesDir(any()) } returns mockFile
        every { mockFile.absolutePath } returns "testFolder/"
        coEvery { mockInputStreamTableTemplate.processSuspend(any(), any(), any()) } returns Unit
        every { mockInputStreamTableTemplate.status.success } returns true
        every { mockInputStreamTableTemplate.status.errorMessage } returns ""
        coEvery { mockMoshiTableTemplateStatusListRepository.save(any()) } returns false

        sut.serializeFromAssets("test")

        assert(!sut.serializeTableTemplatesStatus.success)
        assert(sut.serializeTableTemplatesStatus.errorMessage == FAILED_TO_SAVE_TEMPLATES_STATUS)
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
    fun `serialize from ITEMS SUCCESS`() = runBlocking {
        every { mockCommonSerializationRepoHelper.getInputStream(any()) } returns mockInputStream
        every { mockStringListToSplitItemList.status.success } returns true
        every { mockStringListToSplitItemList.status.fileNames } returns listOf("test.json")
        every { mockStringListToSplitItemList.status.names } returns listOf("test")
        every { mockContext.getExternalFilesDir(any()) } returns mockFile
        every { mockFile.absolutePath } returns "testFolder/"
        coEvery { mockInputStreamTableTemplate.processSuspend(any(), any(), any()) } returns Unit

        sut.serializeItems("test")

        assert(sut.serializeTableTemplatesStatus.success)
        assert(sut.serializeTableTemplatesStatus.errorMessage == "")
        assert(sut.serializeTableTemplatesStatus.items?.get(0)?.name == "test")
        assert(sut.serializeTableTemplatesStatus.items?.get(0)?.file == "test.json")
    }

    @Test
    fun `serialize FAILS due to INPUT STREAM of main FILE is NULL`() = runBlocking {
        every { mockFile.path } returns "test"
        every { mockFile.absolutePath } returns "testFolder/path"
        every { mockCommonSerializationRepoHelper.getInputStream(any()) } returns null
        every { mockStringListToSplitItemList.status.errorMessage } returns FILE_DOES_NOT_EXIST + "test"

        sut.serializeItems("test")

        assert(!sut.serializeTableTemplatesStatus.success)
        assert(sut.serializeTableTemplatesStatus.errorMessage == "File does not exist: test")
    }

    @Test
    fun `serialize ITEMS FAILS due to INPUT STREAM throwing EXCEPTION`() = runBlocking {
        every { mockCommonSerializationRepoHelper.getInputStream(any()) } throws Exception("error")

        sut.serializeItems("test")

        assert(!sut.serializeTableTemplatesStatus.success)
        assert(sut.serializeTableTemplatesStatus.errorMessage == EXCEPTION_THROWN_MSG + "error")
    }

    @Test
    fun `serialize ITEMS FAILS due to StringListToSplitItemList RETURNS FALSE`() = runBlocking {
        every { mockCommonSerializationRepoHelper.getInputStream(any()) } returns mockInputStream
        every { mockStringListToSplitItemList.status.success } returns false
        every { mockStringListToSplitItemList.status.errorMessage } returns FILE_DOES_NOT_EXIST + "test"

        sut.serializeItems("test")

        assert(!sut.serializeTableTemplatesStatus.success)
        assert(sut.serializeTableTemplatesStatus.errorMessage == FILE_DOES_NOT_EXIST + "test")
    }
}