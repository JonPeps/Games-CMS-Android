package com.jonpeps.gamescms.createtable.viewmodels

import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateItemListMoshi
import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateItemMoshi
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
    private lateinit var directoryFile: File
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

    private val path = "test/"
    private val templateName = "my_template"

    @Before
    fun setup() {
        val file = File.createTempFile(path, "")
        file.deleteOnExit()

        viewModel = TableTemplateGroupViewModel(path, tableTemplateRepository, dispatcher)
    }

    @Test
    fun `test load template success all paths`() = runTest(dispatcher) {
        `when`(tableTemplateRepository.load()).thenReturn(true)
        viewModel.load(templateName)
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
}