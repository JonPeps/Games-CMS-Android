package com.jonpeps.gamescms.viewmodels

import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateDetailsListMoshi
import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateDetailsMoshi
import com.jonpeps.gamescms.data.helpers.InputStreamTableTemplateStatus
import com.jonpeps.gamescms.data.repositories.MoshiTableTemplateDetailsListRepository
import com.jonpeps.gamescms.data.repositories.MoshiTableTemplateRepository
import com.jonpeps.gamescms.data.serialization.ICommonSerializationRepoHelper
import com.jonpeps.gamescms.ui.tabletemplates.viewmodels.SerializeTableTemplatesViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SerializeTableTemplatesViewModelTests {
    private val dispatcher = UnconfinedTestDispatcher()
    @MockK
    private lateinit var mockInputStreamTableTemplateStatus: InputStreamTableTemplateStatus
    @MockK
    private lateinit var mockMoshiTableTemplateRepository: MoshiTableTemplateRepository
    @MockK
    private lateinit var mockMoshiTableTemplateDetailsListRepository: MoshiTableTemplateDetailsListRepository
    @MockK
    private lateinit var mockCommonSerializationRepoHelper: ICommonSerializationRepoHelper

    private lateinit var tableTemplateDetailsListMoshi: TableTemplateDetailsListMoshi

    private lateinit var viewModel: SerializeTableTemplatesViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        viewModel = SerializeTableTemplatesViewModel(dispatcher,
            mockInputStreamTableTemplateStatus,
            mockMoshiTableTemplateRepository,
            mockMoshiTableTemplateDetailsListRepository,
            mockCommonSerializationRepoHelper)

        tableTemplateDetailsListMoshi = TableTemplateDetailsListMoshi(
            arrayListOf(
                TableTemplateDetailsMoshi("test1", "test1.json", true),
                TableTemplateDetailsMoshi("test2", "test2.json", true)
        ))
    }

    @Test
    fun `read items from assets SUCCESS`() {
        every { mockCommonSerializationRepoHelper.getInputStreamFromStr(any()) } returns mockk()
        coEvery { mockInputStreamTableTemplateStatus.processSuspend(any(), any(), any()) } returns Unit
        every { mockInputStreamTableTemplateStatus.status.success } returns true
        every { mockInputStreamTableTemplateStatus.status.item } returns mockk()
        every { mockInputStreamTableTemplateStatus.status.errorMessage } returns ""

        viewModel.readItemsFromAssets("", "", "")

        assert(viewModel.serializeStatus.value.success)
    }

    @Test
    fun `read items from assets FAILS due to NULL INPUT STREAM`() {
        every { mockCommonSerializationRepoHelper.getInputStreamFromStr(any()) } returns null

        viewModel.readItemsFromAssets("", "", "")

        assert(!viewModel.serializeStatus.value.success)
        assert(viewModel.serializeStatus.value.errorMessage == SerializeTableTemplatesViewModel.FAILED_TO_FIND_ASSET_FILE)
    }

    @Test
    fun `read items SUCCESS`() {
        every { mockMoshiTableTemplateRepository.setAbsoluteFile(any()) } returns mockk()
        every { mockCommonSerializationRepoHelper.getAbsoluteFile(any(), any()) } returns mockk()
        coEvery { mockMoshiTableTemplateDetailsListRepository.load() }
        coEvery { mockMoshiTableTemplateDetailsListRepository.load() } returns true
        every { mockMoshiTableTemplateDetailsListRepository.getItem() } returns mockk()
        every { mockMoshiTableTemplateDetailsListRepository.getErrorMsg() } returns ""

        viewModel.readItems("", "")

        assert(viewModel.serializeStatus.value.success)
        assert(viewModel.serializeStatus.value.results != null)
        assert(viewModel.serializeStatus.value.errorMessage == "")
    }

    @Test
    fun `read items FAILS`() {
        every { mockMoshiTableTemplateRepository.setAbsoluteFile(any()) } returns mockk()
        every { mockCommonSerializationRepoHelper.getAbsoluteFile(any(), any()) } returns mockk()
        coEvery { mockMoshiTableTemplateDetailsListRepository.load() } returns false
        every { mockMoshiTableTemplateDetailsListRepository.getErrorMsg() } returns "error!"

        viewModel.readItems("", "")

        assert(!viewModel.serializeStatus.value.success)
        assert(viewModel.serializeStatus.value.errorMessage == "error!")
    }

    @Test
    fun `update table template SUCCESS WHEN EXISTING ITEM FOUND`() {
        every { mockCommonSerializationRepoHelper.getAbsoluteFile(any(), any()) } returns mockk()
        every { mockMoshiTableTemplateRepository.setAbsoluteFile(any()) } returns Unit
        coEvery { mockMoshiTableTemplateRepository.save(any()) } returns true
        coEvery { mockMoshiTableTemplateDetailsListRepository.save(any()) } returns true
        every { mockMoshiTableTemplateDetailsListRepository.getErrorMsg() } returns ""

        viewModel.updateTableTemplate("test1", "",
            mockk(), tableTemplateDetailsListMoshi, true)

        assert(viewModel.updatedTableTemplateStatus.value.success)
        assert(tableTemplateDetailsListMoshi.items.size == 2)
        assert(viewModel.updatedTableTemplateStatus.value.errorMessage == "")
    }

    @Test
    fun `update table template SUCCESS WHEN EXISTING ITEM NOT FOUND`() {
        every { mockCommonSerializationRepoHelper.getAbsoluteFile(any(), any()) } returns mockk()
        every { mockMoshiTableTemplateRepository.setAbsoluteFile(any()) } returns Unit
        coEvery { mockMoshiTableTemplateRepository.save(any()) } returns true
        coEvery { mockMoshiTableTemplateDetailsListRepository.save(any()) } returns true
        every { mockMoshiTableTemplateDetailsListRepository.getErrorMsg() } returns ""

        viewModel.updateTableTemplate("test3", "",
            mockk(), tableTemplateDetailsListMoshi, true)

        assert(viewModel.updatedTableTemplateStatus.value.success)
        assert(tableTemplateDetailsListMoshi.items.size == 3)
        assert(viewModel.updatedTableTemplateStatus.value.errorMessage == "")
    }

    @Test
    fun `update table template FAILS WHEN SAVE TableTemplate TO REPO FAILS`() {
        every { mockCommonSerializationRepoHelper.getAbsoluteFile(any(), any()) } returns mockk()
        every { mockMoshiTableTemplateRepository.setAbsoluteFile(any()) } returns Unit
        coEvery { mockMoshiTableTemplateRepository.save(any()) } returns false
        every { mockMoshiTableTemplateRepository.getErrorMsg() } returns "error"

        viewModel.updateTableTemplate("test1", "",
            mockk(), tableTemplateDetailsListMoshi, true)

        assert(!viewModel.updatedTableTemplateStatus.value.success)
        assert(viewModel.updatedTableTemplateStatus.value.errorMessage == mockMoshiTableTemplateRepository.getErrorMsg())
    }

    @Test
    fun `update table template FAILS WHEN SAVE TableTemplate TO REPO SUCCESS yet SAVE TABLE TEMPLATE DETAILS LIST FAILS`() {
        every { mockCommonSerializationRepoHelper.getAbsoluteFile(any(), any()) } returns mockk()
        every { mockMoshiTableTemplateRepository.setAbsoluteFile(any()) } returns Unit
        coEvery { mockMoshiTableTemplateRepository.save(any()) } returns true
        every { mockMoshiTableTemplateDetailsListRepository.getErrorMsg() } returns "error"
        coEvery { mockMoshiTableTemplateDetailsListRepository.save(any()) } returns false

        viewModel.updateTableTemplate("test1", "",
            mockk(), tableTemplateDetailsListMoshi, true)

        assert(!viewModel.updatedTableTemplateStatus.value.success)
        assert(viewModel.updatedTableTemplateStatus.value.errorMessage == mockMoshiTableTemplateDetailsListRepository.getErrorMsg())
    }
}