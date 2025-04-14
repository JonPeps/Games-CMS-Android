package com.jonpeps.gamescms.viewmodels

import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateItemMoshi
import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateItemListMoshi
import com.jonpeps.gamescms.ui.createtable.viewmodels.TableTemplateGroupViewModel
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
import java.io.File
import java.io.FileWriter

@RunWith(MockitoJUnitRunner::class)
class TableTemplateGroupViewModelTests {
    @OptIn(ExperimentalCoroutinesApi::class)
    private val dispatcher = UnconfinedTestDispatcher()
    @Mock
    private lateinit var filePath: File
    @Mock
    private lateinit var file: File
    @Mock
    private lateinit var absolutePath: File
    @Mock
    private lateinit var bufferedReader: BufferedReader
    @Mock
    private lateinit var fileWriter: FileWriter
    @Mock
    private lateinit var tableTemplateRepository: ITableTemplateFileRepository

    private val dummyData = TableTemplateItemListMoshi("test_template", listOf(TableTemplateItemMoshi(1, "test1")))

    private lateinit var viewModel: TableTemplateGroupViewModel

    @Before
    fun setup() {
        viewModel = TableTemplateGroupViewModel(tableTemplateRepository, dispatcher)
        tableTemplateRepository.setFilePath(filePath)
        tableTemplateRepository.setFile(file)
        tableTemplateRepository.setAbsolutePath(absolutePath)
        tableTemplateRepository.setBufferReader(bufferedReader)
        tableTemplateRepository.setFileWriter(fileWriter)
    }

    @Test
    fun `test load template works all success paths`() = runTest(dispatcher) {
        `when`(tableTemplateRepository.load()).thenReturn(true)
        `when`(tableTemplateRepository.getItem()).thenReturn(dummyData)
        viewModel.load(absolutePath, bufferedReader)
        assert(viewModel.status.value.success)
        assert(viewModel.status.value.message == "")
        assert(viewModel.status.value.ex == null)
        assert(viewModel.status.value.items.size == 1)
        assert(viewModel.status.value.items[0].name == "test1")
        assert(viewModel.status.value.currentIndex == 0)
    }

    @Test
    fun `test load template fails all for all required success paths`() = runTest(dispatcher) {
        `when`(tableTemplateRepository.load()).thenReturn(false)
        `when`(tableTemplateRepository.getErrorMsg()).thenReturn("test")
        viewModel.load(absolutePath, bufferedReader)
        assert(!viewModel.status.value.success)
        assert(viewModel.status.value.message == "test")
        assert(viewModel.status.value.ex == null)
        assert(viewModel.status.value.items.size == 0)
        assert(viewModel.status.value.currentIndex == 0)
    }

    @Test
    fun `test load template fails due to null item`() = runTest(dispatcher) {
        `when`(tableTemplateRepository.load()).thenReturn(true)
        `when`(tableTemplateRepository.getItem()).thenReturn(null)
        `when`(absolutePath.name).thenReturn("test")
        viewModel.load(absolutePath, bufferedReader)
        assert(!viewModel.status.value.success)
        assert(viewModel.status.value.message == "Failed to load template: test")
        assert(viewModel.status.value.ex == null)
        assert(viewModel.status.value.items.size == 0)
        assert(viewModel.status.value.currentIndex == 0)
    }

    @Test
    fun `test save template success save to the repository returns true`() = runTest(dispatcher) {
        viewModel.clearItems()
        viewModel.addItem(TableTemplateItemMoshi(1, "test1"))
        viewModel.setTemplateName("test_template")
        `when`(tableTemplateRepository.save(dummyData)).thenReturn(true)
        viewModel.save(filePath, file, fileWriter)
        assert(viewModel.status.value.success)
        assert(viewModel.status.value.message == "")
        assert(viewModel.status.value.ex == null)
    }

