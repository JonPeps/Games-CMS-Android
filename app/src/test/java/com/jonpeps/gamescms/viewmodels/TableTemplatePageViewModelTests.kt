package com.jonpeps.gamescms.viewmodels

import com.jonpeps.gamescms.data.dataclasses.ItemType
import com.jonpeps.gamescms.data.dataclasses.TableItemFinal
import com.jonpeps.gamescms.data.helpers.ITableTemplateGroupValidator
import com.jonpeps.gamescms.ui.tabletemplates.serialization.ISerializeTableTemplateHelpers
import com.jonpeps.gamescms.ui.tabletemplates.viewmodels.TableTemplatePageViewModel
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TableTemplatePageViewModelTests {
    @MockK
    private lateinit var tableTemplateGroupValidator: ITableTemplateGroupValidator
    @MockK
    private lateinit var serializeTableTemplateHelpers: ISerializeTableTemplateHelpers

    private lateinit var sut: TableTemplatePageViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        sut = TableTemplatePageViewModel(
            tableTemplateGroupValidator, serializeTableTemplateHelpers)
    }

    @Test
    fun `set row name SUCCESS WHEN ROW IS NOT EMPTY OR DUPLICATE`() {
        every { tableTemplateGroupValidator.validateNameIsNotEmpty(any()) } returns true
        every { tableTemplateGroupValidator.validateNameIsNotDuplicate(any(), any()) } returns true

        sut.clearItems()
        sut.setItems(listOf(TableItemFinal("test1", isPrimary = true, isSortKey = true,
            value = "test", editable = true, dataType = ItemType.BOOLEAN)))
        sut.setRowName("test2")
        val item = sut.getCurrentPage()
        assert(item.name == "test2")
        assert(!sut.isDuplicateName.value)
        assert(sut.rowNameIsNotEmpty.value)
    }

    @Test
    fun `set row name FAILURE WHEN ROW IS EMPTY`() {
        every { tableTemplateGroupValidator.validateNameIsNotEmpty(any()) } returns false
        every { tableTemplateGroupValidator.validateNameIsNotDuplicate(any(), any()) } returns true

        sut.clearItems()
        sut.setItems(listOf(TableItemFinal("test1", isPrimary = true, isSortKey = true,
            value = "test", editable = true, dataType = ItemType.BOOLEAN)))
        sut.setRowName("")
        val item = sut.getCurrentPage()
        assert(item.name == "")
        assert(!sut.isDuplicateName.value)
        assert(!sut.rowNameIsNotEmpty.value)
    }

    @Test
    fun `set row name FAILURE WHEN ROW IS DUPLICATE`() {
        every { tableTemplateGroupValidator.validateNameIsNotEmpty(any()) } returns true
        every { tableTemplateGroupValidator.validateNameIsNotDuplicate(any(), any()) } returns false

        sut.clearItems()
        sut.setItems(listOf(TableItemFinal("test1", isPrimary = true, isSortKey = true,
            value = "test", editable = true, dataType = ItemType.BOOLEAN),
            TableItemFinal("test2", isPrimary = true, isSortKey = true,
                value = "test", editable = true, dataType = ItemType.BOOLEAN)))
        sut.setRowName("test1")
        val item = sut.getCurrentPage()
        assert(item.name == "test1")
        assert(sut.isDuplicateName.value)
        assert(sut.rowNameIsNotEmpty.value)
    }

    @Test
    fun `set row data type`() {
        every { serializeTableTemplateHelpers.validateTableTemplateValue(any(), any()) } returns true

        sut.clearItems()
        sut.setItems(listOf(TableItemFinal("test1", isPrimary = true, isSortKey = true,
            value = "test", editable = true, dataType = ItemType.BOOLEAN)))
        var item = sut.getCurrentPage()
        assert(item.dataType == ItemType.BOOLEAN)
        sut.setItemType(ItemType.STRING)
        item = sut.getCurrentPage()
        assert(item.dataType == ItemType.STRING)
    }

    @Test
    fun `set IS PRIMARY`() {
        sut.clearItems()
        sut.setItems(listOf(TableItemFinal("test1", isPrimary = false, isSortKey = true,
            value = "test", editable = true, dataType = ItemType.STRING)))
        sut.setPrimary(true)
        val item = sut.getCurrentPage()
        assert(item.isPrimary)
        assert(sut.primaryKeyFound.value)
    }

    @Test
    fun `set IS NOT PRIMARY AND no other rows set AS PRIMARY`() {
        sut.clearItems()
        sut.setItems(listOf(TableItemFinal("test1", isPrimary = false, isSortKey = true,
            value = "test", editable = true, dataType = ItemType.STRING)))
        sut.setPrimary(false)
        val item = sut.getCurrentPage()
        assert(!item.isPrimary)
        assert(!sut.primaryKeyFound.value)
    }

    @Test
    fun `set IS NOT PRIMARY with OTHER row set AS PRIMARY`() {
        sut.clearItems()
        sut.setItems(listOf(TableItemFinal("test1", isPrimary = true, isSortKey = true,
            value = "test", editable = true, dataType = ItemType.STRING),
            TableItemFinal("test2", isPrimary = true, isSortKey = true,
                value = "test", editable = true, dataType = ItemType.STRING)))
        sut.setIndex(0)
        sut.setPrimary(false)
        assert(sut.primaryKeyFound.value)
    }

    @Test
    fun `set IS SORT KEY`() {
        sut.clearItems()
        sut.setItems(listOf(TableItemFinal("test1", isPrimary = true, isSortKey = false,
            value = "test", editable = true, dataType = ItemType.STRING)))
        sut.setSortKey(true)
        val item = sut.getCurrentPage()
        assert(item.isSortKey)
        assert(sut.sortKeyFound.value)
    }

    @Test
    fun `set IS NOT SORT KEY WITH no other rows set as SORT KEY`() {
        sut.clearItems()
        sut.setItems(listOf(TableItemFinal("test1", isPrimary = true, isSortKey = true,
            value = "test", editable = true, dataType = ItemType.STRING)))
        sut.setSortKey(false)
        val item = sut.getCurrentPage()
        assert(!item.isSortKey)
        assert(!sut.sortKeyFound.value)
    }

    @Test
    fun `set IS NOT SORT KEY WITH other row set as SORT KEY`() {
        sut.clearItems()
        sut.setItems(listOf(TableItemFinal("test1", isPrimary = true, isSortKey = false,
            value = "test", editable = true, dataType = ItemType.STRING),
            TableItemFinal("test2", isPrimary = false, isSortKey = true,
                value = "test2", editable = true, dataType = ItemType.STRING)))
        sut.setIndex(0)
        sut.setSortKey(false)
        assert(sut.sortKeyFound.value)
    }

    @Test
    fun `test determineIfParseValueError with NO PARSE ERROR`() {
        every { serializeTableTemplateHelpers.validateTableTemplateValue(any(), any()) } returns true
        sut.clearItems()
        sut.setItems(listOf(TableItemFinal("test1", isPrimary = true, isSortKey = false,
            value = "true", editable = true, dataType = ItemType.BOOLEAN)))
        sut.setIndex(0)
        sut.determineIfParseValueError(ItemType.BOOLEAN)
        assert(!sut.parseValueError.value)
        assert(sut.getParseErrorMsg() == "")
    }

    @Test
    fun `test determineIfParseValueError with PARSE ERROR`() {
        every { serializeTableTemplateHelpers.validateTableTemplateValue(any(), any()) } returns false
        sut.clearItems()
        sut.setItems(listOf(TableItemFinal("test1", isPrimary = true, isSortKey = false,
            value = "Not a boolean", editable = true, dataType = ItemType.BOOLEAN)))
        sut.setIndex(0)
        sut.determineIfParseValueError(ItemType.BOOLEAN)
        assert(sut.parseValueError.value)
        assert(sut.getParseErrorMsg() == "Value is not a Boolean")
    }
}