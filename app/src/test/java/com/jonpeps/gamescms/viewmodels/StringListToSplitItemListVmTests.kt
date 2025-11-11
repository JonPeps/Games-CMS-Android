package com.jonpeps.gamescms.viewmodels

import com.jonpeps.gamescms.data.dataclasses.moshi.StringListMoshi
import com.jonpeps.gamescms.data.helpers.JsonStringFilenamePair
import com.jonpeps.gamescms.data.helpers.JsonStringListHelper
import com.jonpeps.gamescms.data.viewmodels.InputStreamStringListViewModel
import com.jonpeps.gamescms.ui.viewmodels.defaults.StringListToSplitItemListVm
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Before
import org.junit.Test
import java.io.InputStream

@OptIn(ExperimentalCoroutinesApi::class)
class StringListToSplitItemListVmTests {
    private val dispatcher = UnconfinedTestDispatcher()
    @MockK
    private lateinit var jsonStringListHelper: JsonStringListHelper
    @MockK
    private lateinit var inputStreamViewModel: InputStreamStringListViewModel
    @MockK
    private lateinit var inputStream: InputStream

    private lateinit var viewModel: StringListToSplitItemListVm


    private val directory = "directory"
    private val fileName = "fileName"

    private val listItems = arrayListOf("name1:name1.json",
        "name2:name2.json",
        "name3:name3.json")

    private val stringListItem = StringListMoshi(listItems)

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        viewModel = StringListToSplitItemListVm(
            inputStreamViewModel,
            jsonStringListHelper, dispatcher
        )

        coEvery { inputStreamViewModel.processSuspend() } returns Unit
    }

    @Test
    fun `SUCCESS with EQUAL list names AND file names`() {
        every { inputStreamViewModel.status.value.success } returns true
        every { inputStreamViewModel.status.value.item } returns stringListItem

        every { jsonStringListHelper.splitItem(stringListItem) } returns JsonStringFilenamePair(listItems, listItems)

        viewModel.load(directory, fileName, inputStream)

        assert(viewModel.status.value.success)
    }

    @Test
    fun `FAILURE with UNEQUAL list names AND file names`() {
        every { inputStreamViewModel.status.value.success } returns true
        every { inputStreamViewModel.status.value.item } returns stringListItem

        every { jsonStringListHelper.splitItem(stringListItem) } returns JsonStringFilenamePair(emptyList(), listItems)

        viewModel.load(directory, fileName, inputStream)

        assert(!viewModel.status.value.success)
        assert(viewModel.status.value.errorMessage == StringListToSplitItemListVm.ITEMS_NOT_EQUAL)
    }

    @Test
    fun `FAILURE with JSON ITEM is NULL loaded from InputStreamStringListViewModel view model`() {
        every { inputStreamViewModel.status.value.success } returns true
        every { inputStreamViewModel.status.value.item } returns null

        viewModel.load(directory, fileName, inputStream)

        assert(!viewModel.status.value.success)
        assert(viewModel.status.value.errorMessage == StringListToSplitItemListVm.FAILED_TO_LOAD_FILE)
    }

    @Test
    fun `FAILURE with InputStreamStringListViewModel view model load FAILS`() {
        every { inputStreamViewModel.status.value.success } returns false
        every { inputStreamViewModel.status.value.errorMessage } returns "error message"
        every { inputStreamViewModel.status.value.exception } returns Exception()

        viewModel.load(directory, fileName, inputStream)

        assert(!viewModel.status.value.success)
        assert(viewModel.status.value.errorMessage == "error message")
        assert(viewModel.status.value.exception != null)
    }
}