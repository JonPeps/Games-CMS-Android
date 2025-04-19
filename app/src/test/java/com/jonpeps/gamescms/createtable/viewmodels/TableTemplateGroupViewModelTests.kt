package com.jonpeps.gamescms.createtable.viewmodels

import com.jonpeps.gamescms.data.dataclasses.ItemType
import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateItemListMoshi
import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateItemMoshi
import com.jonpeps.gamescms.ui.createtable.helpers.ITableTemplateGroupVmRepoHelper
import com.jonpeps.gamescms.ui.createtable.viewmodels.TableTemplateGroupViewModel
import com.jonpeps.gamescms.ui.createtable.viewmodels.TableTemplateGroupViewModel.Companion.JSON_ITEM_TO_SAVE_IS_NULL
import com.jonpeps.gamescms.ui.tabletemplates.repositories.ITableTemplateFileRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import java.io.BufferedReader

@RunWith(MockitoJUnitRunner::class)
class TableTemplateGroupViewModelTests {
    @OptIn(ExperimentalCoroutinesApi::class)
    private val dispatcher = UnconfinedTestDispatcher()
    @Mock
    private lateinit var tableTemplateRepository: ITableTemplateFileRepository
    @Mock
    private lateinit var tableTemplateGroupVmRepoHelper: ITableTemplateGroupVmRepoHelper
    @Mock
    private lateinit var reader: BufferedReader

    private val dummyData = TableTemplateItemListMoshi("test_template", listOf(TableTemplateItemMoshi(1, "test1")))

    private lateinit var viewModel: TableTemplateGroupViewModel

    private val path = "test/"
    private val templateName = "my_template"

    @Before
    fun setup() {
        viewModel = TableTemplateGroupViewModel(path, tableTemplateRepository, tableTemplateGroupVmRepoHelper, dispatcher)
    }

    @Test
    fun `test load template success when IO files are correct and load from repo is true`() = runTest(dispatcher) {
        `when`(tableTemplateGroupVmRepoHelper.getBufferReader(path, templateName)).thenReturn(reader)
        `when`(tableTemplateRepository.load()).thenReturn(true)
        `when`(tableTemplateRepository.getItem()).thenReturn(dummyData)
        viewModel.load(templateName)
        assert(viewModel.status.value.success)
        assert(viewModel.status.value.message == "")
        assert(viewModel.status.value.ex == null)
        assert(viewModel.status.value.items.size == 1)
        assert(viewModel.status.value.currentIndex == 0)
    }

    @Test
    fun `test load template failure when IO files are correct and load from repo is false`() = runTest(dispatcher) {
        `when`(tableTemplateGroupVmRepoHelper.getBufferReader(path, templateName)).thenReturn(reader)
        `when`(tableTemplateRepository.load()).thenReturn(false)
        viewModel.load(templateName)
        assert(!viewModel.status.value.success)
        assert(viewModel.status.value.message == tableTemplateRepository.getErrorMsg())
        assert(viewModel.status.value.ex == null)
    }

    @Test
    fun `test load template failure when IO files are correct but load Json item fails`() = runTest(dispatcher) {
        `when`(tableTemplateGroupVmRepoHelper.getBufferReader(path, templateName)).thenReturn(reader)
        `when`(tableTemplateRepository.load()).thenReturn(false)
        viewModel.load(templateName)
        assert(!viewModel.status.value.success)
        assert(viewModel.status.value.message == tableTemplateRepository.getErrorMsg())
        assert(viewModel.status.value.ex == null)
        assert(viewModel.status.value.currentIndex == 0)
    }

    @Test
    fun `test load template failure when IO files throw RuntimeException due to invalid File`() = runTest(dispatcher) {
        `when`(
            tableTemplateGroupVmRepoHelper.getAbsoluteFile(
                path,
                templateName
            )
        ).thenThrow(RuntimeException("Runtime error!"))

        viewModel.load(templateName)
        assert(!viewModel.status.value.success)
        assert(viewModel.status.value.message == "Runtime error!")
        assert(viewModel.status.value.ex != null)
    }

    @Test
    fun `test load template failure when IO files throw RuntimeException due to invalid BufferReader`() = runTest(dispatcher) {
        `when`(
            tableTemplateGroupVmRepoHelper.getBufferReader(
                path,
                templateName
            )
        ).thenThrow(RuntimeException("Runtime error!"))

        viewModel.load(templateName)
        assert(!viewModel.status.value.success)
        assert(viewModel.status.value.message == "Runtime error!")
        assert(viewModel.status.value.ex != null)
    }

