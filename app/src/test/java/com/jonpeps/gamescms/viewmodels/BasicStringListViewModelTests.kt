package com.jonpeps.gamescms.viewmodels

import com.jonpeps.gamescms.data.dataclasses.moshi.StringListMoshi
import com.jonpeps.gamescms.data.serialization.ICommonDeleteFileHelper
import com.jonpeps.gamescms.data.serialization.ICommonSerializationRepoHelper
import com.jonpeps.gamescms.data.viewmodels.BasicStringListViewModel
import com.jonpeps.gamescms.data.viewmodels.BasicStringListViewModel.Companion.FAILED_TO_LOAD_FILE
import com.jonpeps.gamescms.data.helpers.IStringListItemsVmChangesCache
import com.jonpeps.gamescms.data.repositories.ICachedMoshiStringListRepository
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
    private lateinit var moshiStringListRepository: ICachedMoshiStringListRepository
    @MockK
    private lateinit var commonSerializationRepoHelper: ICommonSerializationRepoHelper
    @MockK
    private lateinit var listItemsVmChangesCache: IStringListItemsVmChangesCache
    @MockK
    private lateinit var commonDeleteFileHelper: ICommonDeleteFileHelper

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
        every { listItemsVmChangesCache.get(cachedListName) } returns dummyListData

        viewModel.load(cachedListName, false)

        verify { listItemsVmChangesCache.set(cachedListName, dummyListData) }

        assert(viewModel.status.success)
        assert(viewModel.status.message == "")
        assert(viewModel.status.ex == null)
        assert(viewModel.status.items.size == 2)
    }

    @Test
    fun `load string list SUCCESS WHEN IO files are VALID and load from repository RETURNS TRUE and no cached items`() {
        setupForReadingFiles()
        every { commonSerializationRepoHelper.getAbsoluteFile(any(), any()) } returns mockk()
        every { commonSerializationRepoHelper.getBufferReader(any(), any()) } returns mockk()

        every { listItemsVmChangesCache.isPopulated() } returns false
        coEvery { moshiStringListRepository.load(NO_CACHE_NAME) } returns true
        every { moshiStringListRepository.getItem(NO_CACHE_NAME) } returns dummyData
        every { moshiStringListRepository.getErrorMsg() } returns ""

        viewModel.load(NO_CACHE_NAME, false)

        assert(viewModel.status.success)
        assert(viewModel.status.message == "")
        assert(viewModel.status.ex == null)
        assert(viewModel.status.items.size == 2)
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

        assert(viewModel.status.success)
        assert(viewModel.status.message == "")
        assert(viewModel.status.ex == null)
        assert(viewModel.status.items.size == 2)
    }

    @Test
    fun `load string list SUCCESS WHEN items are cached`() {
        every { listItemsVmChangesCache.isPopulated() } returns true
        every { listItemsVmChangesCache.get(cachedListName) } returns dummyListData
        coEvery { moshiStringListRepository.load(cachedListName) } returns true

        viewModel.load(cachedListName, true)

        assert(viewModel.status.success)
        assert(viewModel.status.message == "")
        assert(viewModel.status.ex == null)
        assert(viewModel.status.items.size == dummyListData.size)
    }

    @Test
    fun `load string list FAILS WHEN IO files are VALID and load from repository RETURNS FALSE`() {
        setupForReadingFiles()
        every { commonSerializationRepoHelper.getAbsoluteFile(any(), any()) } returns mockk()
        every { commonSerializationRepoHelper.getBufferReader(any(), any()) } returns mockk()

        every { listItemsVmChangesCache.isPopulated() } returns false
        coEvery { moshiStringListRepository.load(cachedListName) } returns false

        viewModel.load(cachedListName, false)

        assert(!viewModel.status.success)
        assert(viewModel.status.message == FAILED_TO_LOAD_FILE + filesListPath)
        assert(viewModel.status.ex == null)
        assert(viewModel.status.items.isEmpty())
    }

    @Test
    fun `load string list FAILS WHEN IO files are VALID and load from repository RETURNS TRUE AND get item RETURNS NULL`() {
        setupForReadingFiles()
        every { commonSerializationRepoHelper.getAbsoluteFile(any(), any()) } returns mockk()
        every { commonSerializationRepoHelper.getBufferReader(any(), any()) } returns mockk()

        every { listItemsVmChangesCache.isPopulated() } returns false
        coEvery { moshiStringListRepository.load(cachedListName) } returns true
        every { moshiStringListRepository.getItem(cachedListName) } returns null

        viewModel.load(cachedListName, false)

        assert(!viewModel.status.success)
        assert(viewModel.status.message == FAILED_TO_LOAD_FILE + filesListPath)
        assert(viewModel.status.ex == null)
        assert(viewModel.status.items.isEmpty())
    }

    @Test
    fun `load string list FAILS WHEN IO files THROWS EXCEPTION` () {
        setupForReadingFiles()
        every { commonSerializationRepoHelper.getBufferReader(any(), any()) } returns mockk()
        every { commonSerializationRepoHelper.getAbsoluteFile(any(), any()) } throws Exception("Runtime error!")

        viewModel.load(cachedListName, false)

        assert(!viewModel.status.success)
        assert(viewModel.status.message == "Runtime error!")
        assert(viewModel.status.ex != null)
        assert(viewModel.status.items.isEmpty())
    }

    @Test
    fun `add string list EXITS due to ITEM ALREADY EXISTS`() {
        setupForWritingFiles()
        setupCommonFiles()

        coEvery { moshiStringListRepository.save(any(), any()) } returns true
        every { listItemsVmChangesCache.set(any(), any()) } returns Unit

        viewModel.add("test")
        viewModel.add("test")

        assert(viewModel.status.success)
        assert(viewModel.status.message == "")
        assert(viewModel.status.ex == null)
        assert(viewModel.status.items.size == 1)
    }

    @Test
    fun `add string FAILS due to SAVE TO REPO FAILS`() {
        setupForWritingFiles()
        setupCommonFiles()

        coEvery { moshiStringListRepository.save(any(), any()) } returns false
        every { listItemsVmChangesCache.set(any(), any()) } returns Unit

        viewModel.add("test")

        assert(!viewModel.status.success)
        assert(viewModel.status.message == FAILED_TO_SAVE_FILE + filesListPath)
        assert(viewModel.status.ex == null)
        assert(viewModel.status.items.isEmpty())
    }

    @Test
    fun `add string list SUCCESS WHEN IO files are VALID and save to repository RETURNS TRUE`() {
        setupForWritingFiles()
        setupCommonFiles()

        coEvery { moshiStringListRepository.save(any(), any()) } returns true

        viewModel.add("test")

        verify { listItemsVmChangesCache.set(any(), any()) }

        assert(viewModel.status.success)
        assert(viewModel.status.message == "")
        assert(viewModel.status.ex == null)
        assert(viewModel.status.items.size == 1)
    }

    @Test
    fun `delete string SUCCESS WHEN IO files are VALID AND DELETE file RETURNS TRUE as well as SAVE files RETURNS TRUE`() {
        setupForWritingFiles()
        setupCommonFiles()
        every { commonDeleteFileHelper.deleteFile(any(), any()) } returns true
        every { commonDeleteFileHelper.deleteDirectory(any()) } returns Unit
        every { listItemsVmChangesCache.set(any(), any()) } returns Unit

        viewModel.delete("test", "dir", SubDeleteFlag.FILE)

        assert(viewModel.status.success)
        assert(viewModel.status.message == "")
        assert(viewModel.status.ex == null)
        assert(viewModel.status.items.isEmpty())

        verify { listItemsVmChangesCache.set(any(), any()) }
    }

    @Test
    fun `delete string SUCCESS WHEN IO files are VALID AND DELETE file AND DIRECTORY RETURNS TRUE as well as SAVE files RETURNS TRUE`() {
        setupForWritingFiles()
        setupCommonFiles()
        every { commonDeleteFileHelper.deleteDirectory(any()) } returns Unit
        every { listItemsVmChangesCache.set(any(), any()) } returns Unit

        viewModel.delete("test", "dir", SubDeleteFlag.DIRECTORY_AND_FILES)

        assert(viewModel.status.success)
        assert(viewModel.status.message == "")
        assert(viewModel.status.ex == null)
        assert(viewModel.status.items.isEmpty())
    }

    @Test
    fun `delete string SUCCESS`() {
        setupForWritingFiles()
        setupCommonFiles()
        every { commonDeleteFileHelper.deleteDirectory(any()) } returns Unit
        every { listItemsVmChangesCache.set(any(), any()) } returns Unit

        viewModel.delete("test", "dir", SubDeleteFlag.NONE)

        assert(viewModel.status.success)
        assert(viewModel.status.message == "")
        assert(viewModel.status.ex == null)
        assert(viewModel.status.items.isEmpty())
    }

    private fun setupForReadingFiles() {
        every { moshiStringListRepository.setAbsoluteFile(any()) } returns Unit
        every { moshiStringListRepository.setBufferReader(any()) } returns Unit
    }

    private fun setupForWritingFiles() {
        every { moshiStringListRepository.setAbsoluteFile(any()) } returns Unit
        every { moshiStringListRepository.assignDirectoryFile(any()) } returns Unit
        every { moshiStringListRepository.setFile(any()) } returns Unit
        every { moshiStringListRepository.setFileWriter(any()) } returns Unit
        every { moshiStringListRepository.setItem(any(), any()) } returns Unit
    }

    private fun setupCommonFiles() {
        every { commonSerializationRepoHelper.getAbsoluteFile(any(), any()) } returns mockk()
        every { commonSerializationRepoHelper.getFileWriter(any(), any()) } returns mockk()
        every { commonSerializationRepoHelper.getDirectoryFile(any()) } returns mockk()
        every { commonSerializationRepoHelper.getMainFile(any()) } returns mockk()
    }
}