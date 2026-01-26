package com.jonpeps.gamescms.data

import com.jonpeps.gamescms.data.dataclasses.ItemType
import com.jonpeps.gamescms.data.dataclasses.TableItemFinal
import com.jonpeps.gamescms.data.helpers.TableTemplateGroupValidator
import com.jonpeps.gamescms.ui.tabletemplates.serialization.ISerializeTableTemplateHelpers
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Test

class TableTemplateGroupValidatorTests {
    @MockK
    private lateinit var mockSerializeTableTemplateHelpers: ISerializeTableTemplateHelpers

    private lateinit var sut: TableTemplateGroupValidator

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        sut = TableTemplateGroupValidator(mockSerializeTableTemplateHelpers)
    }

    @Test
    fun `validatePrimaryKey SUCCESS`() {
        val items = listOf(TableItemFinal("test", isPrimary = true,
            isSortKey = true, value = "test", editable = true,
            dataType = ItemType.STRING))
        assert(sut.validatePrimaryKey(items))
    }

    @Test
    fun `validatePrimaryKey FAILURE`() {
        val items = listOf(TableItemFinal("test", isPrimary = false,
            isSortKey = true, value = "test", editable = true,
            dataType = ItemType.STRING))
        assert(!sut.validatePrimaryKey(items))
    }

    @Test
    fun `validateSortKey SUCCESS`() {
        val items = listOf(TableItemFinal("test", isPrimary = true,
            isSortKey = true, value = "test", editable = true,
            dataType = ItemType.STRING))
        assert(sut.validateSortKey(items))
    }

    @Test
    fun `validateSortKey FAILURE`() {
        val items = listOf(TableItemFinal("test", isPrimary = true,
            isSortKey = false, value = "test", editable = true,
            dataType = ItemType.STRING))
        assert(!sut.validateSortKey(items))
    }

    @Test
    fun `validateValue SUCCESS`() {
        every { mockSerializeTableTemplateHelpers.validateTableTemplateValue(any(), any()) } returns true
        val item = TableItemFinal("test", isPrimary = true,
            isSortKey = true, value = "true", editable = true,
            dataType = ItemType.BOOLEAN)
        assert(sut.validateValue(item))
        assert(sut.getParseValueErrorMsg() == "")
    }

    @Test
    fun `validateValue FAILURE`() {
        every { mockSerializeTableTemplateHelpers.validateTableTemplateValue(any(), any()) } returns false
        val item = TableItemFinal("test", isPrimary = true,
            isSortKey = true, value = "test", editable = true,
            dataType = ItemType.BOOLEAN)
        assert(!sut.validateValue(item))
        assert(sut.getParseValueErrorMsg() == "Value is not a Boolean")
    }

    @Test
    fun `validateName SUCCESS when NO DUPLICATE NAME`() {
        val items = listOf(TableItemFinal("test1", isPrimary = true,
            isSortKey = true, value = "test", editable = true,
            dataType = ItemType.BOOLEAN), TableItemFinal("test2", isPrimary = true,
            isSortKey = true, value = "test", editable = true,
            dataType = ItemType.BOOLEAN))
        assert(sut.validateNameIsNotDuplicate("test3", items))
    }

    @Test
    fun `validateName SUCCESS WHEN NAME IS NOT EMPTY`() {
        assert(sut.validateNameIsNotEmpty("test1"))
    }

    @Test
    fun `validateName FAILURE WHEN NAME IS EMPTY`() {
        assert(!sut.validateNameIsNotEmpty(""))
    }

    @Test
    fun `validateEditable SUCCESS WHEN IS EDITABLE AND VALUE IS NOT EMPTY`() {
        val item = TableItemFinal("test", isPrimary = true,
            isSortKey = true, value = "value", editable = true,
            dataType = ItemType.BOOLEAN)
        assert(sut.validateEditable(item))
    }

    @Test
    fun `validateEditable FAILURE WHEN IS EDITABLE AND VALUE IS NOT EMPTY`() {
        val item = TableItemFinal("test", isPrimary = true,
            isSortKey = true, value = "", editable = true,
            dataType = ItemType.BOOLEAN)
        assert(!sut.validateEditable(item))
    }

    @Test
    fun `validateEditable FAILURE WHEN IS NOT EDITABLE AND VALUE IS EMPTY`() {
        val item = TableItemFinal("test", isPrimary = true,
            isSortKey = true, value = "", editable = false,
            dataType = ItemType.BOOLEAN)
        assert(!sut.validateEditable(item))
    }
}