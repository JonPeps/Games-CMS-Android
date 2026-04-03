package com.jonpeps.gamescms.viewmodels

import com.jonpeps.gamescms.data.dataclasses.moshi.StringListMoshi
import com.jonpeps.gamescms.data.serialization.ICommonDeleteFileHelper
import com.jonpeps.gamescms.data.serialization.ICommonSerializationRepoHelper
import com.jonpeps.gamescms.data.viewmodels.BasicStringListViewModel
import com.jonpeps.gamescms.data.viewmodels.BasicStringListViewModel.Companion.FAILED_TO_LOAD_FILE
import com.jonpeps.gamescms.data.helpers.IStringListItemsVmChangesCache
import com.jonpeps.gamescms.data.repositories.base.IBaseCachedMoshiJsonRepository
import com.jonpeps.gamescms.data.serialization.SubDeleteFlag
import com.jonpeps.gamescms.data.viewmodels.BasicStringListViewModel.Companion.FAILED_TO_SAVE_FILE
import com.jonpeps.gamescms.data.viewmodels.BasicStringListViewModel.Companion.NO_CACHE_NAME
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
class BasicStringListViewModelTests {
    private val dispatcher = UnconfinedTestDispatcher()
    @MockK
    private lateinit var mockMoshiCacheStringListRepository: IBaseCachedMoshiJsonRepository<StringListMoshi>
    @MockK
    private lateinit var mockCommonSerializationRepoHelper: ICommonSerializationRepoHelper
    @MockK
    private lateinit var mockListItemsVmChangesCache: IStringListItemsVmChangesCache
    @MockK
    private lateinit var mockCommonDeleteFileHelper: ICommonDeleteFileHelper

    private lateinit var viewModel: BasicStringListViewModel

    private val dummyListData = arrayListOf("item1", "item2")
    private val dummyData = StringListMoshi(dummyListData)

    private val filesListPath = "test1/"
    private val filesDirectoryPath = "test2/"
    private val cachedListName = "list"

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        viewModel = BasicStringListViewModel(
            filesDirectoryPath,
            filesListPath,
            mockMoshiCacheStringListRepository,
            mockCommonSerializationRepoHelper,
            mockListItemsVmChangesCache,
            mockCommonDeleteFileHelper,
            dispatcher
        )

        setupCommonFiles()
        setupForReadingFiles()
        setupForWritingFiles()

