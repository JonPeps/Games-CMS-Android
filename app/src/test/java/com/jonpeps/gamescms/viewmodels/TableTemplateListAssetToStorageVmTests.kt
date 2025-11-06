package com.jonpeps.gamescms.viewmodels

import com.jonpeps.gamescms.data.repositories.IMoshiStringListRepository
import com.jonpeps.gamescms.data.serialization.ICommonSerializationRepoHelper
import com.jonpeps.gamescms.data.serialization.debug.IInputStreamSerializationRepoHelper
import com.jonpeps.gamescms.ui.tabletemplates.viewmodels.TableTemplateListAssetToStorageVm
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Before
import org.junit.Test
import java.io.InputStream

@OptIn(ExperimentalCoroutinesApi::class)
class TableTemplateListAssetToStorageVmTests {
    private val dispatcher = UnconfinedTestDispatcher()
    @MockK
    private lateinit var inputStream: InputStream
    @MockK
    private lateinit var mockMoshiStringListRepository: IMoshiStringListRepository
    @MockK
    private lateinit var commonSerializationRepoHelper: ICommonSerializationRepoHelper
    @MockK
    private lateinit var inputStreamSerializationRepoHelper: IInputStreamSerializationRepoHelper
    @MockK
    private lateinit var viewModel: TableTemplateListAssetToStorageVm

    private val directory = "directory"
    private val fileName = "fileName"

    @Before
    fun setup() {
        MockKAnnotations.init(this)


    }

    @Test
    fun `SUCCESS with EQUAL list names AND file names`() {
        assert(true)
    }
}