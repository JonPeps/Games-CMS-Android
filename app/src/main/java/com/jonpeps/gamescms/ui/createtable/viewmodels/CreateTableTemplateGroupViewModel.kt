package com.jonpeps.gamescms.ui.createtable.viewmodels

import com.jonpeps.gamescms.data.dataclasses.TableItem
import com.jonpeps.gamescms.data.serialization.TableItemListMoshiSerialization
import com.jonpeps.gamescms.ui.createtable.viewmodels.data.CreateTableTemplatePageErrorType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

data class CreateTableTemplateStatus(
    var success: Boolean,
    var items: ArrayList<TableItem>,
    var message: String?,
    var ex: Exception?)

interface ICreateTableTemplateGroupViewModel : IOnTemplateTablePageValuesChangedListener {
    fun loadTemplate(fileName: String)
    fun newTemplate()
    fun saveTemplate(fileName: String)
    fun addNewPage()
    fun removePage()
    fun nextPage()
    fun previousPage()
    fun pageCount(): Int
}

class CreateTableTemplateGroupViewModel
@Inject constructor(private val serialization: TableItemListMoshiSerialization)
    : BaseCreateTableTemplatesVm<CreateTableTemplatePageErrorType>(), ICreateTableTemplateGroupViewModel {

    private val _status = MutableStateFlow(CreateTableTemplateStatus(true, arrayListOf(), null, null))
    var status: StateFlow<CreateTableTemplateStatus> = _status

    private var index = 0
    private var rowId = 0

    override fun loadTemplate(fileName: String) {
        TODO("Not yet implemented")
    }

    override fun newTemplate() {
        addNewPage()
    }

    override fun saveTemplate(fileName: String) {
        TODO("Not yet implemented")
    }

    override fun addNewPage() {
        val item = TableItem()
        index++
        _status.value.items.add(index, item)
    }

    override fun removePage() {
        if (decrementPage()) {
            _status.value.items.removeAt(index + 1)
        }
    }

    override fun nextPage() {
        if (index ==  _status.value.items.size - 1) return
        index++
    }

    override fun previousPage() {
        decrementPage()
    }

    override fun pageCount() = _status.value.items.size

    override fun onNameChanged(name: String) {
        removeError(CreateTableTemplatePageErrorType.ROW_NAME_EXISTS)
        _status.value.items.forEach {
            if (it.name == name) {
                addError(CreateTableTemplatePageErrorType.ROW_NAME_EXISTS)
                return
            }
        }
    }

    override fun isPrimaryChanged(isPrimary: Boolean) {
        removeError(CreateTableTemplatePageErrorType.NO_PRIMARY_KEY)
        _status.value.items.forEach {
            if (it.isPrimary) {
                return
            }
        }
        addError(CreateTableTemplatePageErrorType.NO_PRIMARY_KEY)
    }

    override fun isSortKeyChanged(isSort: Boolean) {
        removeError(CreateTableTemplatePageErrorType.NO_SORT_KEY)
        _status.value.items.forEach {
            if (it.isSortKey) {
                return
            }
        }
        addError(CreateTableTemplatePageErrorType.NO_SORT_KEY)
    }

    private fun decrementPage(): Boolean {
        if (index == 0) return false
        index--
        return true
    }
}