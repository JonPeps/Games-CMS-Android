package com.jonpeps.gamescms.viewmodels

import com.jonpeps.gamescms.data.dataclasses.ItemType
import com.jonpeps.gamescms.ui.createtable.viewmodels.IGlobalWatchCoreValuesChangedListener
import com.jonpeps.gamescms.ui.createtable.viewmodels.TableTemplateSingleItemViewModel
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class TableTemplateSingleItemViewModelTests {
    @Mock
    private lateinit var listener: IGlobalWatchCoreValuesChangedListener

    private lateinit var viewModel: TableTemplateSingleItemViewModel

    @Before
    fun setup() {
        viewModel = TableTemplateSingleItemViewModel(listener)
    }

    @Test
    fun `set row name on viewModel when name is empty`() {
        viewModel.setRowName("")
        assert(viewModel.rowNameEmpty.value)
    }

    @Test
    fun `set row name on viewModel when name is not empty`() {
        viewModel.setRowName("test")
        assert(!viewModel.rowNameEmpty.value)
        assert(viewModel.getItem().name == "test")
        verify(listener, times(1)).onNameChanged("test")
    }

    @Test
    fun `test set item`() {
        viewModel.setItemType(ItemType.STRING)
        assert(viewModel.getItem().dataType == ItemType.STRING)
    }

    @Test
    fun `set default value not empty when row is editable`() {
        viewModel.setDefaultValue("test")
        assert(!viewModel.noValueWithNotEditable.value)
        assert(viewModel.getItem().value == "test")
    }

    @Test
    fun `set default value empty when row is not editable`() {
        viewModel.setIsEditable(false)
        viewModel.setDefaultValue("")
        assert(viewModel.noValueWithNotEditable.value)
        assert(viewModel.getItem().value == "")
    }

    @Test
    fun `set primary key`() {
        viewModel.setPrimary(true)
        assert(viewModel.getItem().isPrimary)
        verify(listener, times(1)).isPrimaryChanged(true)
    }

    @Test
    fun `set sort key`() {
        viewModel.setSortKey(true)
        assert(viewModel.getItem().isSortKey)
        verify(listener, times(1)).isSortKeyChanged(true)
    }

    @Test
    fun `set editable`() {
        viewModel.setIsEditable(true)
        assert(viewModel.getItem().editable)
        assert(!viewModel.noValueWithNotEditable.value)
    }
}