    @Test
    fun `test load template failure when IO files are correct but Json item is null`() = runTest(dispatcher) {
        `when`(tableTemplateRepository.load()).thenReturn(true)
        `when`(tableTemplateRepository.getItem()).thenReturn(null)
        viewModel.load(templateName)
        assert(!viewModel.status.value.success)
        assert(viewModel.status.value.message == TableTemplateGroupViewModel.JSON_ITEM_TO_SAVE_IS_NULL + templateName)
        assert(viewModel.status.value.ex == null)
    }

    @Test
    fun `test save template success when IO files are correct and save to repo is true`() = runTest(dispatcher) {
        tableTemplateRepository.setItem(dummyData)
        `when`(tableTemplateRepository.getItem()).thenReturn(dummyData)
        `when`(tableTemplateRepository.save(dummyData)).thenReturn(true)
        viewModel.save(templateName)
        assert(viewModel.status.value.success)
        assert(viewModel.status.value.message == "")
        assert(viewModel.status.value.ex == null)
    }

    @Test
    fun `test save template fails when IO files are correct and Json to save is null`() = runTest(dispatcher) {
        `when`(tableTemplateRepository.getItem()).thenReturn(null)
        viewModel.save(templateName)
        assert(!viewModel.status.value.success)
        assert(viewModel.status.value.message == JSON_ITEM_TO_SAVE_IS_NULL + templateName)
    }

    @Test
    fun `test save template fails when IO file throws RuntimeException due to invalid absolute file path`() = runTest(dispatcher) {
        `when`(tableTemplateGroupVmRepoHelper.getAbsoluteFile(path, templateName)).thenThrow(RuntimeException("Runtime error!"))
        viewModel.save(templateName)
        assert(!viewModel.status.value.success)
        assert(viewModel.status.value.message == "Runtime error!")
        assert(viewModel.status.value.ex != null)
    }

    @Test
    fun `test save template fails when IO file throws RuntimeException due to invalid FileWriter`() = runTest(dispatcher) {
        `when`(tableTemplateGroupVmRepoHelper.getFileWriter(path, templateName)).thenThrow(RuntimeException("Runtime error!"))
        viewModel.save(templateName)
        assert(!viewModel.status.value.success)
        assert(viewModel.status.value.message == "Runtime error!")
        assert(viewModel.status.value.ex != null)
    }

    @Test
    fun `test save template fails when IO files are correct and save to repo is false`() = runTest(dispatcher) {
        tableTemplateRepository.setItem(dummyData)
        `when`(tableTemplateRepository.getItem()).thenReturn(dummyData)
        `when`(tableTemplateRepository.save(dummyData)).thenReturn(false)
        viewModel.save(templateName)
        assert(!viewModel.status.value.success)
        assert(viewModel.status.value.message == tableTemplateRepository.getErrorMsg())
    }

    @Test
    fun `test save template fails when IO files are incorrect`() = runTest(dispatcher) {
        `when`(tableTemplateRepository.getItem()).thenReturn(dummyData)
        `when`(tableTemplateRepository.save(dummyData)).thenReturn(false)
        `when`(tableTemplateRepository.getErrorMsg()).thenReturn("File error!")
        viewModel.save(templateName)
        assert(!viewModel.status.value.success)
        assert(viewModel.status.value.message == "File error!")
        assert(viewModel.status.value.ex == null)
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
        viewModel.addItem(TableTemplateItemMoshi(1, "test1"))
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
        viewModel.addItem(TableTemplateItemMoshi(1, "test1"))
        viewModel.setRowName("test2")
        assert(!viewModel.rowNameEmpty.value)
        assert(!viewModel.duplicateName.value)
    }

    @Test
    fun `set row name when name is not empty and is duplicate`() {
        viewModel.clearItems()
        viewModel.addItem(TableTemplateItemMoshi(1, "test1"))
        viewModel.setRowName("test1")
        assert(!viewModel.rowNameEmpty.value)
        assert(viewModel.duplicateName.value)
    }

    @Test
    fun `set row data type`() {
        viewModel.clearItems()
        viewModel.addItem(TableTemplateItemMoshi(1, "test1", dataType = ItemType.BOOLEAN))
        var item = viewModel.getCurrentPage()
        assert(item.dataType == ItemType.BOOLEAN)
        viewModel.setItemType(ItemType.STRING)
        item = viewModel.getCurrentPage()
        assert(item.dataType == ItemType.STRING)
    }