        every { mockListItemsVmChangesCache.set(any(), any()) } returns Unit
    }

    @Test
    fun `load string list SUCCESS WHEN IO files are VALID and load from repository RETURNS TRUE`() {
        every { mockCommonSerializationRepoHelper.getAbsoluteFile(any(), any()) } returns mockk()
        every { mockCommonSerializationRepoHelper.getBufferReader(any(), any()) } returns mockk()

        every { mockListItemsVmChangesCache.isPopulated() } returns false
        coEvery { mockMoshiCacheStringListRepository.load(cachedListName) } returns true
        every { mockMoshiCacheStringListRepository.getItem(cachedListName) } returns dummyData
        every { mockMoshiCacheStringListRepository.getErrorMsg() } returns ""
        every { mockListItemsVmChangesCache.get(cachedListName) } returns dummyListData

        viewModel.load(cachedListName, false)

        verify { mockListItemsVmChangesCache.set(cachedListName, dummyListData) }

        assert(viewModel.status.success)
        assert(viewModel.status.message == "")
        assert(viewModel.status.ex == null)
        assert(viewModel.status.items.size == 2)
    }

    @Test
    fun `load string list SUCCESS WHEN IO files are VALID and load from repository RETURNS TRUE and no cached items`() {
        every { mockCommonSerializationRepoHelper.getAbsoluteFile(any(), any()) } returns mockk()
        every { mockCommonSerializationRepoHelper.getBufferReader(any(), any()) } returns mockk()

        every { mockListItemsVmChangesCache.isPopulated() } returns false
        coEvery { mockMoshiCacheStringListRepository.load(NO_CACHE_NAME) } returns true
        every { mockMoshiCacheStringListRepository.getItem(NO_CACHE_NAME) } returns dummyData
        every { mockMoshiCacheStringListRepository.getErrorMsg() } returns ""

        viewModel.load(NO_CACHE_NAME, false)

        assert(viewModel.status.success)
        assert(viewModel.status.message == "")
        assert(viewModel.status.ex == null)
        assert(viewModel.status.items.size == 2)
    }

    @Test
    fun `load string list SUCCESS WHEN IO files are VALID and load from repository RETURNS TRUE WHEN no cached items should be polled`() {
        every { mockCommonSerializationRepoHelper.getAbsoluteFile(any(), any()) } returns mockk()
        every { mockCommonSerializationRepoHelper.getBufferReader(any(), any()) } returns mockk()

        every { mockListItemsVmChangesCache.isPopulated() } returns false
        coEvery { mockMoshiCacheStringListRepository.load(cachedListName) } returns true
        every { mockMoshiCacheStringListRepository.getItem(cachedListName) } returns dummyData
        every { mockMoshiCacheStringListRepository.getErrorMsg() } returns ""

        viewModel.load(cachedListName, true)

        verify { mockListItemsVmChangesCache.set(any(), any()) }

        assert(viewModel.status.success)
        assert(viewModel.status.message == "")
        assert(viewModel.status.ex == null)
        assert(viewModel.status.items.size == 2)
    }

    @Test
    fun `load string list SUCCESS WHEN items are cached`() {
        every { mockListItemsVmChangesCache.isPopulated() } returns true
        every { mockListItemsVmChangesCache.get(cachedListName) } returns dummyListData
        coEvery { mockMoshiCacheStringListRepository.load(cachedListName) } returns true

        viewModel.load(cachedListName, true)

        assert(viewModel.status.success)
        assert(viewModel.status.message == "")
        assert(viewModel.status.ex == null)
        assert(viewModel.status.items.size == dummyListData.size)
    }

    @Test
    fun `load string list FAILS WHEN IO files are VALID and load from repository RETURNS FALSE`() {
        every { mockCommonSerializationRepoHelper.getAbsoluteFile(any(), any()) } returns mockk()
        every { mockCommonSerializationRepoHelper.getBufferReader(any(), any()) } returns mockk()

        every { mockListItemsVmChangesCache.isPopulated() } returns false
        coEvery { mockMoshiCacheStringListRepository.load(cachedListName) } returns false

        viewModel.load(cachedListName, false)

        assert(!viewModel.status.success)
        assert(viewModel.status.message == FAILED_TO_LOAD_FILE + filesListPath)
        assert(viewModel.status.ex == null)
        assert(viewModel.status.items.isEmpty())
    }

    @Test
    fun `load string list FAILS WHEN IO files are VALID and load from repository RETURNS TRUE AND get item RETURNS NULL`() {
        every { mockCommonSerializationRepoHelper.getAbsoluteFile(any(), any()) } returns mockk()
        every { mockCommonSerializationRepoHelper.getBufferReader(any(), any()) } returns mockk()

        every { mockListItemsVmChangesCache.isPopulated() } returns false
        coEvery { mockMoshiCacheStringListRepository.load(cachedListName) } returns true
        every { mockMoshiCacheStringListRepository.getItem(cachedListName) } returns null

        viewModel.load(cachedListName, false)

        assert(!viewModel.status.success)
        assert(viewModel.status.message == FAILED_TO_LOAD_FILE + filesListPath)
        assert(viewModel.status.ex == null)
        assert(viewModel.status.items.isEmpty())
    }

    @Test
    fun `load string list FAILS WHEN IO files THROWS EXCEPTION` () {
        every { mockCommonSerializationRepoHelper.getBufferReader(any(), any()) } returns mockk()
        every { mockCommonSerializationRepoHelper.getAbsoluteFile(any(), any()) } throws Exception("Runtime error!")

        viewModel.load(cachedListName, false)

        assert(!viewModel.status.success)
        assert(viewModel.status.message == "Runtime error!")
        assert(viewModel.status.ex != null)
        assert(viewModel.status.items.isEmpty())
    }

    @Test
    fun `add string list EXITS due to ITEM ALREADY EXISTS`() {
        coEvery { mockMoshiCacheStringListRepository.save(any(), any()) } returns true
        every { mockListItemsVmChangesCache.set(any(), any()) } returns Unit

        viewModel.add("test")
        viewModel.add("test")

        assert(viewModel.status.success)
        assert(viewModel.status.message == "")
        assert(viewModel.status.ex == null)
        assert(viewModel.status.items.size == 1)
    }

    @Test
    fun `add string FAILS due to SAVE TO REPO FAILS`() {
        coEvery { mockMoshiCacheStringListRepository.save(any(), any()) } returns false
        every { mockListItemsVmChangesCache.set(any(), any()) } returns Unit

        viewModel.add("test")

        assert(!viewModel.status.success)
        assert(viewModel.status.message == FAILED_TO_SAVE_FILE + filesListPath)
        assert(viewModel.status.ex == null)
    }

    @Test
    fun `add string list SUCCESS WHEN IO files are VALID and save to repository RETURNS TRUE`() {
        coEvery { mockMoshiCacheStringListRepository.save(any(), any()) } returns true

        viewModel.add("test")

        verify { mockListItemsVmChangesCache.set(any(), any()) }

        assert(viewModel.status.success)
        assert(viewModel.status.message == "")
        assert(viewModel.status.ex == null)
        assert(viewModel.status.items.size == 1)
    }

    @Test
    fun `delete string SUCCESS WHEN IO files are VALID AND DELETE file RETURNS TRUE as well as SAVE files RETURNS TRUE`() {
        every { mockCommonDeleteFileHelper.deleteFile(any(), any()) } returns true
        every { mockCommonDeleteFileHelper.deleteDirectory(any()) } returns Unit
        every { mockListItemsVmChangesCache.set(any(), any()) } returns Unit

        viewModel.delete("test", "dir", SubDeleteFlag.FILE)

        assert(viewModel.status.success)
        assert(viewModel.status.message == "")
        assert(viewModel.status.ex == null)
        assert(viewModel.status.items.isEmpty())
    }

    @Test
    fun `delete string SUCCESS WHEN IO files are VALID AND DELETE file AND DIRECTORY RETURNS TRUE as well as SAVE files RETURNS TRUE`() {
        every { mockCommonDeleteFileHelper.deleteDirectory(any()) } returns Unit
        every { mockListItemsVmChangesCache.set(any(), any()) } returns Unit

        viewModel.delete("test", "dir", SubDeleteFlag.DIRECTORY_AND_FILES)

        assert(viewModel.status.success)
        assert(viewModel.status.message == "")
        assert(viewModel.status.ex == null)
        assert(viewModel.status.items.isEmpty())
    }

    @Test
    fun `delete string SUCCESS`() {
        every { mockCommonDeleteFileHelper.deleteDirectory(any()) } returns Unit
        every { mockListItemsVmChangesCache.set(any(), any()) } returns Unit

        viewModel.delete("test", "dir", SubDeleteFlag.NONE)

        assert(viewModel.status.success)
        assert(viewModel.status.message == "")
        assert(viewModel.status.ex == null)
        assert(viewModel.status.items.isEmpty())
    }

    private fun setupForReadingFiles() {
        every { mockMoshiCacheStringListRepository.setAbsoluteFile(any()) } returns Unit
        every { mockMoshiCacheStringListRepository.setBufferReader(any()) } returns Unit
    }

    private fun setupForWritingFiles() {
        every { mockMoshiCacheStringListRepository.setAbsoluteFile(any()) } returns Unit
        every { mockMoshiCacheStringListRepository.assignDirectoryFile(any()) } returns Unit
        every { mockMoshiCacheStringListRepository.setFile(any()) } returns Unit
        every { mockMoshiCacheStringListRepository.setFileWriter(any()) } returns Unit
        every { mockMoshiCacheStringListRepository.setItem(any(), any()) } returns Unit
    }

    private fun setupCommonFiles() {
        every { mockCommonSerializationRepoHelper.getAbsoluteFile(any(), any()) } returns mockk()
        every { mockCommonSerializationRepoHelper.getFileWriter(any(), any()) } returns mockk()
        every { mockCommonSerializationRepoHelper.getDirectoryFile(any()) } returns mockk()
        every { mockCommonSerializationRepoHelper.getMainFile(any()) } returns mockk()
    }
}