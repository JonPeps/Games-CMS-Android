package com.jonpeps.gamescms.viewmodels

import com.jonpeps.gamescms.data.dataclasses.moshi.StringListMoshi
import com.jonpeps.gamescms.data.repositories.IMoshiStringListRepository
import com.jonpeps.gamescms.data.serialization.ICommonSerializationRepoHelper
import com.jonpeps.gamescms.data.serialization.debug.IAssetSerializationRepoHelper
import com.jonpeps.gamescms.data.viewmodels.AssetsStringListViewModel
import com.jonpeps.gamescms.data.viewmodels.AssetsStringListViewModel.Companion.FAILED_TO_LOAD_FILE
import com.jonpeps.gamescms.data.viewmodels.AssetsStringListViewModel.Companion.FAILED_TO_WRITE_FILE
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
class AssetsStringListViewModelTests {
    private val dispatcher = UnconfinedTestDispatcher()
    @MockK
    private lateinit var moshiStringListRepository: IMoshiStringListRepository
    @MockK
    private lateinit var listItemsVmChangesCache: IStringListItemsVmChangesCache
    @MockK
    private lateinit var commonSerializationRepoHelper: ICommonSerializationRepoHelper
    @MockK
    private lateinit var assetSerializationRepoHelper: IAssetSerializationRepoHelper

    private lateinit var viewModel: AssetsStringListViewModel

    private val dummyListData = arrayListOf("item1", "item2")
    private val dummyData = StringListMoshi(dummyListData)

    private val filesListPath = "test1/"
    private val cachedListName = "list"

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        viewModel = AssetsStringListViewModel(
            filesListPath,
            commonSerializationRepoHelper,
            assetSerializationRepoHelper,
            moshiStringListRepository,
            listItemsVmChangesCache,
            dispatcher
        )

        every { listItemsVmChangesCache.set(any(), any()) } returns Unit
    }

    @Test
    fun `load string list SUCCESS when assets stream is VALID AND write contents to LOCAL STORAGE SUCCESS`() {
        setupForReadingFiles()
        setupForWritingFiles()
        coEvery { moshiStringListRepository.load(cachedListName) } returns true
        every { moshiStringListRepository.getItem(cachedListName) } returns dummyData
        every { moshiStringListRepository.getErrorMsg() } returns ""
        coEvery { moshiStringListRepository.save(cachedListName, dummyData) } returns true

        viewModel.loadFromAssets(cachedListName, mockk())

        verify { listItemsVmChangesCache.set(cachedListName, dummyListData) }
        assert(viewModel.status.value.success)
        assert(viewModel.status.value.message == "")
        assert(viewModel.status.value.ex == null)
        assert(viewModel.status.value.items.size == 2)
    }

    @Test
    fun `load string list FAILURE when LOAD REPOSITORY returns FALSE`() {
        setupForReadingFiles()
        coEvery { moshiStringListRepository.load(cachedListName) } returns false

        viewModel.loadFromAssets(cachedListName, mockk())

        assert(!viewModel.status.value.success)
        assert(viewModel.status.value.message == FAILED_TO_LOAD_FILE)
        assert(viewModel.status.value.ex == null)
        assert(viewModel.status.value.items.size == 0)
    }

    @Test
    fun `load string list FAILURE when LOAD REPOSITORY RETURNS TRUE but GET ITEM is NULL`() {
        setupForReadingFiles()
        coEvery { moshiStringListRepository.load(cachedListName) } returns true
        every { moshiStringListRepository.getItem(cachedListName) } returns null

        viewModel.loadFromAssets(cachedListName, mockk())

        assert(!viewModel.status.value.success)
        assert(viewModel.status.value.message == FAILED_TO_LOAD_FILE)
        assert(viewModel.status.value.ex == null)
        assert(viewModel.status.value.items.size == 0)
    }

    @Test
    fun `load string list FAILURE when LOAD REPOSITRY THROWS Exception`() {
        setupForReadingFiles()
        coEvery { moshiStringListRepository.load(cachedListName) } throws Exception()

        viewModel.loadFromAssets(cachedListName, mockk())

        assert(!viewModel.status.value.success)
        assert(viewModel.status.value.message != null)
        assert(viewModel.status.value.ex != null)
        assert(viewModel.status.value.items.size == 0)
    }

    @Test
    fun `load string list SUCCESS when assets stream is VALID AND write contents to LOCAL STORAGE FAILS`() {
        setupForReadingFiles()
        setupForWritingFiles()
        coEvery { moshiStringListRepository.load(cachedListName) } returns true
        every { moshiStringListRepository.getItem(cachedListName) } returns dummyData
        every { moshiStringListRepository.getErrorMsg() } returns ""
        coEvery { moshiStringListRepository.save(cachedListName, dummyData) } returns false

        viewModel.loadFromAssets(cachedListName, mockk())

        assert(!viewModel.status.value.success)
        assert(viewModel.status.value.message == FAILED_TO_WRITE_FILE)
        assert(viewModel.status.value.ex == null)
        assert(viewModel.status.value.items.size == 0)
    }

    @Test
    fun `load string list SUCCESS when assets stream is VALID AND write contents to LOCAL STORAGE throws Exception`() {
        setupForReadingFiles()
        setupForWritingFiles()
        coEvery { moshiStringListRepository.load(cachedListName) } returns true
        every { moshiStringListRepository.getItem(cachedListName) } returns dummyData
        every { moshiStringListRepository.getErrorMsg() } returns ""
        coEvery { moshiStringListRepository.save(cachedListName, dummyData) } throws Exception()

        viewModel.loadFromAssets(cachedListName, mockk())

        assert(!viewModel.status.value.success)
        assert(viewModel.status.value.message != null)
        assert(viewModel.status.value.ex != null)
        assert(viewModel.status.value.items.size == 0)
    }

    private fun setupForReadingFiles() {
        every { moshiStringListRepository.setAbsoluteFile(any()) } returns Unit
        every { moshiStringListRepository.setBufferReader(any()) } returns Unit
        every { assetSerializationRepoHelper.getBufferReader(any()) } returns mockk()
    }

    private fun setupForWritingFiles() {
        every { commonSerializationRepoHelper.getAbsoluteFile(any(), any()) } returns mockk()
        every { commonSerializationRepoHelper.getFileWriter(any(), any()) } returns mockk()
        every { commonSerializationRepoHelper.getDirectoryFile(any()) } returns mockk()
        every { commonSerializationRepoHelper.getMainFile(any()) } returns mockk()
        every { moshiStringListRepository.setAbsoluteFile(any()) } returns Unit
        every { moshiStringListRepository.setDirectoryFile(any()) } returns Unit
        every { moshiStringListRepository.setFile(any()) } returns Unit
        every { moshiStringListRepository.setFileWriter(any()) } returns Unit
        every { moshiStringListRepository.setItem(any(), any()) } returns Unit
    }
}