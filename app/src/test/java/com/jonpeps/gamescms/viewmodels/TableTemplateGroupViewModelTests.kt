package com.jonpeps.gamescms.viewmodels

import com.jonpeps.gamescms.data.dataclasses.ItemType
import com.jonpeps.gamescms.data.dataclasses.TableItemFinal
import com.jonpeps.gamescms.data.dataclasses.mappers.TableItemFinalMapper
import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateItemListMoshi
import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateItemMoshi
import com.jonpeps.gamescms.data.serialization.ICommonSerializationRepoHelper
import com.jonpeps.gamescms.ui.tabletemplates.viewmodels.TableTemplateGroupViewModel
import com.jonpeps.gamescms.ui.tabletemplates.viewmodels.TableTemplateGroupViewModel.Companion.JSON_ITEM_TO_SAVE_IS_NULL
import com.jonpeps.gamescms.ui.tabletemplates.repositories.ITableTemplateFileRepository
import com.jonpeps.gamescms.ui.tabletemplates.viewmodels.ITableTemplateGroupVmChangesCache
import com.jonpeps.gamescms.ui.tabletemplates.viewmodels.TableTemplateGroupViewModel.Companion.JSON_ITEM_TO_LOAD_IS_NULL
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
    private lateinit var tableTemplateGroupVmRepoHelper: ICommonSerializationRepoHelper
    @MockK
    private lateinit var tableTemplateGroupVmChangesCache: ITableTemplateGroupVmChangesCache

    private val dummyData = TableTemplateItemListMoshi("test_template",
        listOf(TableTemplateItemMoshi("test",
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
            tableTemplateGroupVmChangesCache,
            dispatcher)

        mockkObject(TableItemFinalMapper.Companion) {
            val mockMapperResult1 = mockk<List<TableItemFinal>>()
            every { TableItemFinalMapper.fromTableTemplateListMoshi(any()) } returns mockMapperResult1
            val mockMapperResult2 = mockk<TableTemplateItemListMoshi>()
            every { TableItemFinalMapper.toTableTemplateItemListMoshi(any(), any()) } returns mockMapperResult2
        }

        every { tableTemplateGroupVmChangesCache.set(any(), any()) } returns Unit
    }

    @Test
    fun `load template SUCCESS WHEN IO files are VALID and load from repository RETURNS TRUE`() {
        setupForReadingFiles()
        every { tableTemplateGroupVmRepoHelper.getAbsoluteFile(path, templateName) } returns mockk()
        every { tableTemplateGroupVmRepoHelper.getBufferReader(path, templateName) } returns mockk()
        every { tableTemplateGroupVmChangesCache.isPopulated() } returns false
        every { tableTemplateRepository.getItem(templateName) } returns dummyData
        coEvery { tableTemplateRepository.load(templateName) } returns true

        viewModel.load(templateName, false)

        assert(viewModel.status.value.success)
        assert(viewModel.status.value.message == "")
        assert(viewModel.status.value.ex == null)
        assert(viewModel.status.value.items.size == 1)
        assert(viewModel.status.value.currentIndex == 0)
    }

    @Test
    fun `load template FAILURE WHEN IO files are VALID AND load from repository RETURNS false`() {
        setupForReadingFiles()
        every { tableTemplateGroupVmRepoHelper.getAbsoluteFile(path, templateName) } returns mockk()
        every { tableTemplateGroupVmRepoHelper.getBufferReader(path, templateName) } returns mockk()
        every { tableTemplateGroupVmChangesCache.isPopulated() } returns false
        every { tableTemplateRepository.getErrorMsg() } returns "An error occurred!"
        coEvery { tableTemplateRepository.load(templateName) } returns false

        viewModel.load(templateName)

        assert(!viewModel.status.value.success)
        assert(viewModel.status.value.message == tableTemplateRepository.getErrorMsg())
        assert(viewModel.status.value.ex == null)
    }

    @Test
    fun `load template FAILURE WHEN IO files THROW RuntimeException due to INVALID File`() {
        every { tableTemplateGroupVmRepoHelper.getAbsoluteFile(any(), any()) } throws RuntimeException("Runtime error!")
        every { tableTemplateGroupVmChangesCache.isPopulated() } returns false

        viewModel.load(templateName)

        assert(!viewModel.status.value.success)
        assert(viewModel.status.value.message == "Runtime error!")
        assert(viewModel.status.value.ex != null)
    }

    @Test
    fun `load template FAILURE WHEN IO files THROW RuntimeException due to INVALID BufferReader`() {
        setupForReadingFiles()
        setupForWritingFiles()
        every { tableTemplateGroupVmRepoHelper.getAbsoluteFile(any(), any()) } returns mockk()
        every { tableTemplateGroupVmRepoHelper.getBufferReader(any(), any()) } throws RuntimeException("Runtime error!")
        every { tableTemplateGroupVmChangesCache.isPopulated() } returns false

        viewModel.load(templateName)

        assert(!viewModel.status.value.success)
        assert(viewModel.status.value.message == "Runtime error!")
        assert(viewModel.status.value.ex != null)
    }

    @Test
    fun `load template FAILURE WHEN IO files are correct BUT Json item is NULL`() {
        setupForReadingFiles()
        every { tableTemplateGroupVmRepoHelper.getAbsoluteFile(path, templateName) } returns mockk()
        every { tableTemplateGroupVmRepoHelper.getBufferReader(path, templateName) } returns mockk()
        coEvery { tableTemplateRepository.load(templateName) } returns true
        every { tableTemplateRepository.getItem(templateName) } returns null

        viewModel.load(templateName, false)

        assert(!viewModel.status.value.success)
        assert(viewModel.status.value.message == JSON_ITEM_TO_LOAD_IS_NULL + templateName)
        assert(viewModel.status.value.ex == null)
    }

    @Test
    fun `load template from cache SUCCESS`() {
        every { tableTemplateGroupVmChangesCache.isPopulated() } returns true
        every { tableTemplateGroupVmChangesCache.get(templateName) } returns listOf(
            TableItemFinal("test", isPrimary = true, isSortKey = true,
                value = "test", editable = true, dataType = ItemType.STRING))

        viewModel.load(templateName, true)

        assert(viewModel.status.value.success)
        assert(viewModel.status.value.message == "")
        assert(viewModel.status.value.ex == null)
        assert(viewModel.status.value.items.size == 1)
        assert(viewModel.status.value.currentIndex == 0)
    }

    @Test
    fun `save template SUCCESS WHEN IO files are VALID and save to repository RETURNS TRUE`() {
        setupForReadingFiles()
        setupForWritingFiles()
        every { tableTemplateGroupVmRepoHelper.getAbsoluteFile(path, templateName) } returns mockk()
        every { tableTemplateGroupVmRepoHelper.getMainFile(templateName) } returns mockk()
        every { tableTemplateGroupVmRepoHelper.getDirectoryFile(path) } returns mockk()
        every { tableTemplateGroupVmRepoHelper.getFileWriter(path, templateName) } returns mockk()
        every { tableTemplateRepository.getItem(templateName) } returns dummyData
        coEvery { tableTemplateRepository.save(templateName, dummyData) } returns true

        viewModel.save(templateName)

        assert(viewModel.status.value.success)
        assert(viewModel.status.value.message == "")
        assert(viewModel.status.value.ex == null)
    }

    @Test
    fun `save template FAILURE WHEN IO files are VALID and Json to save is NULL`() {
        setupForWritingFiles()
        setupForReadingFiles()
        every { tableTemplateGroupVmRepoHelper.getAbsoluteFile(path, templateName) } returns mockk()
        every { tableTemplateGroupVmRepoHelper.getMainFile(templateName) } returns mockk()
        every { tableTemplateGroupVmRepoHelper.getDirectoryFile(path) } returns mockk()
        every { tableTemplateGroupVmRepoHelper.getFileWriter(path, templateName) } returns mockk()
        every { tableTemplateRepository.getItem(templateName) } returns null

        viewModel.save(templateName)

        assert(!viewModel.status.value.success)
        assert(viewModel.status.value.message == JSON_ITEM_TO_SAVE_IS_NULL + templateName)
    }

    @Test
    fun `save template FAILURE WHEN IO file THROWS RuntimeException due to INVALID absolute file path`() {
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
    fun `save template FAILURE WHEN IO files are VALID BUT save to repository RETURNS FALSE`() {
        setupForReadingFiles()
        setupForWritingFiles()
        every { tableTemplateGroupVmRepoHelper.getAbsoluteFile(path, templateName) } returns mockk()
        every { tableTemplateGroupVmRepoHelper.getMainFile(templateName) } returns mockk()
        every { tableTemplateGroupVmRepoHelper.getDirectoryFile(path) } returns mockk()
        every { tableTemplateGroupVmRepoHelper.getFileWriter(path, templateName) } returns mockk()
        every { tableTemplateRepository.getItem(templateName) } returns dummyData
        every { tableTemplateRepository.getErrorMsg() } returns "An error occurred!"
        coEvery { tableTemplateRepository.save(templateName, dummyData) } returns false

        viewModel.save(templateName)

        assert(!viewModel.status.value.success)
        assert(viewModel.status.value.message == tableTemplateRepository.getErrorMsg())
    }

    @Test
    fun `new template SUCCESS`() {
        viewModel.new()
        assert(viewModel.status.value.success)
        assert(viewModel.status.value.message == "")
        assert(viewModel.status.value.ex == null)
    }

    @Test
    fun `reset all items in view model SUCCESS`() {
        every { tableTemplateGroupVmChangesCache.reset(any()) } returns Unit
        every { tableTemplateGroupVmChangesCache.get(any()) } returns listOf()

        viewModel.reset()

        assert(viewModel.status.value.success)
        assert(viewModel.status.value.message == "")
        assert(viewModel.status.value.ex == null)
    }

    @Test
    fun `ADD page WHEN no pages EXIST`() {
        viewModel.clearItems()
        viewModel.addPage()
        assert(viewModel.status.value.success)
        assert(viewModel.status.value.message == "")
        assert(viewModel.status.value.ex == null)
        assert(viewModel.status.value.items.size == 1)
        assert(viewModel.status.value.currentIndex == 0)
    }

    @Test
    fun `ADD page WHEN pages EXIST`() {
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
    fun `REMOVE page WHEN no pages EXIST`() {
        viewModel.clearItems()
        viewModel.removePage()
        assert(viewModel.status.value.success)
        assert(viewModel.status.value.message == "")
        assert(viewModel.status.value.ex == null)
        assert(viewModel.status.value.items.isEmpty())
    }

    @Test
    fun `REMOVE page WHEN pages EXIST`() {
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
    fun `NEXT page WHEN no pages EXIST`() {
        viewModel.clearItems()
        viewModel.nextPage()
        assert(viewModel.status.value.success)
        assert(viewModel.status.value.message == "")
        assert(viewModel.status.value.ex == null)
        assert(viewModel.status.value.items.isEmpty())
        assert(viewModel.status.value.currentIndex == 0)
    }

    @Test
    fun `NEXT page WHEN only one page EXISTS`() {
        viewModel.clearItems()
        viewModel.setIndex(0)
        viewModel.nextPage()
        assert(viewModel.status.value.success)
        assert(viewModel.status.value.message == "")
        assert(viewModel.status.value.ex == null)
        assert(viewModel.status.value.items.isEmpty())
        assert(viewModel.status.value.currentIndex == 0)
        assert(viewModel.getIndex() == 0)
    }

    @Test
    fun `NEXT page WHEN pages EXIST`() {
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
    fun `PREVIOUS page WHEN no pages EXIST`() {
        viewModel.clearItems()
        viewModel.previousPage()
        assert(viewModel.status.value.success)
        assert(viewModel.status.value.message == "")
        assert(viewModel.status.value.ex == null)
        assert(viewModel.status.value.items.isEmpty())
        assert(viewModel.status.value.currentIndex == 0)
    }

    @Test
    fun `PREVIOUS page WHEN pages EXIST`() {
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
    fun `set row name WHEN name is EMPTY`() {
        viewModel.clearItems()
        viewModel.setRowName("")
        assert(viewModel.rowNameEmpty.value)
        assert(!viewModel.duplicateName.value)
    }

    @Test
    fun `set row name WHEN name IS NOT EMPTY AND no duplicates`() {
        viewModel.clearItems()
        viewModel.addItem(TableItemFinal("test1", isPrimary = true, isSortKey = true,
            value = "test", editable = true, dataType = ItemType.STRING))
        viewModel.setRowName("test2")
        assert(!viewModel.rowNameEmpty.value)
        assert(!viewModel.duplicateName.value)
    }

    @Test
    fun `set row name WHEN name IS NOT EMPTY AND is duplicate`() {
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
    fun `set default value WHEN row IS EDITABLE`() {
        viewModel.clearItems()
        viewModel.addItem(TableItemFinal("test1", isPrimary = true, isSortKey = true,
            value = "test", editable = true, dataType = ItemType.STRING))
        viewModel.setDefaultValue("test")
        val item = viewModel.getCurrentPage()
        assert(item.value == "test")
        assert(!viewModel.noValueWithNotEditable.value)
    }

    @Test
    fun `set DEFAULT value WHEN row IS NOT EDITABLE`() {
        viewModel.clearItems()
        viewModel.addItem(TableItemFinal("test1", isPrimary = true, isSortKey = true,
            value = "test", editable = false, dataType = ItemType.STRING))
        viewModel.setDefaultValue("test")
        val item = viewModel.getCurrentPage()
        assert(item.value == "test")
        assert(!viewModel.noValueWithNotEditable.value)
    }

    @Test
    fun `set NO DEFAULT value WHEN row IS EDITABLE`() {
        viewModel.clearItems()
        viewModel.addItem(TableItemFinal("test1", isPrimary = true, isSortKey = true,
            value = "test", editable = false, dataType = ItemType.STRING))
        viewModel.setDefaultValue("")
        val item = viewModel.getCurrentPage()
        assert(item.value == "")
        assert(viewModel.noValueWithNotEditable.value)
    }

    @Test
    fun `set IS EDITABLE WITH NO DEFAULT value REQUIRED`() {
        viewModel.clearItems()
        viewModel.addItem(TableItemFinal("test1", isPrimary = true, isSortKey = true,
            value = "test", editable = false, dataType = ItemType.STRING))
        viewModel.setIsEditable(true)
        val item = viewModel.getCurrentPage()
        assert(item.editable)
        assert(!viewModel.noValueWithNotEditable.value)
    }

    @Test
    fun `set IS EDITABLE WITH DEFAULT value EMPTY`() {
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
    fun `set IS EDITABLE WITH DEFAULT value REQUIRED`() {
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
    fun `set IS EDITABLE WITH DEFAULT value PRESENT`() {
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
    fun `set IS PRIMARY`() {
        viewModel.clearItems()
        viewModel.addItem(TableItemFinal("test1", isPrimary = false, isSortKey = true,
            value = "test", editable = true, dataType = ItemType.STRING))
        viewModel.setPrimary(true)
        val item = viewModel.getCurrentPage()
        assert(item.isPrimary)
        assert(!viewModel.noPrimaryKeyFound.value)
    }

    @Test
    fun `set IS NOT PRIMARY AND no other rows set AS PRIMARY`() {
        viewModel.clearItems()
        viewModel.addItem(TableItemFinal("test1", isPrimary = false, isSortKey = true,
            value = "test", editable = true, dataType = ItemType.STRING))
        viewModel.setPrimary(false)
        val item = viewModel.getCurrentPage()
        assert(!item.isPrimary)
        assert(viewModel.noPrimaryKeyFound.value)
    }

    @Test
    fun `set IS NOT PRIMARY with OTHER row set AS PRIMARY`() {
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
    fun `set IS SORT KEY`() {
        viewModel.clearItems()
        viewModel.addItem(TableItemFinal("test1", isPrimary = true, isSortKey = false,
            value = "test", editable = true, dataType = ItemType.STRING))
        viewModel.setSortKey(true)
        val item = viewModel.getCurrentPage()
        assert(item.isSortKey)
        assert(!viewModel.noSortKeyFound.value)
    }

    @Test
    fun `set IS NOT SORT KEY WITH no other rows set as SORT KEY`() {
        viewModel.clearItems()
        viewModel.addItem(TableItemFinal("test1", isPrimary = true, isSortKey = true,
            value = "test", editable = true, dataType = ItemType.STRING))
        viewModel.setSortKey(false)
        val item = viewModel.getCurrentPage()
        assert(!item.isSortKey)
        assert(viewModel.noSortKeyFound.value)
    }

    @Test
    fun `set IS NOT SORT KEY WITH other row set as SORT KEY`() {
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
    fun `PAGE COUNT WHEN pages added`() {
        viewModel.clearItems()
        viewModel.addPage()
        viewModel.addPage()
        assert(viewModel.pageCount() == 2)
    }

    @Test
    fun `NEW table template SUCCESS`() {
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
        every { tableTemplateRepository.setItem(templateName, any()) } returns Unit
    }
}