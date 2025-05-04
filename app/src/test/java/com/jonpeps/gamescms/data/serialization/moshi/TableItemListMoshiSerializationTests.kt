package com.jonpeps.gamescms.data.serialization.moshi

import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateItemListMoshi
import com.squareup.moshi.JsonAdapter
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
class TableItemListMoshiSerializationTests {
    private val dispatcher = UnconfinedTestDispatcher()
    @MockK
    private lateinit var tableItemListMoshiSerialization: JsonAdapter<TableTemplateItemListMoshi>
    @MockK
    private lateinit var moshiJsonAdapterCreator: MoshiJsonAdapterCreator

    private lateinit var serializer: TableItemListMoshiSerialization

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        serializer = TableItemListMoshiSerialization(moshiJsonAdapterCreator, dispatcher)
    }

    @Test
    fun `get moshi adapter`() {
        every { moshiJsonAdapterCreator.getTableItemTemplateJsonAdapter() } returns tableItemListMoshiSerialization
        serializer.getMoshiAdapter()
        verify { moshiJsonAdapterCreator.getTableItemTemplateJsonAdapter() }
    }
}