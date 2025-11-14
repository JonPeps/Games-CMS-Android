package com.jonpeps.gamescms.data.serialization

import android.content.Context
import android.content.res.AssetManager
import com.jonpeps.gamescms.data.helpers.InputStreamTableTemplate
import com.jonpeps.gamescms.data.helpers.StringListToSplitItemList
import com.jonpeps.gamescms.data.repositories.MoshiTableTemplateStatusListRepository
import com.jonpeps.gamescms.ui.viewmodels.defaults.SerializeDefaultTemplates
import com.jonpeps.gamescms.ui.viewmodels.defaults.SerializeDefaultTemplates.Companion.FAILED_TO_SAVE_TEMPLATES_STATUS
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

class SerializeDefaultTemplatesTests {
    @MockK
    private lateinit var mockContext: Context
    @MockK
    private lateinit var mockAssetManager: AssetManager
    @MockK
    private lateinit var mockFile: File
    @MockK
    private lateinit var mockFileInputStream: FileInputStream
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

    private lateinit var sut: SerializeDefaultTemplates

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        sut = SerializeDefaultTemplates(mockContext,
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
        every { mockContext.getExternalFilesDir(any()) } returns mockFile
    }

    @Test
    fun `serialize from ASSETS SUCCESS`() = runBlocking {
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

        assert(sut.serializeDefaultTemplatesStatus.success)
        assert(sut.serializeDefaultTemplatesStatus.errorMessage == "")
    }

    @Test
    fun `serialize from FILE SUCCESS`() = runBlocking {
        every { mockFile.exists() } returns true
        every { mockFile.inputStream() } returns mockFileInputStream
        every { mockStringListToSplitItemList.status.success } returns true
        every { mockCommonSerializationRepoHelper.getInputStream(mockFile) } returns mockFileInputStream
        coEvery { mockMoshiTableTemplateStatusListRepository.save(any()) } returns true
        every { mockInputStreamTableTemplate.status.errorMessage } returns ""

        sut.serializeFromFile(mockFile)

        assert(sut.serializeDefaultTemplatesStatus.success)
        assert(sut.serializeDefaultTemplatesStatus.errorMessage == "")
    }

    @Test
    fun `serialize from ASSETS FAILS due to StringListToSplitItemList RETURNS FALSE`() = runBlocking {
        every { mockStringListToSplitItemList.status.success } returns false
        every { mockStringListToSplitItemList.status.errorMessage } returns "error"

        sut.serializeFromAssets("test")

        assert(!sut.serializeDefaultTemplatesStatus.success)
        assert(sut.serializeDefaultTemplatesStatus.errorMessage == "error")
    }

    @Test
    fun `serialize from ASSETS FAILS due to EXCEPTION THROWN by StringListToSplitItemList`() = runBlocking {
        every { mockAssetManager.open(any()) } returns mockInputStream
        coEvery { mockStringListToSplitItemList.loadSuspend(any()) } throws Exception("error")

        sut.serializeFromAssets("test")

        assert(!sut.serializeDefaultTemplatesStatus.success)
        assert(sut.serializeDefaultTemplatesStatus.errorMessage == "error")
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

        assert(!sut.serializeDefaultTemplatesStatus.success)
        assert(sut.serializeDefaultTemplatesStatus.errorMessage == FAILED_TO_SAVE_TEMPLATES_STATUS)
    }


}