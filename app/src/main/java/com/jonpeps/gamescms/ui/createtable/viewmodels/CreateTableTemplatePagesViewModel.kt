package com.jonpeps.gamescms.ui.createtable.viewmodels

import com.jonpeps.gamescms.data.TableItem
import com.jonpeps.gamescms.ui.createtable.viewmodels.data.CreateTableTemplatePageErrorType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

interface ICreateTableTemplatePagesViewModel : IOnTemplateTablePageValuesChangedListener {
    fun init(items: List<TableItem>)
    fun addNew()
    fun remove()
    fun nextPage()
    fun previousPage()
    fun itemCount(): Int
}

class CreateTableTemplatePagesViewModel
@Inject constructor(private val createTableTemplatePageViewModel: CreateTableTemplatePageViewModel)
    : BaseCreateTableTemplatesVm<CreateTableTemplatePageErrorType>(), ICreateTableTemplatePagesViewModel {

    private val _items = MutableStateFlow(arrayListOf<TableItem>())
    var items: StateFlow<ArrayList<TableItem>> = _items

    private var index = 0
    private var rowId = 0

    override fun init(items: List<TableItem>) {
        _items.value = items as ArrayList<TableItem>
        createTableTemplatePageViewModel.setListener(this)
        createTableTemplatePageViewModel.populate(_items.value[index])
    }

    override fun addNew() {
        val item = TableItem()
        createTableTemplatePageViewModel.populate(item)
        index++
        _items.value.add(index, item)
    }

    override fun remove() {
        if (decrementPage()) {
            _items.value.removeAt(index + 1)
        }
    }

    override fun nextPage() {
        if (index == _items.value.size - 1) return
        index++
        createTableTemplatePageViewModel.populate(_items.value[index])
    }

    override fun previousPage() {
        decrementPage()
    }

    override fun itemCount() = _items.value.size

    override fun onNameChanged(name: String) {
        removeError(CreateTableTemplatePageErrorType.ROW_NAME_EXISTS)
        _items.value.forEach {
            if (it.name == name) {
                addError(CreateTableTemplatePageErrorType.ROW_NAME_EXISTS)
                return
            }
        }
    }

    override fun isPrimaryChanged(isPrimary: Boolean) {
        removeError(CreateTableTemplatePageErrorType.NO_PRIMARY_KEY)
        _items.value.forEach {
            if (it.isPrimary) {
                return
            }
        }
        addError(CreateTableTemplatePageErrorType.NO_PRIMARY_KEY)
    }

    override fun isSortKeyChanged(isSort: Boolean) {
        removeError(CreateTableTemplatePageErrorType.NO_SORT_KEY)
        _items.value.forEach {
            if (it.isSortKey) {
                return
            }
        }
        addError(CreateTableTemplatePageErrorType.NO_SORT_KEY)
    }

    private fun decrementPage(): Boolean {
        if (index == 0) return false
        index--
        createTableTemplatePageViewModel.populate(_items.value[index])
        return true
    }
}