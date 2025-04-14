package com.jonpeps.gamescms.viewmodels

import com.jonpeps.gamescms.data.dataclasses.ItemType
import com.jonpeps.gamescms.data.dataclasses.TableItemFinal
import com.jonpeps.gamescms.ui.createtable.viewmodels.IGlobalWatchCoreValuesChangedListener
import com.jonpeps.gamescms.ui.createtable.viewmodels.TableTemplateSingleItem
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class TableTemplateSingleItemTests {
    @Mock
    private lateinit var listener: IGlobalWatchCoreValuesChangedListener
    private lateinit var tableTemplateSingleItem: TableTemplateSingleItem
    private lateinit var item: TableItemFinal

    @Before
    fun setup() {
        item = TableItemFinal("test", ItemType.STRING)
        tableTemplateSingleItem = TableTemplateSingleItem(listener, item)
    }

    @Test
    fun `set row name on viewModel when name is empty`() {
        tableTemplateSingleItem.setRowName("")
        assert(tableTemplateSingleItem.rowNameEmpty.value)
    }

    @Test
    fun `set row name on viewModel when name is not empty`() {
        tableTemplateSingleItem.setRowName("test")
        assert(!tableTemplateSingleItem.rowNameEmpty.value)
        assert(tableTemplateSingleItem.getItem().name == "test")
        verify(listener, times(1)).onNameChanged("test")
    }

    @Test
    fun `test set item`() {
        tableTemplateSingleItem.setItemType(ItemType.STRING)
        assert(tableTemplateSingleItem.getItem().dataType == ItemType.STRING)
    }

    @Test
    fun `set default value not empty when row is editable`() {
        tableTemplateSingleItem.setDefaultValue("test")
        assert(!tableTemplateSingleItem.noValueWithNotEditable.value)
        assert(tableTemplateSingleItem.getItem().value == "test")
    }

    @Test
    fun `set default value empty when row is not editable`() {
        tableTemplateSingleItem.setIsEditable(false)
        tableTemplateSingleItem.setDefaultValue("")
        assert(tableTemplateSingleItem.noValueWithNotEditable.value)
        assert(tableTemplateSingleItem.getItem().value == "")
    }

    @Test
    fun `set primary key`() {
        tableTemplateSingleItem.setPrimary(true)
        assert(tableTemplateSingleItem.getItem().isPrimary)
        verify(listener, times(1)).isPrimaryChanged(true)
    }

    @Test
    fun `set sort key`() {
        tableTemplateSingleItem.setSortKey(true)
        assert(tableTemplateSingleItem.getItem().isSortKey)
        verify(listener, times(1)).isSortKeyChanged(true)
    }

    @Test
    fun `set editable`() {
        tableTemplateSingleItem.setIsEditable(true)
        assert(tableTemplateSingleItem.getItem().editable)
        assert(!tableTemplateSingleItem.noValueWithNotEditable.value)
    }
}