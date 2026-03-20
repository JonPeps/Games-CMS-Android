package com.jonpeps.gamescms.data.serialization

import com.jonpeps.gamescms.data.helpers.IGenericSerializationCache
import com.jonpeps.gamescms.data.repositories.base.IBaseSingleItemMoshiJsonRepository
import com.jonpeps.gamescms.data.serialization.GenericRepoLoader.Companion.JSON_ITEM_TO_SAVE_IS_NULL
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.io.BufferedReader
import java.io.File

class GenericRepoLoaderTests {
    data class TempData1(val name: String)
    data class TempData2(val name: String)

    @MockK
    private lateinit var mockRepository: IBaseSingleItemMoshiJsonRepository<TempData1>
    @MockK
    private lateinit var mockCommonSerializationRepoHelper: CommonSerializationRepoHelper
    @MockK
    private lateinit var mockCache: IGenericSerializationCache<TempData2>
    @MockK
    private lateinit var mockFile: File
    @MockK
    private lateinit var mockBufferReader: BufferedReader

    class GenericRepoLoaderSut(
        repository: IBaseSingleItemMoshiJsonRepository<TempData1>,
        helper: CommonSerializationRepoHelper,
        cache: IGenericSerializationCache<TempData2>
    ): GenericRepoLoader<TempData1, TempData2>(
        repository,
        helper,
        cache
    ){
        override fun getParsedValue(item: TempData1): TempData2? {
            return TempData2(item.name)
        }
    }

    private lateinit var sut: GenericRepoLoaderSut

    private val tempData1 = TempData1("test1")
    private val tempData2 = TempData2("test2")

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        sut = GenericRepoLoaderSut(
            mockRepository,
            mockCommonSerializationRepoHelper,
            mockCache
        )

        every { mockCommonSerializationRepoHelper.getAbsoluteFile(any(), any()) } returns mockFile
        every { mockCommonSerializationRepoHelper.getBufferReader(any(), any()) } returns mockBufferReader
        every { mockRepository.setAbsoluteFile(mockFile) } returns Unit
        every { mockRepository.setBufferReader(mockBufferReader) } returns Unit
        every { mockCache.set(any(), any()) } returns Unit
    }

    @Test
    fun `GenericRepoLoader load SUCCESS from File`() = runBlocking {
        every { mockCache.isPopulated() } returns false
        coEvery { mockRepository.load() } returns true
        every { mockRepository.getItem() } returns tempData1

        sut.load("test", "path", "cacheName", false)

        val result = sut.getItem()
        assert(result?.success == true)
        assert(result?.item?.name == "test1")
        assert(result?.currentIndex == 0)
        assert(result?.message == "")
        assert(result?.ex == null)
    }

    @Test
    fun `GenericRepoLoader load SUCCESS from CACHE`() = runBlocking {
        every { mockCache.isPopulated() } returns true
        every { mockRepository.getItem() } returns tempData1
        every { mockCache.get(any()) } returns tempData2

        sut.load("test", "path", "cacheName", true)

        val result = sut.getItem()
        assert(result?.success == true)
        assert(result?.item?.name == "test2")
        assert(result?.currentIndex == 0)
        assert(result?.message == "")
        assert(result?.ex == null)
    }

    @Test
    fun `GenericRepoLoader load SUCCESS attempting to load from CACHE but CACHE is empty`() = runBlocking {
        every { mockCache.isPopulated() } returns false
        coEvery { mockRepository.load() } returns true
        every { mockRepository.getItem() } returns tempData1

        sut.load("test", "path", "cacheName", true)

        val result = sut.getItem()
        assert(result?.success == true)
        assert(result?.item?.name == "test1")
        assert(result?.currentIndex == 0)
        assert(result?.message == "")
        assert(result?.ex == null)
    }

    @Test
    fun `GenericRepoLoader load FAILS due to EXCEPTION THROWN`() = runBlocking {
        every { mockCache.isPopulated() } returns false
        coEvery { mockRepository.load() } throws Exception("error!")

        sut.load("test", "path", "cacheName", false)

        val result = sut.getItem()
        assert(!result?.success!!)
        assert(result.item == null)
        assert(result.currentIndex == 0)
        assert(result.message == "error!")
        assert(result.ex is Exception)
    }

    @Test
    fun `GenericRepoLoader load FAILS due to LOAD from Repository returning FALSE`() = runBlocking {
        every { mockCache.isPopulated() } returns false
        coEvery { mockRepository.load() } returns false
        every { mockRepository.getErrorMsg() } returns "error!"

        sut.load("test", "path", "cacheName", false)

        val result = sut.getItem()
        assert(!result?.success!!)
        assert(result.item == null)
        assert(result.currentIndex == 0)
        assert(result.message == "error!")
        assert(result.ex == null)
    }

    @Test
    fun `GenericRepoLoader load FAILS due to NULL ITEM from Repository`() = runBlocking {
        every { mockCache.isPopulated() } returns false
        coEvery { mockRepository.load() } returns true
        every { mockRepository.getItem() } returns null

        sut.load("test", "path", "cacheName", false)

        val result = sut.getItem()
        assert(!result?.success!!)
        assert(result.item == null)
        assert(result.currentIndex == 0)
        assert(result.message == JSON_ITEM_TO_SAVE_IS_NULL + "test")
    }
}