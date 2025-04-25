package com.jonpeps.gamescms.createtable.viewmodels

import com.jonpeps.gamescms.data.dataclasses.ItemType
import com.jonpeps.gamescms.data.dataclasses.TableItemFinal
import com.jonpeps.gamescms.data.dataclasses.mappers.TableItemFinalMapper
import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateItemListMoshi
import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateItemMoshi
import com.jonpeps.gamescms.ui.createtable.helpers.ITableTemplateGroupVmRepoHelper
import com.jonpeps.gamescms.ui.createtable.viewmodels.TableTemplateGroupViewModel
import com.jonpeps.gamescms.ui.createtable.viewmodels.TableTemplateGroupViewModel.Companion.JSON_ITEM_TO_SAVE_IS_NULL
import com.jonpeps.gamescms.ui.tabletemplates.repositories.ITableTemplateFileRepository
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
    private lateinit var tableTemplateRepository: ITableTemplateFileRepository
    @MockK
    private lateinit var tableTemplateGroupVmRepoHelper: ITableTemplateGroupVmRepoHelper

    private val dummyData = TableTemplateItemListMoshi("test_template", listOf(TableTemplateItemMoshi("test",
        dataType = ItemType.STRING)))

    private lateinit var viewModel: TableTemplateGroupViewModel

    private val path = "test/"
    private val templateName = "my_template"

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        viewModel = TableTemplateGroupViewModel(path,
            tableTemplateRepository,
            tableTemplateGroupVmRepoHelper,
            dispatcher)

        mockkObject(TableItemFinalMapper.Companion) {
            val mockMapperResult1 = mockk<List<TableItemFinal>>()
            every { TableItemFinalMapper.fromTableTemplateListMoshi(any()) } returns mockMapperResult1
            val mockMapperResult2 = mockk<TableTemplateItemListMoshi>()
            every { TableItemFinalMapper.toTableTemplateItemListMoshi(any(), any()) } returns mockMapperResult2
        }
    }

    @Test
    fun `test load template success when IO files are correct and load from repo is true`() {
        setupForReadingFiles()
        every { tableTemplateGroupVmRepoHelper.getAbsoluteFile(path, templateName) } returns mockk()
        every { tableTemplateGroupVmRepoHelper.getBufferReader(path, templateName) } returns mockk()
        every { tableTemplateRepository.getItem() } returns dummyData
        coEvery { tableTemplateRepository.load() } returns true

        viewModel.load(templateName)

        assert(viewModel.status.value.success)
        assert(viewModel.status.value.message == "")
        assert(viewModel.status.value.ex == null)
        assert(viewModel.status.value.items.size == 1)
        assert(viewModel.status.value.currentIndex == 0)
    }

    @Test
    fun `test load template failure when IO files are correct and load from repo is false`() {
        setupForReadingFiles()
        every { tableTemplateGroupVmRepoHelper.getAbsoluteFile(path, templateName) } returns mockk()
        every { tableTemplateGroupVmRepoHelper.getBufferReader(path, templateName) } returns mockk()
        every { tableTemplateRepository.getErrorMsg() } returns "An error occurred!"
        coEvery { tableTemplateRepository.load() } returns false

        viewModel.load(templateName)

        assert(!viewModel.status.value.success)
        assert(viewModel.status.value.message == tableTemplateRepository.getErrorMsg())
        assert(viewModel.status.value.ex == null)
    }

    @Test
    fun `test load template failure when IO files throw RuntimeException due to invalid File`() {
        every { tableTemplateGroupVmRepoHelper.getAbsoluteFile(any(), any()) } throws RuntimeException("Runtime error!")

        viewModel.load(templateName)

        assert(!viewModel.status.value.success)
        assert(viewModel.status.value.message == "Runtime error!")
        assert(viewModel.status.value.ex != null)
    }

    @Test
    fun `test load template failure when IO files throw RuntimeException due to invalid BufferReader`() {
        setupForReadingFiles()
        setupForWritingFiles()
        every { tableTemplateGroupVmRepoHelper.getAbsoluteFile(any(), any()) } returns mockk()
        every { tableTemplateGroupVmRepoHelper.getBufferReader(any(), any()) } throws RuntimeException("Runtime error!")

        viewModel.load(templateName)

        assert(!viewModel.status.value.success)
        assert(viewModel.status.value.message == "Runtime error!")
        assert(viewModel.status.value.ex != null)
    }

    @Test
    fun `test load template failure when IO files are correct but Json item is null`() {
        setupForReadingFiles()
        every { tableTemplateGroupVmRepoHelper.getAbsoluteFile(path, templateName) } returns mockk()
        every { tableTemplateGroupVmRepoHelper.getBufferReader(path, templateName) } returns mockk()
        coEvery { tableTemplateRepository.load() } returns true
        every { tableTemplateRepository.getItem() } returns null

        viewModel.load(templateName)

        assert(!viewModel.status.value.success)
        assert(viewModel.status.value.message == JSON_ITEM_TO_SAVE_IS_NULL + templateName)
        assert(viewModel.status.value.ex == null)
    }

    @Test
    fun `test save template success when IO files are correct and save to repo is true`() {
        setupForReadingFiles()
        setupForWritingFiles()
        every { tableTemplateGroupVmRepoHelper.getAbsoluteFile(path, templateName) } returns mockk()
        every { tableTemplateGroupVmRepoHelper.getMainFile(templateName) } returns mockk()
        every { tableTemplateGroupVmRepoHelper.getDirectoryFile(path) } returns mockk()
        every { tableTemplateGroupVmRepoHelper.getFileWriter(path, templateName) } returns mockk()
        every { tableTemplateRepository.getItem() } returns dummyData
        coEvery { tableTemplateRepository.save(dummyData) } returns true

        viewModel.save(templateName)

        assert(viewModel.status.value.success)
        assert(viewModel.status.value.message == "")
        assert(viewModel.status.value.ex == null)
    }

    @Test
    fun `test save template fails when IO files are correct and Json to save is null`() {
        setupForWritingFiles()
        setupForReadingFiles()
        every { tableTemplateGroupVmRepoHelper.getAbsoluteFile(path, templateName) } returns mockk()
        every { tableTemplateGroupVmRepoHelper.getMainFile(templateName) } returns mockk()
        every { tableTemplateGroupVmRepoHelper.getDirectoryFile(path) } returns mockk()
        every { tableTemplateGroupVmRepoHelper.getFileWriter(path, templateName) } returns mockk()
        every { tableTemplateRepository.getItem() } returns null

        viewModel.save(templateName)

        assert(!viewModel.status.value.success)
        assert(viewModel.status.value.message == JSON_ITEM_TO_SAVE_IS_NULL + templateName)
    }

    @Test
    fun `test save template fails when IO file throws RuntimeException due to invalid absolute file path`() {
        every { tableTemplateGroupVmRepoHelper.getAbsoluteFile(path, templateName) } throws RuntimeException("Runtime error!")

        viewModel.save(templateName)

        assert(!viewModel.status.value.success)
        assert(viewModel.status.value.message == "Runtime error!")
        assert(viewModel.status.value.ex != null)
    }

    @Test
    fun `test save template fails when IO file throws RuntimeException due to invalid FileWriter`() {
        setupForReadingFiles()
        setupForWritingFiles()
        every { tableTemplateGroupVmRepoHelper.getAbsoluteFile(path, templateName) } returns mockk()
        every { tableTemplateGroupVmRepoHelper.getMainFile(templateName) } returns mockk()
        every { tableTemplateGroupVmRepoHelper.getDirectoryFile(path) } returns mockk()
        every { tableTemplateGroupVmRepoHelper.getFileWriter(path, templateName) } throws RuntimeException("Runtime error!")

        viewModel.save(templateName)

        assert(!viewModel.status.value.success)
        assert(viewModel.status.value.message == "Runtime error!")
        assert(viewModel.status.value.ex != null)
    }

    @Test
    fun `test save template fails when IO files are correct and save to repo is false`() {
        setupForReadingFiles()
        setupForWritingFiles()
        every { tableTemplateGroupVmRepoHelper.getAbsoluteFile(path, templateName) } returns mockk()
        every { tableTemplateGroupVmRepoHelper.getMainFile(templateName) } returns mockk()
        every { tableTemplateGroupVmRepoHelper.getDirectoryFile(path) } returns mockk()
        every { tableTemplateGroupVmRepoHelper.getFileWriter(path, templateName) } returns mockk()
        every { tableTemplateRepository.getItem() } returns dummyData
        every { tableTemplateRepository.getErrorMsg() } returns "An error occurred!"
        coEvery { tableTemplateRepository.save(dummyData) } returns false

        viewModel.save(templateName)

        assert(!viewModel.status.value.success)
        assert(viewModel.status.value.message == tableTemplateRepository.getErrorMsg())
    }

    @Test
    fun `test add page when no pages exist`() {
        viewModel.clearItems()
        viewModel.addPage()
        assert(viewModel.status.value.success)
        assert(viewModel.status.value.message == "")
        assert(viewModel.status.value.ex == null)
        assert(viewModel.status.value.items.size == 1)
        assert(viewModel.status.value.currentIndex == 0)
    }

    @Test
    fun `test add page when pages exist`() {
        viewModel.clearItems()
        viewModel.addItem(TableItemFinal("test1", isPrimary = true, isSortKey = true,
            value = "test", editable = true, dataType = ItemType.STRING))
        viewModel.addPage()
        assert(viewModel.status.value.success)
        assert(viewModel.status.value.message == "")
        assert(viewModel.status.value.ex == null)
        assert(viewModel.status.value.items.size == 2)
        assert(viewModel.status.value.currentIndex == 1)
    }

    @Test
    fun `test remove page when no pages exist`() {
        viewModel.clearItems()
        viewModel.removePage()
        assert(viewModel.status.value.success)
        assert(viewModel.status.value.message == "")
        assert(viewModel.status.value.ex == null)
        assert(viewModel.status.value.items.size == 0)
    }

    @Test
    fun `test remove page when pages exist`() {
        viewModel.clearItems()
        viewModel.addPage()
        viewModel.addPage()
        assert(viewModel.status.value.items.size == 2)
        viewModel.removePage()
        assert(viewModel.status.value.success)
        assert(viewModel.status.value.message == "")
        assert(viewModel.status.value.ex == null)
        assert(viewModel.status.value.items.size == 1)
    }

    @Test
    fun `test next page when no pages exist`() {
        viewModel.clearItems()
        viewModel.nextPage()
        assert(viewModel.status.value.success)
        assert(viewModel.status.value.message == "")
        assert(viewModel.status.value.ex == null)
        assert(viewModel.status.value.items.size == 0)
        assert(viewModel.status.value.currentIndex == 0)
    }

    @Test
    fun `test next page when only one page exist`() {
        viewModel.clearItems()
        viewModel.setIndex(0)
        viewModel.nextPage()
        assert(viewModel.status.value.success)
        assert(viewModel.status.value.message == "")
        assert(viewModel.status.value.ex == null)
        assert(viewModel.status.value.items.size == 0)
        assert(viewModel.status.value.currentIndex == 0)
        assert(viewModel.getIndex() == 0)
    }

    @Test
    fun `test next page when pages exist`() {
        viewModel.clearItems()
        viewModel.addPage()
        viewModel.addPage()
        viewModel.setIndex(0)
        assert(viewModel.status.value.items.size == 2)
        viewModel.nextPage()
        assert(viewModel.status.value.success)
        assert(viewModel.status.value.message == "")
        assert(viewModel.status.value.ex == null)
        assert(viewModel.status.value.currentIndex == 1)
        assert(viewModel.getIndex() == 1)
    }

    @Test
    fun `test previous page when no pages exist`() {
        viewModel.clearItems()
        viewModel.previousPage()
        assert(viewModel.status.value.success)
        assert(viewModel.status.value.message == "")
        assert(viewModel.status.value.ex == null)
        assert(viewModel.status.value.items.size == 0)
        assert(viewModel.status.value.currentIndex == 0)
    }

    @Test
    fun `test previous page when pages exist`() {
        viewModel.clearItems()
        viewModel.addPage()
        viewModel.addPage()
        assert(viewModel.status.value.items.size == 2)
        viewModel.previousPage()
        assert(viewModel.status.value.success)
        assert(viewModel.status.value.message == "")
        assert(viewModel.status.value.ex == null)
        assert(viewModel.status.value.currentIndex == 0)
    }

    @Test
    fun `set row name when name is empty`() {
        viewModel.clearItems()
        viewModel.setRowName("")
        assert(viewModel.rowNameEmpty.value)
        assert(!viewModel.duplicateName.value)
    }

    @Test
    fun `set row name when name is not empty and no duplicates`() {
        viewModel.clearItems()
        viewModel.addItem(TableItemFinal("test1", isPrimary = true, isSortKey = true,
            value = "test", editable = true, dataType = ItemType.STRING))
        viewModel.setRowName("test2")
        assert(!viewModel.rowNameEmpty.value)
        assert(!viewModel.duplicateName.value)
    }

    @Test
    fun `set row name when name is not empty and is duplicate`() {
        viewModel.clearItems()
        viewModel.addItem(TableItemFinal("test1", isPrimary = true, isSortKey = true,
            value = "test", editable = true, dataType = ItemType.STRING))
        viewModel.setRowName("test1")
        assert(!viewModel.rowNameEmpty.value)
        assert(viewModel.duplicateName.value)
    }

    @Test
    fun `set row data type`() {
        viewModel.clearItems()
        viewModel.addItem(TableItemFinal("test1", isPrimary = true, isSortKey = true,
            value = "test", editable = true, dataType = ItemType.BOOLEAN))
        var item = viewModel.getCurrentPage()
        assert(item.dataType == ItemType.BOOLEAN)
        viewModel.setItemType(ItemType.STRING)
        item = viewModel.getCurrentPage()
        assert(item.dataType == ItemType.STRING)
    }

    @Test
    fun `set default value when row is editable`() {
        viewModel.clearItems()
        viewModel.addItem(TableItemFinal("test1", isPrimary = true, isSortKey = true,
            value = "test", editable = true, dataType = ItemType.STRING))
        viewModel.setDefaultValue("test")
        val item = viewModel.getCurrentPage()
        assert(item.value == "test")
        assert(!viewModel.noValueWithNotEditable.value)
    }

    @Test
    fun `set default value when row is not editable`() {
        viewModel.clearItems()
        viewModel.addItem(TableItemFinal("test1", isPrimary = true, isSortKey = true,
            value = "test", editable = false, dataType = ItemType.STRING))
        viewModel.setDefaultValue("test")
        val item = viewModel.getCurrentPage()
        assert(item.value == "test")
        assert(!viewModel.noValueWithNotEditable.value)
    }

    @Test
    fun `set no default value when row is editable`() {
        viewModel.clearItems()
        viewModel.addItem(TableItemFinal("test1", isPrimary = true, isSortKey = true,
            value = "test", editable = false, dataType = ItemType.STRING))
        viewModel.setDefaultValue("")
        val item = viewModel.getCurrentPage()
        assert(item.value == "")
        assert(viewModel.noValueWithNotEditable.value)
    }

    @Test
    fun `set is editable with no default value required`() {
        viewModel.clearItems()
        viewModel.addItem(TableItemFinal("test1", isPrimary = true, isSortKey = true,
            value = "test", editable = false, dataType = ItemType.STRING))
        viewModel.setIsEditable(true)
        val item = viewModel.getCurrentPage()
        assert(item.editable)
        assert(!viewModel.noValueWithNotEditable.value)
    }

    @Test
    fun `set is editable with default value empty`() {
        viewModel.clearItems()
        viewModel.addItem(TableItemFinal("test1", isPrimary = true, isSortKey = true,
            value = "test", editable = false, dataType = ItemType.STRING))
        viewModel.setDefaultValue("")
        viewModel.setIsEditable(false)
        viewModel.setIndex(0)
        val item = viewModel.getCurrentPage()
        assert(!item.editable)
        assert(viewModel.noValueWithNotEditable.value)
    }

    @Test
    fun `set is editable with default value required`() {
        viewModel.clearItems()
        viewModel.addItem(TableItemFinal("test1", isPrimary = true, isSortKey = true,
            value = "test", editable = true, dataType = ItemType.STRING))
        viewModel.setDefaultValue("")
        viewModel.setIsEditable(false)
        val item = viewModel.getCurrentPage()
        assert(!item.editable)
        assert(viewModel.noValueWithNotEditable.value)
    }

    @Test
    fun `set is editable with default value present`() {
        viewModel.clearItems()
        viewModel.addItem(TableItemFinal("test1", isPrimary = true, isSortKey = true,
            value = "test", editable = false, dataType = ItemType.STRING))
        viewModel.setDefaultValue("test value")
        viewModel.setIsEditable(true)
        val item = viewModel.getCurrentPage()
        assert(item.editable)
        assert(!viewModel.noValueWithNotEditable.value)
    }

    @Test
    fun `set is primary`() {
        viewModel.clearItems()
        viewModel.addItem(TableItemFinal("test1", isPrimary = false, isSortKey = true,
            value = "test", editable = true, dataType = ItemType.STRING))
        viewModel.setPrimary(true)
        val item = viewModel.getCurrentPage()
        assert(item.isPrimary)
        assert(!viewModel.noPrimaryKeyFound.value)
    }

    @Test
    fun `set is not primary with no other rows set as primary`() {
        viewModel.clearItems()
        viewModel.addItem(TableItemFinal("test1", isPrimary = false, isSortKey = true,
            value = "test", editable = true, dataType = ItemType.STRING))
        viewModel.setPrimary(false)
        val item = viewModel.getCurrentPage()
        assert(!item.isPrimary)
        assert(viewModel.noPrimaryKeyFound.value)
    }

    @Test
    fun `set is not primary with other row set as primary`() {
        viewModel.clearItems()
        viewModel.addItem(TableItemFinal("test1", isPrimary = true, isSortKey = true,
            value = "test", editable = true, dataType = ItemType.STRING))
        viewModel.addItem(TableItemFinal("test2", isPrimary = true, isSortKey = true,
            value = "test", editable = true, dataType = ItemType.STRING))
        viewModel.setIndex(0)
        viewModel.setPrimary(false)
        assert(!viewModel.noPrimaryKeyFound.value)
    }

    @Test
    fun `set is sort key`() {
        viewModel.clearItems()
        viewModel.addItem(TableItemFinal("test1", isPrimary = true, isSortKey = false,
            value = "test", editable = true, dataType = ItemType.STRING))
        viewModel.setSortKey(true)
        val item = viewModel.getCurrentPage()
        assert(item.isSortKey)
        assert(!viewModel.noSortKeyFound.value)
    }

    @Test
    fun `set is not sort key with no other rows set as sort key`() {
        viewModel.clearItems()
        viewModel.addItem(TableItemFinal("test1", isPrimary = true, isSortKey = true,
            value = "test", editable = true, dataType = ItemType.STRING))
        viewModel.setSortKey(false)
        val item = viewModel.getCurrentPage()
        assert(!item.isSortKey)
        assert(viewModel.noSortKeyFound.value)
    }

    @Test
    fun `set is not sort key with other row set as sort key`() {
        viewModel.clearItems()
        viewModel.addItem(TableItemFinal("test1", isPrimary = true, isSortKey = false,
            value = "test", editable = true, dataType = ItemType.STRING))
        viewModel.addItem(TableItemFinal("test2", isPrimary = false, isSortKey = true,
            value = "test", editable = true, dataType = ItemType.STRING))
        viewModel.setIndex(0)
        viewModel.setSortKey(false)
        assert(!viewModel.noSortKeyFound.value)
    }

    @Test
    fun `test page count`() {
        viewModel.clearItems()
        viewModel.addPage()
        viewModel.addPage()
        assert(viewModel.pageCount() == 2)
    }

    @Test
    fun `test new table template`() {
        viewModel.addPage()
        viewModel.new()
        assert(viewModel.status.value.success)
        assert(viewModel.status.value.message == "")
        assert(viewModel.status.value.ex == null)
        assert(viewModel.status.value.items.size == 1)
    }

    private fun setupForReadingFiles() {
        every { tableTemplateRepository.setAbsoluteFile(any()) } returns Unit
        every { tableTemplateRepository.setBufferReader(any()) } returns Unit
    }

    private fun setupForWritingFiles() {
        every { tableTemplateRepository.setDirectoryFile(any()) } returns Unit
        every { tableTemplateRepository.setFileWriter(any()) } returns Unit
        every { tableTemplateRepository.setFile(any()) } returns Unit
        every { tableTemplateRepository.setItem(any()) } returns Unit
    }
}