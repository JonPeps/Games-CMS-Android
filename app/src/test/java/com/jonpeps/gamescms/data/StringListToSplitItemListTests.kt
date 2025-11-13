package com.jonpeps.gamescms.data

import com.jonpeps.gamescms.data.dataclasses.moshi.StringListMoshi
import com.jonpeps.gamescms.data.helpers.JsonStringFilenamePair
import com.jonpeps.gamescms.data.helpers.JsonStringListHelper
import com.jonpeps.gamescms.data.helpers.StringListToSplitItemList
import com.jonpeps.gamescms.data.serialization.moshi.InputStreamStringList
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.io.InputStream

@OptIn(ExperimentalCoroutinesApi::class)
class StringListToSplitItemListTests {
    @MockK
    private lateinit var jsonStringListHelper: JsonStringListHelper
    @MockK
    private lateinit var inputStreamStringList: InputStreamStringList
    @MockK
    private lateinit var inputStream: InputStream

    private lateinit var sut: StringListToSplitItemList

    private val listItems = arrayListOf("name1:name1.json",
        "name2:name2.json",
        "name3:name3.json")

    private val stringListItem = StringListMoshi(listItems)

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        sut = StringListToSplitItemList(
            inputStreamStringList,
            jsonStringListHelper
        )

        coEvery { inputStreamStringList.processSuspend(inputStream, any(), any()) } returns Unit
    }

    @Test
    fun `SUCCESS with EQUAL list names AND file names`() = runBlocking {
        every { inputStreamStringList.status.success } returns true
        every { inputStreamStringList.status.item } returns stringListItem

        every { jsonStringListHelper.splitItem(stringListItem) } returns JsonStringFilenamePair(
            listItems,
            listItems
        )

        sut.loadSuspend(inputStream, "", "")

        assert(sut.status.success)
    }

    @Test
    fun `FAILURE with UNEQUAL list names AND file names`() = runBlocking {
        every { inputStreamStringList.status.success } returns true
        every { inputStreamStringList.status.item } returns stringListItem

        every { jsonStringListHelper.splitItem(stringListItem) } returns JsonStringFilenamePair(
            emptyList(),
            listItems
        )

        sut.loadSuspend(inputStream, "", "")

        assert(!sut.status.success)
        assert(sut.status.errorMessage == StringListToSplitItemList.Companion.ITEMS_NOT_EQUAL)
    }

    @Test
    fun `FAILURE with JSON ITEM is NULL loaded from InputStreamStringListViewModel view model`() =
        runBlocking {
            every { inputStreamStringList.status.success } returns true
            every { inputStreamStringList.status.item } returns null

            sut.loadSuspend(inputStream, "", "")

            assert(!sut.status.success)
            assert(sut.status.errorMessage == StringListToSplitItemList.Companion.FAILED_TO_LOAD_FILE)
        }

    @Test
    fun `FAILURE with InputStreamStringListViewModel view model load FAILS`() = runBlocking {
        every { inputStreamStringList.status.success } returns false
        every { inputStreamStringList.status.errorMessage } returns "error message"
        every { inputStreamStringList.status.exception } returns Exception()

        sut.loadSuspend(inputStream, "", "")

        assert(!sut.status.success)
        assert(sut.status.errorMessage == "error message")
        assert(sut.status.exception != null)
    }
}