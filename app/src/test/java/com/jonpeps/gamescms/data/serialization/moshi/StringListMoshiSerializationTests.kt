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
class StringListMoshiSerializationTests {
    private val dispatcher = UnconfinedTestDispatcher()
    @MockK
    private lateinit var stringListJsonAdapter: JsonAdapter<TableTemplateItemListMoshi>
    @MockK
    private lateinit var moshiJsonAdapterCreator: MoshiJsonAdapterCreator

    private lateinit var serializer: StringListMoshiSerialization

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        serializer = StringListMoshiSerialization(moshiJsonAdapterCreator, dispatcher)
    }

    @Test
    fun `get moshi adapter`() {
        every { moshiJsonAdapterCreator.getStringListJsonAdapter() } returns stringListJsonAdapter
        serializer.getMoshiAdapter()
        verify { moshiJsonAdapterCreator.getStringListJsonAdapter() }
    }
}