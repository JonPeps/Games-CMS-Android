package com.jonpeps.gamescms.viewmodels

import com.jonpeps.gamescms.data.dataclasses.moshi.StringListMoshi
import com.jonpeps.gamescms.data.repositories.IMoshiStringListRepository
import com.jonpeps.gamescms.data.serialization.ICommonDeleteFileHelper
import com.jonpeps.gamescms.data.serialization.ICommonSerializationRepoHelper
import com.jonpeps.gamescms.data.viewmodels.CommonStringListViewModel
import com.jonpeps.gamescms.data.viewmodels.CommonStringListViewModel.Companion.FAILED_TO_LOAD_FILE
import com.jonpeps.gamescms.data.viewmodels.CommonStringListViewModel.Companion.FAILED_TO_SAVE_FILE
import com.jonpeps.gamescms.ui.tabletemplates.viewmodels.IStringListItemsVmChangesCache
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CommonStringListViewModelTests {
    private val dispatcher = UnconfinedTestDispatcher()
    @MockK
    private lateinit var moshiStringListRepository: IMoshiStringListRepository
    @MockK
    private lateinit var commonSerializationRepoHelper: ICommonSerializationRepoHelper
    @MockK
    private lateinit var listItemsVmChangesCache: IStringListItemsVmChangesCache
    @MockK
    private lateinit var commonDeleteFileHelper: ICommonDeleteFileHelper

    private lateinit var viewModel: CommonStringListViewModel

    private val dummyListData = arrayListOf("item1", "item2")
    private val dummyData = StringListMoshi(dummyListData)

    private val filesListPath = "test1/"
    private val filesDirectoryPath = "test2/"
    private val cachedListName = "list"

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        viewModel = CommonStringListViewModel(
            filesDirectoryPath,
            filesListPath,
            moshiStringListRepository,
            commonSerializationRepoHelper,
            listItemsVmChangesCache,
            commonDeleteFileHelper,
            dispatcher
        )

        every { listItemsVmChangesCache.set(any(), any()) } returns Unit
    }

    @Test
    fun `load string list SUCCESS WHEN IO files are VALID and load from repository RETURNS TRUE`() {
        setupForReadingFiles()
        every { commonSerializationRepoHelper.getAbsoluteFile(any(), any()) } returns mockk()
        every { commonSerializationRepoHelper.getBufferReader(any(), any()) } returns mockk()

        every { listItemsVmChangesCache.isPopulated() } returns false
        coEvery { moshiStringListRepository.load(cachedListName) } returns true
        every { moshiStringListRepository.getItem(cachedListName) } returns dummyData
        every { moshiStringListRepository.getErrorMsg() } returns ""

        viewModel.load(cachedListName, false)

        verify { listItemsVmChangesCache.set(any(), any()) }

        assert(viewModel.status.value.success)
        assert(viewModel.status.value.message == "")
        assert(viewModel.status.value.ex == null)
        assert(viewModel.status.value.items.size == 2)
    }

    @Test
    fun `load string list SUCCESS WHEN IO files are VALID and load from repository RETURNS TRUE WHEN no cached items should be polled`() {
        setupForReadingFiles()
        every { commonSerializationRepoHelper.getAbsoluteFile(any(), any()) } returns mockk()
        every { commonSerializationRepoHelper.getBufferReader(any(), any()) } returns mockk()

        every { listItemsVmChangesCache.isPopulated() } returns false
        coEvery { moshiStringListRepository.load(cachedListName) } returns true
        every { moshiStringListRepository.getItem(cachedListName) } returns dummyData
        every { moshiStringListRepository.getErrorMsg() } returns ""

        viewModel.load(cachedListName, true)

        verify { listItemsVmChangesCache.set(any(), any()) }

        assert(viewModel.status.value.success)
        assert(viewModel.status.value.message == "")
        assert(viewModel.status.value.ex == null)
        assert(viewModel.status.value.items.size == 2)
    }

    @Test
    fun `load string list SUCCESS WHEN items are cached`() {
        every { listItemsVmChangesCache.isPopulated() } returns true
        every { listItemsVmChangesCache.get(cachedListName) } returns dummyListData
        coEvery { moshiStringListRepository.load(cachedListName) } returns true

        viewModel.load(cachedListName, true)

        assert(viewModel.status.value.success)
        assert(viewModel.status.value.message == "")
        assert(viewModel.status.value.ex == null)
        assert(viewModel.status.value.items.size == dummyListData.size)
    }

    @Test
    fun `load string list FAILS WHEN IO files are VALID and load from repository RETURNS FALSE`() {
        setupForReadingFiles()
        every { commonSerializationRepoHelper.getAbsoluteFile(any(), any()) } returns mockk()
        every { commonSerializationRepoHelper.getBufferReader(any(), any()) } returns mockk()

        every { listItemsVmChangesCache.isPopulated() } returns false
        coEvery { moshiStringListRepository.load(cachedListName) } returns false
        every { moshiStringListRepository.getItem(cachedListName) } returns dummyData
        every { moshiStringListRepository.getErrorMsg() } returns ""

        viewModel.load(cachedListName, false)

        assert(!viewModel.status.value.success)
        assert(viewModel.status.value.message == FAILED_TO_LOAD_FILE + filesListPath)
        assert(viewModel.status.value.ex == null)
        assert(viewModel.status.value.items.size == 0)
    }

    @Test
    fun `load string list FAILS WHEN IO files are VALID and load from repository RETURNS TRUE AND get item RETURNS NULL`() {
        setupForReadingFiles()
        every { commonSerializationRepoHelper.getAbsoluteFile(any(), any()) } returns mockk()
        every { commonSerializationRepoHelper.getBufferReader(any(), any()) } returns mockk()

        every { listItemsVmChangesCache.isPopulated() } returns false
        coEvery { moshiStringListRepository.load(cachedListName) } returns true
        every { moshiStringListRepository.getItem(cachedListName) } returns null
        every { moshiStringListRepository.getErrorMsg() } returns ""

        viewModel.load(cachedListName, false)

        assert(!viewModel.status.value.success)
        assert(viewModel.status.value.message == FAILED_TO_LOAD_FILE + filesListPath)
        assert(viewModel.status.value.ex == null)
        assert(viewModel.status.value.items.size == 0)
    }

    @Test
    fun `load string list FAILS WHEN IO files THROWS EXCEPTION` () {
        setupForReadingFiles()
        every { commonSerializationRepoHelper.getBufferReader(any(), any()) } returns mockk()
        every { commonSerializationRepoHelper.getAbsoluteFile(any(), any()) } throws Exception("Runtime error!")

        viewModel.load(cachedListName, false)

        assert(!viewModel.status.value.success)
        assert(viewModel.status.value.message == "Runtime error!")
        assert(viewModel.status.value.ex != null)
        assert(viewModel.status.value.items.size == 0)
    }



    private fun setupForReadingFiles() {
        every { moshiStringListRepository.setAbsoluteFile(any()) } returns Unit
        every { moshiStringListRepository.setBufferReader(any()) } returns Unit
    }

    private fun setupForWritingFiles() {
        every { moshiStringListRepository.setDirectoryFile(any()) } returns Unit
        every { moshiStringListRepository.setFileWriter(any()) } returns Unit
        every { moshiStringListRepository.setFile(any()) } returns Unit
        every { moshiStringListRepository.setItem(cachedListName, any()) } returns Unit
    }
}