    @Test
    fun `test save template fails due to save to the repository returns false`() = runTest(dispatcher) {
        viewModel.clearItems()
        viewModel.addItem(TableTemplateItemMoshi(1, "test1"))
        viewModel.setTemplateName("test_template")
        `when`(tableTemplateRepository.save(dummyData)).thenReturn(false)
        viewModel.save(filePath, file, fileWriter)
        assert(!viewModel.status.value.success)
        assert(viewModel.status.value.message == "Failed to save template: test_template")
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
    fun `test on name changed when name is empty`() {
        viewModel.onNameChanged("")
        assert(!viewModel.duplicateName.value)
    }

    @Test
    fun `test on name changed when name is not empty and duplicates`() {
        viewModel.clearItems()
        viewModel.addItem(TableTemplateItemMoshi(1, "test1"))
        viewModel.addItem(TableTemplateItemMoshi(2, "test2"))
        viewModel.onNameChanged("test1")
        assert(viewModel.duplicateName.value)
    }

    @Test
    fun `test on name changed when name is not empty and no duplicates`() {
        viewModel.clearItems()
        viewModel.addItem(TableTemplateItemMoshi(1, "test1"))
        viewModel.addItem(TableTemplateItemMoshi(2, "test2"))
        viewModel.onNameChanged("test3")
        assert(!viewModel.duplicateName.value)
    }

    @Test
    fun `test primary changed when no primary exists and current page is not primary`() {
        viewModel.clearItems()
        viewModel.isPrimaryChanged(false)
        assert(viewModel.noPrimaryKeyFound.value)
    }

    @Test
    fun `test primary changed when no primary exists and current page primary`() {
        viewModel.clearItems()
        viewModel.isPrimaryChanged(true)
        assert(!viewModel.noPrimaryKeyFound.value)
    }

    @Test
    fun `test primary changed when primary does not exist and current page is not primary`() {
        viewModel.clearItems()
        viewModel.addItem(TableTemplateItemMoshi(1, "test1", isPrimary = false))
        viewModel.addItem(TableTemplateItemMoshi(2, "test2", isPrimary = false))
        viewModel.isPrimaryChanged(false)
        assert(viewModel.noPrimaryKeyFound.value)
    }

    @Test
    fun `test primary changed when primary does not exist and current page is primary`() {
        viewModel.clearItems()
        viewModel.addItem(TableTemplateItemMoshi(1, "test1", isPrimary = false))
        viewModel.addItem(TableTemplateItemMoshi(2, "test2", isPrimary = true))
        viewModel.isPrimaryChanged(true)
        assert(!viewModel.noPrimaryKeyFound.value)
    }

    @Test
    fun `test primary changed when primary exists and current page is not primary`() {
        viewModel.clearItems()
        viewModel.addItem(TableTemplateItemMoshi(1, "test1", isPrimary = false))
        viewModel.addItem(TableTemplateItemMoshi(2, "test2", isPrimary = true))
        viewModel.isPrimaryChanged(false)
        assert(!viewModel.noPrimaryKeyFound.value)
    }

    @Test
    fun `test sort key changed when no sort key exists and current page is not sort key`() {
        viewModel.clearItems()
        viewModel.isSortKeyChanged(false)
        assert(viewModel.noSortKeyFound.value)
    }

    @Test
    fun `test sort key changed when no sort key exists and current page sort key`() {
        viewModel.clearItems()
        viewModel.isSortKeyChanged(true)
        assert(!viewModel.noSortKeyFound.value)
    }

    @Test
    fun `test sort key changed when sort key does not exist and current page is not sort key`() {
        viewModel.clearItems()
        viewModel.addItem(TableTemplateItemMoshi(1, "test1", isSortKey = false))
        viewModel.addItem(TableTemplateItemMoshi(2, "test2", isSortKey = false))
        viewModel.isSortKeyChanged(false)
        assert(viewModel.noSortKeyFound.value)
    }

    @Test
    fun `test sort key changed when primary does not exist and current page is sort key`() {
        viewModel.clearItems()
        viewModel.addItem(TableTemplateItemMoshi(1, "test1", isSortKey = false))
        viewModel.addItem(TableTemplateItemMoshi(2, "test2", isSortKey = true))
        viewModel.isPrimaryChanged(true)
        assert(!viewModel.noSortKeyFound.value)
    }

    @Test
    fun `test sort key changed when primary exists and current page is not sort key`() {
        viewModel.clearItems()
        viewModel.addItem(TableTemplateItemMoshi(1, "test1", isSortKey = false))
        viewModel.addItem(TableTemplateItemMoshi(2, "test2", isSortKey = true))
        viewModel.isSortKeyChanged(false)
        assert(!viewModel.noSortKeyFound.value)
    }
}