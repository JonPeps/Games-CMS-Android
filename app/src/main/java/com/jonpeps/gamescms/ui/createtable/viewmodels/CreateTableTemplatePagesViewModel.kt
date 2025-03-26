package com.jonpeps.gamescms.ui.createtable.viewmodels

import com.jonpeps.gamescms.data.TableItem
import com.jonpeps.gamescms.ui.createtable.viewmodels.data.CreateTableTemplatePageErrorType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

interface ICreateTableTemplatePagesViewModel : IOnValuesChangedListener {
    fun init(items: List<TableItem>)
    fun addNew()
    fun remove()
    fun nextPage()
    fun previousPage()
    fun itemCount(): Int
}

class CreateTableTemplatePagesViewModel
@Inject constructor(private val createTableTemplateViewModel: CreateTableTemplatePageViewModel)
    : BaseCreateTableTemplatesVm<CreateTableTemplatePageErrorType>(), ICreateTableTemplatePagesViewModel {

    private val _items = MutableStateFlow(arrayListOf<TableItem>())
    var items: StateFlow<ArrayList<TableItem>> = _items

    private var index = 0

    override fun init(items: List<TableItem>) {
        _items.value = items as ArrayList<TableItem>
    }

    override fun addNew() {
        val item = TableItem()
        createTableTemplateViewModel.populate(item)
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
        createTableTemplateViewModel.populate(_items.value[index])
    }

    override fun previousPage() {
        decrementPage()
    }

    override fun itemCount() = _items.value.size

    override fun isPrimaryChanged(isPrimary: Boolean) {
        removeError(CreateTableTemplatePageErrorType.NO_PRIMARY_KEY)
        var primaryKeyFound: Boolean
        _items.value.forEach {
            primaryKeyFound = it.isPrimary
            if (primaryKeyFound) {
                return
            }
        }
        addError(CreateTableTemplatePageErrorType.NO_PRIMARY_KEY)
    }

    override fun isSortKeyChanged(isSort: Boolean) {
        removeError(CreateTableTemplatePageErrorType.NO_SORT_KEY)
        var sortKeyFound: Boolean
        _items.value.forEach {
            sortKeyFound = it.isSortKey
            if (sortKeyFound) {
                return
            }
        }
        addError(CreateTableTemplatePageErrorType.NO_SORT_KEY)
    }

    private fun decrementPage(): Boolean {
        if (index == 0) return false
        index--
        createTableTemplateViewModel.populate(_items.value[index])
        return true
    }
}