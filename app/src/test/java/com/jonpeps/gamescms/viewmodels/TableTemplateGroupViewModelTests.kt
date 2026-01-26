package com.jonpeps.gamescms.viewmodels

import com.jonpeps.gamescms.data.dataclasses.ItemType
import com.jonpeps.gamescms.data.dataclasses.TableItemFinal
import com.jonpeps.gamescms.data.dataclasses.mappers.TableItemFinalMapper
import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateItemListMoshi
import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateItemMoshi
import com.jonpeps.gamescms.data.helpers.ITableTemplateGroupValidator
import com.jonpeps.gamescms.data.repositories.IMoshiTableTemplateRepository
import com.jonpeps.gamescms.data.serialization.ICommonSerializationRepoHelper
import com.jonpeps.gamescms.ui.tabletemplates.serialization.ISerializeTableTemplateHelpers
import com.jonpeps.gamescms.ui.tabletemplates.viewmodels.TableTemplateGroupViewModel
import com.jonpeps.gamescms.ui.tabletemplates.viewmodels.TableTemplateGroupViewModel.Companion.JSON_ITEM_TO_SAVE_IS_NULL
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
    private lateinit var tableTemplateRepository: IMoshiTableTemplateRepository
    @MockK
    private lateinit var tableTemplateGroupVmRepoHelper: ICommonSerializationRepoHelper
    @MockK
    private lateinit var tableTemplateGroupVmChangesCache: ITableTemplateGroupVmChangesCache
    @MockK
    private lateinit var tableTemplateGroupValidator: ITableTemplateGroupValidator
    @MockK
    private lateinit var serializeTableTemplateHelpers: ISerializeTableTemplateHelpers

    private val dummyData = TableTemplateItemListMoshi("test_template",
        listOf(TableTemplateItemMoshi("test",
        dataType = ItemType.STRING)))

    private lateinit var sut: TableTemplateGroupViewModel

    private val path = "test/"
    private val templateName = "my_template"

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        sut = TableTemplateGroupViewModel(path,
            tableTemplateRepository,
            tableTemplateGroupVmRepoHelper,
            tableTemplateGroupVmChangesCache,
            tableTemplateGroupValidator,
            serializeTableTemplateHelpers,
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
        every { tableTemplateRepository.getItem() } returns dummyData
        coEvery { tableTemplateRepository.load() } returns true

        sut.load(templateName, false)

        assert(sut.status.value.success)
        assert(sut.status.value.message == "")
        assert(sut.status.value.ex == null)
        assert(sut.status.value.items.size == 1)
        assert(sut.status.value.currentIndex == 0)
    }

    @Test
    fun `load template FAILURE WHEN IO files are VALID AND load from repository RETURNS false`() {
        setupForReadingFiles()
        every { tableTemplateGroupVmRepoHelper.getAbsoluteFile(path, templateName) } returns mockk()
        every { tableTemplateGroupVmRepoHelper.getBufferReader(path, templateName) } returns mockk()
        every { tableTemplateGroupVmChangesCache.isPopulated() } returns false
        every { tableTemplateRepository.getErrorMsg() } returns "An error occurred!"
        coEvery { tableTemplateRepository.load() } returns false

        sut.load(templateName)

        assert(!sut.status.value.success)
        assert(sut.status.value.message == tableTemplateRepository.getErrorMsg())
        assert(sut.status.value.ex == null)
    }

    @Test
    fun `load template FAILURE WHEN IO files THROW RuntimeException due to INVALID File`() {
        every { tableTemplateGroupVmRepoHelper.getAbsoluteFile(any(), any()) } throws RuntimeException("Runtime error!")
        every { tableTemplateGroupVmChangesCache.isPopulated() } returns false

        sut.load(templateName)

        assert(!sut.status.value.success)
        assert(sut.status.value.message == "Runtime error!")
        assert(sut.status.value.ex != null)
    }

    @Test
    fun `load template FAILURE WHEN IO files THROW RuntimeException due to INVALID BufferReader`() {
        setupForReadingFiles()
        setupForWritingFiles()
        every { tableTemplateGroupVmRepoHelper.getAbsoluteFile(any(), any()) } returns mockk()
        every { tableTemplateGroupVmRepoHelper.getBufferReader(any(), any()) } throws RuntimeException("Runtime error!")
        every { tableTemplateGroupVmChangesCache.isPopulated() } returns false

        sut.load(templateName)

        assert(!sut.status.value.success)
        assert(sut.status.value.message == "Runtime error!")
        assert(sut.status.value.ex != null)
    }

    @Test
    fun `load template FAILURE WHEN IO files are correct BUT Json item is NULL`() {
        setupForReadingFiles()
        every { tableTemplateGroupVmRepoHelper.getAbsoluteFile(path, templateName) } returns mockk()
        every { tableTemplateGroupVmRepoHelper.getBufferReader(path, templateName) } returns mockk()
        coEvery { tableTemplateRepository.load() } returns true
        every { tableTemplateRepository.getItem() } returns null

        sut.load(templateName, false)

        assert(!sut.status.value.success)
        assert(sut.status.value.message == JSON_ITEM_TO_LOAD_IS_NULL + templateName)
        assert(sut.status.value.ex == null)
    }

    @Test
    fun `load template from cache SUCCESS`() {
        every { tableTemplateGroupVmChangesCache.isPopulated() } returns true
        every { tableTemplateGroupVmChangesCache.get(templateName) } returns listOf(
            TableItemFinal("test", isPrimary = true, isSortKey = true,
                value = "test", editable = true, dataType = ItemType.STRING))

        sut.load(templateName, true)

        assert(sut.status.value.success)
        assert(sut.status.value.message == "")
        assert(sut.status.value.ex == null)
        assert(sut.status.value.items.size == 1)
        assert(sut.status.value.currentIndex == 0)
    }

    @Test
    fun `save template SUCCESS WHEN IO files are VALID and save to repository RETURNS TRUE`() {
        setupForReadingFiles()
        setupForWritingFiles()
        every { tableTemplateGroupVmRepoHelper.getAbsoluteFile(path, templateName) } returns mockk()
        every { tableTemplateGroupVmRepoHelper.getMainFile(templateName) } returns mockk()
        every { tableTemplateGroupVmRepoHelper.getDirectoryFile(path) } returns mockk()
        every { tableTemplateGroupVmRepoHelper.getFileWriter(path, templateName) } returns mockk()
        every { tableTemplateRepository.getItem() } returns dummyData
        coEvery { tableTemplateRepository.save(dummyData) } returns true

        sut.save(templateName)

        assert(sut.status.value.success)
        assert(sut.status.value.message == "")
        assert(sut.status.value.ex == null)
    }

    @Test
    fun `save template FAILURE WHEN IO files are VALID and Json to save is NULL`() {
        setupForWritingFiles()
        setupForReadingFiles()
        every { tableTemplateGroupVmRepoHelper.getAbsoluteFile(path, templateName) } returns mockk()
        every { tableTemplateGroupVmRepoHelper.getMainFile(templateName) } returns mockk()
        every { tableTemplateGroupVmRepoHelper.getDirectoryFile(path) } returns mockk()
        every { tableTemplateGroupVmRepoHelper.getFileWriter(path, templateName) } returns mockk()
        every { tableTemplateRepository.getItem() } returns null

        sut.save(templateName)

        assert(!sut.status.value.success)
        assert(sut.status.value.message == JSON_ITEM_TO_SAVE_IS_NULL + templateName)
    }

    @Test
    fun `save template FAILURE WHEN IO file THROWS RuntimeException due to INVALID absolute file path`() {
        every { tableTemplateGroupVmRepoHelper.getAbsoluteFile(path, templateName) } throws RuntimeException("Runtime error!")

        sut.save(templateName)

        assert(!sut.status.value.success)
        assert(sut.status.value.message == "Runtime error!")
        assert(sut.status.value.ex != null)
    }

    @Test
    fun `test save template fails when IO file throws RuntimeException due to invalid FileWriter`() {
        setupForReadingFiles()
        setupForWritingFiles()
        every { tableTemplateGroupVmRepoHelper.getAbsoluteFile(path, templateName) } returns mockk()
        every { tableTemplateGroupVmRepoHelper.getMainFile(templateName) } returns mockk()
        every { tableTemplateGroupVmRepoHelper.getDirectoryFile(path) } returns mockk()
        every { tableTemplateGroupVmRepoHelper.getFileWriter(path, templateName) } throws RuntimeException("Runtime error!")

        sut.save(templateName)

        assert(!sut.status.value.success)
        assert(sut.status.value.message == "Runtime error!")
        assert(sut.status.value.ex != null)
    }

    @Test
    fun `save template FAILURE WHEN IO files are VALID BUT save to repository RETURNS FALSE`() {
        setupForReadingFiles()
        setupForWritingFiles()
        every { tableTemplateGroupVmRepoHelper.getAbsoluteFile(path, templateName) } returns mockk()
        every { tableTemplateGroupVmRepoHelper.getMainFile(templateName) } returns mockk()
        every { tableTemplateGroupVmRepoHelper.getDirectoryFile(path) } returns mockk()
        every { tableTemplateGroupVmRepoHelper.getFileWriter(path, templateName) } returns mockk()
        every { tableTemplateRepository.getItem() } returns dummyData
        every { tableTemplateRepository.getErrorMsg() } returns "An error occurred!"
        coEvery { tableTemplateRepository.save(dummyData) } returns false

        sut.save(templateName)

        assert(!sut.status.value.success)
        assert(sut.status.value.message == tableTemplateRepository.getErrorMsg())
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
        every { tableTemplateGroupVmChangesCache.reset(any()) } returns Unit
        every { tableTemplateGroupVmChangesCache.get(any()) } returns listOf()

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
    fun `set row name SUCCESS WHEN ROW IS NOT EMPTY OR DUPLICATE`() {
        every { tableTemplateGroupValidator.validateNameIsNotEmpty(any()) } returns true
        every { tableTemplateGroupValidator.validateNameIsNotDuplicate(any(), any()) } returns true

        sut.clearItems()
        sut.addItem(TableItemFinal("test1", isPrimary = true, isSortKey = true,
            value = "test", editable = true, dataType = ItemType.BOOLEAN))
        sut.setRowName("test2")
        val item = sut.getCurrentPage()
        assert(item.name == "test2")
        assert(sut.isNotDuplicateName.value)
        assert(sut.rowNameIsNotEmpty.value)
    }

    @Test
    fun `set row name FAILURE WHEN ROW IS EMPTY`() {
        every { tableTemplateGroupValidator.validateNameIsNotEmpty(any()) } returns false
        every { tableTemplateGroupValidator.validateNameIsNotDuplicate(any(), any()) } returns true

        sut.clearItems()
        sut.addItem(TableItemFinal("test1", isPrimary = true, isSortKey = true,
            value = "test", editable = true, dataType = ItemType.BOOLEAN))
        sut.setRowName("")
        val item = sut.getCurrentPage()
        assert(item.name == "")
        assert(sut.isNotDuplicateName.value)
        assert(!sut.rowNameIsNotEmpty.value)
    }

    @Test
    fun `set row name FAILURE WHEN ROW IS DUPLICATE`() {
        every { tableTemplateGroupValidator.validateNameIsNotEmpty(any()) } returns true
        every { tableTemplateGroupValidator.validateNameIsNotDuplicate(any(), any()) } returns false

        sut.clearItems()
        sut.addItem(TableItemFinal("test1", isPrimary = true, isSortKey = true,
            value = "test", editable = true, dataType = ItemType.BOOLEAN))
        sut.new()
        sut.setRowName("test1")
        val item = sut.getCurrentPage()
        assert(item.name == "test1")
        assert(!sut.isNotDuplicateName.value)
        assert(sut.rowNameIsNotEmpty.value)
    }

    @Test
    fun `set row data type`() {
        every { serializeTableTemplateHelpers.validateTableTemplateValue(any(), any()) } returns true

        sut.clearItems()
        sut.addItem(TableItemFinal("test1", isPrimary = true, isSortKey = true,
            value = "test", editable = true, dataType = ItemType.BOOLEAN))
        var item = sut.getCurrentPage()
        assert(item.dataType == ItemType.BOOLEAN)
        sut.setItemType(ItemType.STRING)
        item = sut.getCurrentPage()
        assert(item.dataType == ItemType.STRING)
    }

    @Test
    fun `set default value WHEN row IS EDITABLE`() {
        every { serializeTableTemplateHelpers.validateTableTemplateValue(any(), any()) } returns true

        sut.clearItems()
        sut.addItem(TableItemFinal("test1", isPrimary = true, isSortKey = true,
            value = "test", editable = true, dataType = ItemType.STRING))
        sut.setDefaultValue("test")
        val item = sut.getCurrentPage()
        assert(item.value == "test")
        assert(!sut.noValueWithNotEditable.value)
    }

    @Test
    fun `set DEFAULT value WHEN row IS NOT EDITABLE`() {
        every { serializeTableTemplateHelpers.validateTableTemplateValue(any(), any()) } returns true

        sut.clearItems()
        sut.addItem(TableItemFinal("test1", isPrimary = true, isSortKey = true,
            value = "test", editable = false, dataType = ItemType.STRING))
        sut.setDefaultValue("test")
        val item = sut.getCurrentPage()
        assert(item.value == "test")
        assert(!sut.noValueWithNotEditable.value)
    }

    @Test
    fun `set NO DEFAULT value WHEN row IS EDITABLE`() {
        every { serializeTableTemplateHelpers.validateTableTemplateValue(any(), any()) } returns false

        sut.clearItems()
        sut.addItem(TableItemFinal("test1", isPrimary = true, isSortKey = true,
            value = "test", editable = false, dataType = ItemType.STRING))
        sut.setDefaultValue("")
        val item = sut.getCurrentPage()
        assert(item.value == "")
        assert(sut.noValueWithNotEditable.value)
    }

    @Test
    fun `set IS EDITABLE WITH NO DEFAULT value REQUIRED`() {
        every { serializeTableTemplateHelpers.validateTableTemplateValue(any(), any()) } returns true

        sut.clearItems()
        sut.addItem(TableItemFinal("test1", isPrimary = true, isSortKey = true,
            value = "test", editable = false, dataType = ItemType.STRING))
        sut.setIsEditable(true)
        val item = sut.getCurrentPage()
        assert(item.editable)
        assert(!sut.noValueWithNotEditable.value)
    }

    @Test
    fun `set IS EDITABLE WITH DEFAULT value EMPTY`() {
        every { serializeTableTemplateHelpers.validateTableTemplateValue(any(), any()) } returns true

        sut.clearItems()
        sut.addItem(TableItemFinal("test1", isPrimary = true, isSortKey = true,
            value = "test", editable = false, dataType = ItemType.STRING))
        sut.setDefaultValue("")
        sut.setIsEditable(false)
        sut.setIndex(0)
        val item = sut.getCurrentPage()
        assert(!item.editable)
        assert(sut.noValueWithNotEditable.value)
    }

    @Test
    fun `set IS EDITABLE WITH DEFAULT value REQUIRED`() {
        every { serializeTableTemplateHelpers.validateTableTemplateValue(any(), any()) } returns false

        sut.clearItems()
        sut.addItem(TableItemFinal("test1", isPrimary = true, isSortKey = true,
            value = "test", editable = true, dataType = ItemType.STRING))
        sut.setDefaultValue("")
        sut.setIsEditable(false)
        val item = sut.getCurrentPage()
        assert(!item.editable)
        assert(sut.noValueWithNotEditable.value)
    }

    @Test
    fun `set IS EDITABLE WITH DEFAULT value PRESENT`() {
        every { serializeTableTemplateHelpers.validateTableTemplateValue(any(), any()) } returns true

        sut.clearItems()
        sut.addItem(TableItemFinal("test1", isPrimary = true, isSortKey = true,
            value = "test", editable = false, dataType = ItemType.STRING))
        sut.setDefaultValue("test value")
        sut.setIsEditable(true)
        val item = sut.getCurrentPage()
        assert(item.editable)
        assert(!sut.noValueWithNotEditable.value)
    }

    @Test
    fun `set IS PRIMARY`() {
        sut.clearItems()
        sut.addItem(TableItemFinal("test1", isPrimary = false, isSortKey = true,
            value = "test", editable = true, dataType = ItemType.STRING))
        sut.setPrimary(true)
        val item = sut.getCurrentPage()
        assert(item.isPrimary)
        assert(!sut.noPrimaryKeyFound.value)
    }

    @Test
    fun `set IS NOT PRIMARY AND no other rows set AS PRIMARY`() {
        sut.clearItems()
        sut.addItem(TableItemFinal("test1", isPrimary = false, isSortKey = true,
            value = "test", editable = true, dataType = ItemType.STRING))
        sut.setPrimary(false)
        val item = sut.getCurrentPage()
        assert(!item.isPrimary)
        assert(sut.noPrimaryKeyFound.value)
    }

    @Test
    fun `set IS NOT PRIMARY with OTHER row set AS PRIMARY`() {
        sut.clearItems()
        sut.addItem(TableItemFinal("test1", isPrimary = true, isSortKey = true,
            value = "test", editable = true, dataType = ItemType.STRING))
        sut.addItem(TableItemFinal("test2", isPrimary = true, isSortKey = true,
            value = "test", editable = true, dataType = ItemType.STRING))
        sut.setIndex(0)
        sut.setPrimary(false)
        assert(!sut.noPrimaryKeyFound.value)
    }

    @Test
    fun `set IS SORT KEY`() {
        sut.clearItems()
        sut.addItem(TableItemFinal("test1", isPrimary = true, isSortKey = false,
            value = "test", editable = true, dataType = ItemType.STRING))
        sut.setSortKey(true)
        val item = sut.getCurrentPage()
        assert(item.isSortKey)
        assert(!sut.noSortKeyFound.value)
    }

    @Test
    fun `set IS NOT SORT KEY WITH no other rows set as SORT KEY`() {
        sut.clearItems()
        sut.addItem(TableItemFinal("test1", isPrimary = true, isSortKey = true,
            value = "test", editable = true, dataType = ItemType.STRING))
        sut.setSortKey(false)
        val item = sut.getCurrentPage()
        assert(!item.isSortKey)
        assert(sut.noSortKeyFound.value)
    }

    @Test
    fun `set IS NOT SORT KEY WITH other row set as SORT KEY`() {
        sut.clearItems()
        sut.addItem(TableItemFinal("test1", isPrimary = true, isSortKey = false,
            value = "test", editable = true, dataType = ItemType.STRING))
        sut.addItem(TableItemFinal("test2", isPrimary = false, isSortKey = true,
            value = "test", editable = true, dataType = ItemType.STRING))
        sut.setIndex(0)
        sut.setSortKey(false)
        assert(!sut.noSortKeyFound.value)
    }

    @Test
    fun `test determineIfParseValueError with NO PARSE ERROR`() {
        every { serializeTableTemplateHelpers.validateTableTemplateValue(any(), any()) } returns true
        sut.clearItems()
        sut.addItem(TableItemFinal("test1", isPrimary = true, isSortKey = false,
            value = "true", editable = true, dataType = ItemType.BOOLEAN))
        sut.setIndex(0)
        sut.determineIfParseValueError(ItemType.BOOLEAN)
        assert(!sut.parseValueError.value)
        assert(sut.getParseErrorMsg() == "")
    }

    @Test
    fun `test determineIfParseValueError with PARSE ERROR`() {
        every { serializeTableTemplateHelpers.validateTableTemplateValue(any(), any()) } returns false
        sut.clearItems()
        sut.addItem(TableItemFinal("test1", isPrimary = true, isSortKey = false,
            value = "Not a boolean", editable = true, dataType = ItemType.BOOLEAN))
        sut.setIndex(0)
        sut.determineIfParseValueError(ItemType.BOOLEAN)
        assert(sut.parseValueError.value)
        assert(sut.getParseErrorMsg() == "Value is not a Boolean")
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

    private fun setupForReadingFiles() {
        every { tableTemplateRepository.setAbsoluteFile(any()) } returns Unit
        every { tableTemplateRepository.setBufferReader(any()) } returns Unit
    }

    private fun setupForWritingFiles() {
        every { tableTemplateRepository.assignDirectoryFile(any()) } returns Unit
        every { tableTemplateRepository.setFileWriter(any()) } returns Unit
        every { tableTemplateRepository.setFile(any()) } returns Unit
        every { tableTemplateRepository.setItem(any()) } returns Unit
    }
}