    @Test
    fun `set default value when row is editable`() {
        viewModel.clearItems()
        viewModel.addItem(TableTemplateItemMoshi(1, "test1", editable = true))
        viewModel.setDefaultValue("test")
        val item = viewModel.getCurrentPage()
        assert(item.value == "test")
        assert(!viewModel.noValueWithNotEditable.value)
    }

    @Test
    fun `set default value when row is not editable`() {
        viewModel.clearItems()
        viewModel.addItem(TableTemplateItemMoshi(1, "test1", editable = false))
        viewModel.setDefaultValue("test")
        val item = viewModel.getCurrentPage()
        assert(item.value == "test")
        assert(!viewModel.noValueWithNotEditable.value)
    }

    @Test
    fun `set no default value when row is editable`() {
        viewModel.clearItems()
        viewModel.addItem(TableTemplateItemMoshi(1, "test1", editable = false))
        viewModel.setDefaultValue("")
        val item = viewModel.getCurrentPage()
        assert(item.value == "")
        assert(viewModel.noValueWithNotEditable.value)
    }

    @Test
    fun `set is editable with no default value required`() {
        viewModel.clearItems()
        viewModel.addItem(TableTemplateItemMoshi(1, "test1", editable = false))
        viewModel.setIsEditable(true)
        val item = viewModel.getCurrentPage()
        assert(item.editable)
        assert(!viewModel.noValueWithNotEditable.value)
    }

    @Test
    fun `set is editable with default value empty`() {
        viewModel.clearItems()
        viewModel.addItem(TableTemplateItemMoshi(1, "test1", editable = false))
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
        viewModel.addItem(TableTemplateItemMoshi(1, "test1", editable = true))
        viewModel.setDefaultValue("")
        viewModel.setIsEditable(false)
        val item = viewModel.getCurrentPage()
        assert(!item.editable)
        assert(viewModel.noValueWithNotEditable.value)
    }

    @Test
    fun `set is editable with default value present`() {
        viewModel.clearItems()
        viewModel.addItem(TableTemplateItemMoshi(1, "test1", editable = false))
        viewModel.setDefaultValue("test value")
        viewModel.setIsEditable(true)
        val item = viewModel.getCurrentPage()
        assert(item.editable)
        assert(!viewModel.noValueWithNotEditable.value)
    }

    @Test
    fun `set is primary`() {
        viewModel.clearItems()
        viewModel.addItem(TableTemplateItemMoshi(1, "test1", isPrimary = false))
        viewModel.setPrimary(true)
        val item = viewModel.getCurrentPage()
        assert(item.isPrimary)
        assert(!viewModel.noPrimaryKeyFound.value)
    }

    @Test
    fun `set is not primary with no other rows set as primary`() {
        viewModel.clearItems()
        viewModel.addItem(TableTemplateItemMoshi(1, "test1", isPrimary = false))
        viewModel.setPrimary(false)
        val item = viewModel.getCurrentPage()
        assert(!item.isPrimary)
        assert(viewModel.noPrimaryKeyFound.value)
    }

    @Test
    fun `set is not primary with other row set as primary`() {
        viewModel.clearItems()
        viewModel.addItem(TableTemplateItemMoshi(1, "test1", isPrimary = true))
        viewModel.addItem(TableTemplateItemMoshi(2, "test2", isPrimary = true))
        viewModel.setIndex(0)
        viewModel.setPrimary(false)
        assert(!viewModel.noPrimaryKeyFound.value)
    }

    @Test
    fun `set is sort key`() {
        viewModel.clearItems()
        viewModel.addItem(TableTemplateItemMoshi(1, "test1", isSortKey = false))
        viewModel.setSortKey(true)
        assert(!viewModel.noSortKeyFound.value)
    }

    @Test
    fun `set no sort key with no other rows set as sort key`() {
        viewModel.clearItems()
        viewModel.addItem(TableTemplateItemMoshi(1, "test1", isSortKey = false))
        viewModel.setSortKey(false)
        assert(viewModel.noSortKeyFound.value)
    }

    @Test
    fun `set no sort key with other row set as sort key`() {
        viewModel.addItem(TableTemplateItemMoshi(1, "test1", isSortKey = true))
        viewModel.addItem(TableTemplateItemMoshi(2, "test2", isSortKey = false))
        viewModel.setSortKey(false)
        assert(!viewModel.noSortKeyFound.value)
    }
}