package com.jonpeps.gamescms.viewmodels

import com.jonpeps.gamescms.data.dataclasses.CommonDataItem
import com.jonpeps.gamescms.data.dataclasses.ItemType
import com.jonpeps.gamescms.data.dataclasses.TableItemFinal
import com.jonpeps.gamescms.data.dataclasses.mappers.TableItemFinalMapper
import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateItemListMoshi
import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateItemMoshi
import com.jonpeps.gamescms.data.repositories.base.ISingleItemMoshiJsonRepository
import com.jonpeps.gamescms.data.serialization.ICommonSerializationRepoHelper
import com.jonpeps.gamescms.ui.tabletemplates.serialization.TableTemplateLoader
import com.jonpeps.gamescms.ui.tabletemplates.viewmodels.TableTemplateGroupViewModel
import com.jonpeps.gamescms.ui.tabletemplates.viewmodels.TableTemplateGroupViewModel.Companion.JSON_ITEM_TO_SAVE_IS_NULL
import com.jonpeps.gamescms.ui.tabletemplates.viewmodels.ITableTemplateGroupVmChangesCache
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TableTemplateGroupViewModelTests {
    private val dispatcher = UnconfinedTestDispatcher()
    @MockK
    private lateinit var mockTableTemplateLoader: TableTemplateLoader
    @MockK
    private lateinit var mockTableTemplateRepository
        : ISingleItemMoshiJsonRepository<TableTemplateItemListMoshi>
    @MockK
    private lateinit var mockTableTemplateGroupVmRepoHelper
        : ICommonSerializationRepoHelper
    @MockK
    private lateinit var mockTableTemplateGroupVmChangesCache
        : ITableTemplateGroupVmChangesCache

    private val dummyData = TableTemplateItemListMoshi(
        "test_template",
        listOf(TableTemplateItemMoshi("test",
        dataType = ItemType.STRING)))

    private val dummyLoadedResult = CommonDataItem<List<TableItemFinal>>(
        true,
        arrayListOf(),
        0,
        "",
        null
    )

    private lateinit var sut: TableTemplateGroupViewModel

    private val path = "test/"
    private val templateName = "my_template"

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        sut = TableTemplateGroupViewModel(path,
            mockTableTemplateLoader,
            mockTableTemplateRepository,
            mockTableTemplateGroupVmRepoHelper,
            mockTableTemplateGroupVmChangesCache,
            dispatcher)

        mockkObject(TableItemFinalMapper.Companion) {
            val mockMapperResult1 = mockk<List<TableItemFinal>>()
            every { TableItemFinalMapper.fromTableTemplateListMoshi(any()) } returns mockMapperResult1
            val mockMapperResult2 = mockk<TableTemplateItemListMoshi>()
            every { TableItemFinalMapper.toTableTemplateItemListMoshi(any(), any()) } returns mockMapperResult2
        }

        every { mockTableTemplateGroupVmChangesCache.set(any(), any()) } returns Unit
        coEvery { mockTableTemplateLoader.load(any(), any(), any(), any()) } returns Unit
    }

    @Test
    fun `load template SUCCESS`() {
        every { mockTableTemplateLoader.getItem() } returns dummyLoadedResult

        sut.load(templateName)

        assert(sut.status.value.success)
        assert(sut.status.value.message == "")
        assert(sut.status.value.ex == null)
        assert(sut.status.value.currentIndex == 0)
    }

    @Test
    fun `save template SUCCESS WHEN IO files are VALID and save to repository RETURNS TRUE`() {
        setupForReadingFiles()
        setupForWritingFiles()
        every { mockTableTemplateGroupVmRepoHelper.getAbsoluteFile(path, templateName) } returns mockk()
        every { mockTableTemplateGroupVmRepoHelper.getMainFile(templateName) } returns mockk()
        every { mockTableTemplateGroupVmRepoHelper.getDirectoryFile(path) } returns mockk()
        every { mockTableTemplateGroupVmRepoHelper.getFileWriter(path, templateName) } returns mockk()
        every { mockTableTemplateRepository.getItem() } returns dummyData
        coEvery { mockTableTemplateRepository.save(dummyData) } returns true

        sut.save(templateName)

        assert(sut.status.value.success)
        assert(sut.status.value.message == "")
        assert(sut.status.value.ex == null)
    }

    @Test
    fun `save template FAILURE WHEN IO files are VALID and Json to save is NULL`() {
        setupForWritingFiles()
        setupForReadingFiles()
        every { mockTableTemplateGroupVmRepoHelper.getAbsoluteFile(path, templateName) } returns mockk()
        every { mockTableTemplateGroupVmRepoHelper.getMainFile(templateName) } returns mockk()
        every { mockTableTemplateGroupVmRepoHelper.getDirectoryFile(path) } returns mockk()
        every { mockTableTemplateGroupVmRepoHelper.getFileWriter(path, templateName) } returns mockk()
        every { mockTableTemplateRepository.getItem() } returns null

        sut.save(templateName)

        assert(!sut.status.value.success)
        assert(sut.status.value.message == JSON_ITEM_TO_SAVE_IS_NULL + templateName)
    }

    @Test
    fun `save template FAILURE WHEN IO file THROWS RuntimeException due to INVALID absolute file path`() {
        every { mockTableTemplateGroupVmRepoHelper.getAbsoluteFile(path, templateName) } throws RuntimeException("Runtime error!")

        sut.save(templateName)

        assert(!sut.status.value.success)
        assert(sut.status.value.message == "Runtime error!")
        assert(sut.status.value.ex != null)
    }

    @Test
    fun `test save template fails when IO file throws RuntimeException due to invalid FileWriter`() {
        setupForReadingFiles()
        setupForWritingFiles()
        every { mockTableTemplateGroupVmRepoHelper.getAbsoluteFile(path, templateName) } returns mockk()
        every { mockTableTemplateGroupVmRepoHelper.getMainFile(templateName) } returns mockk()
        every { mockTableTemplateGroupVmRepoHelper.getDirectoryFile(path) } returns mockk()
        every { mockTableTemplateGroupVmRepoHelper.getFileWriter(path, templateName) } throws RuntimeException("Runtime error!")

        sut.save(templateName)

        assert(!sut.status.value.success)
        assert(sut.status.value.message == "Runtime error!")
        assert(sut.status.value.ex != null)
    }

    @Test
    fun `save template FAILURE WHEN IO files are VALID BUT save to repository RETURNS FALSE`() {
        setupForReadingFiles()
        setupForWritingFiles()
        every { mockTableTemplateGroupVmRepoHelper.getAbsoluteFile(path, templateName) } returns mockk()
        every { mockTableTemplateGroupVmRepoHelper.getMainFile(templateName) } returns mockk()
        every { mockTableTemplateGroupVmRepoHelper.getDirectoryFile(path) } returns mockk()
        every { mockTableTemplateGroupVmRepoHelper.getFileWriter(path, templateName) } returns mockk()
        every { mockTableTemplateRepository.getItem() } returns dummyData
        every { mockTableTemplateRepository.getErrorMsg() } returns "An error occurred!"
        coEvery { mockTableTemplateRepository.save(dummyData) } returns false

        sut.save(templateName)

        assert(!sut.status.value.success)
        assert(sut.status.value.message == mockTableTemplateRepository.getErrorMsg())
    }

    @Test
    fun `new template SUCCESS`() {
        sut.new()
        assert(sut.status.value.success)
        assert(sut.status.value.message == "")
        assert(sut.status.value.ex == null)
    }

    @Test
    fun `reset all items in view model SUCCESS`() {
        every { mockTableTemplateGroupVmChangesCache.reset(any()) } returns Unit
        every { mockTableTemplateGroupVmChangesCache.get(any()) } returns listOf()

        sut.reset()

        assert(sut.status.value.success)
        assert(sut.status.value.message == "")
        assert(sut.status.value.ex == null)
    }

    @Test
    fun `ADD page WHEN no pages EXIST`() {
        sut.clearItems()
        sut.addPage()
        assert(sut.status.value.success)
        assert(sut.status.value.message == "")
        assert(sut.status.value.ex == null)
        assert(sut.status.value.items.size == 1)
        assert(sut.status.value.currentIndex == 0)
    }

    @Test
    fun `ADD page WHEN pages EXIST`() {
        sut.clearItems()
        sut.addItem(TableItemFinal("test1", isPrimary = true, isSortKey = true,
            value = "test", editable = true, dataType = ItemType.STRING))
        sut.addPage()
        assert(sut.status.value.success)
        assert(sut.status.value.message == "")
        assert(sut.status.value.ex == null)
        assert(sut.status.value.items.size == 2)
        assert(sut.status.value.currentIndex == 1)
    }

    @Test
    fun `REMOVE page WHEN no pages EXIST`() {
        sut.clearItems()
        sut.removePage()
        assert(sut.status.value.success)
        assert(sut.status.value.message == "")
        assert(sut.status.value.ex == null)
        assert(sut.status.value.items.isEmpty())
    }

    @Test
    fun `REMOVE page WHEN pages EXIST`() {
        sut.clearItems()
        sut.addPage()
        sut.addPage()
        assert(sut.status.value.items.size == 2)
        sut.removePage()
        assert(sut.status.value.success)
        assert(sut.status.value.message == "")
        assert(sut.status.value.ex == null)
        assert(sut.status.value.items.size == 1)
    }

    @Test
    fun `NEXT page WHEN no pages EXIST`() {
        sut.clearItems()
        sut.nextPage()
        assert(sut.status.value.success)
        assert(sut.status.value.message == "")
        assert(sut.status.value.ex == null)
        assert(sut.status.value.items.isEmpty())
        assert(sut.status.value.currentIndex == 0)
    }

    @Test
    fun `NEXT page WHEN only one page EXISTS`() {
        sut.clearItems()
        sut.setIndex(0)
        sut.nextPage()
        assert(sut.status.value.success)
        assert(sut.status.value.message == "")
        assert(sut.status.value.ex == null)
        assert(sut.status.value.items.isEmpty())
        assert(sut.status.value.currentIndex == 0)
        assert(sut.getIndex() == 0)
    }

    @Test
    fun `NEXT page WHEN pages EXIST`() {
        sut.clearItems()
        sut.addPage()
        sut.addPage()
        sut.setIndex(0)
        assert(sut.status.value.items.size == 2)
        sut.nextPage()
        assert(sut.status.value.success)
        assert(sut.status.value.message == "")
        assert(sut.status.value.ex == null)
        assert(sut.status.value.currentIndex == 1)
        assert(sut.getIndex() == 1)
    }

    @Test
    fun `PREVIOUS page WHEN no pages EXIST`() {
        sut.clearItems()
        sut.previousPage()
        assert(sut.status.value.success)
        assert(sut.status.value.message == "")
        assert(sut.status.value.ex == null)
        assert(sut.status.value.items.isEmpty())
        assert(sut.status.value.currentIndex == 0)
    }

    @Test
    fun `PREVIOUS page WHEN pages EXIST`() {
        sut.clearItems()
        sut.addPage()
        sut.addPage()
        assert(sut.status.value.items.size == 2)
        sut.previousPage()
        assert(sut.status.value.success)
        assert(sut.status.value.message == "")
        assert(sut.status.value.ex == null)
        assert(sut.status.value.currentIndex == 0)
    }

    @Test
    fun `PAGE COUNT WHEN pages added`() {
        sut.clearItems()
        sut.addPage()
        sut.addPage()
        assert(sut.pageCount() == 2)
    }

    @Test
    fun `NEW table template SUCCESS`() {
        sut.addPage()
        sut.new()
        assert(sut.status.value.success)
        assert(sut.status.value.message == "")
        assert(sut.status.value.ex == null)
        assert(sut.status.value.items.size == 1)
    }

    @Test
    fun `test hasChanges returns TRUE`() {
        every { mockTableTemplateGroupVmChangesCache.hasChanges(any()) } returns true
        assert(sut.hasChanges())
    }

    @Test
    fun `test hasChanges returns FALSE`() {
        every { mockTableTemplateGroupVmChangesCache.hasChanges(any()) } returns false
        assert(!sut.hasChanges())
    }

    private fun setupForReadingFiles() {
        every { mockTableTemplateRepository.setAbsoluteFile(any()) } returns Unit
        every { mockTableTemplateRepository.setBufferReader(any()) } returns Unit
    }

    private fun setupForWritingFiles() {
        every { mockTableTemplateRepository.assignDirectoryFile(any()) } returns Unit
        every { mockTableTemplateRepository.setFileWriter(any()) } returns Unit
        every { mockTableTemplateRepository.setFile(any()) } returns Unit
        every { mockTableTemplateRepository.setItem(any()) } returns Unit
    }
}