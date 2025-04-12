package com.jonpeps.gamescms.viewmodels

import com.jonpeps.gamescms.data.dataclasses.TableItem
import com.jonpeps.gamescms.data.dataclasses.TableItemList
import com.jonpeps.gamescms.ui.createtable.viewmodels.TableTemplateGroupViewModel
import com.jonpeps.gamescms.ui.tabletemplates.repositories.ITableTemplateRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mockConstruction
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
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
    private lateinit var fileReader: FileReader
    @Mock
    private lateinit var tableTemplateRepository: ITableTemplateRepository

    private val dummyData = TableItemList("test", listOf(TableItem(1, "test1")))

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
}