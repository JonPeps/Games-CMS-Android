package com.jonpeps.gamescms.ui.createtable.viewmodels

import com.jonpeps.gamescms.data.TableItem
import com.jonpeps.gamescms.ui.createtable.viewmodels.data.CreateTableTemplatePageErrorType
import javax.inject.Inject

interface ICreateTableTemplatePageViewModel {
    fun populate(items: ArrayList<TableItem>)
    fun addNew()
    fun remove(index: Int)
    fun nextPage()
    fun previousPage()
    fun itemCount()
}

class CreateTableTemplatePageViewModel
    @Inject constructor(createTableTemplateBridgeVm: ICreateTableTemplateBridgeVm)
    : BaseCreateTableTemplateVm<CreateTableTemplatePageErrorType>(createTableTemplateBridgeVm), ICreateTableTemplatePageViewModel {

    override fun populate(items: ArrayList<TableItem>) {
        TODO("Not yet implemented")
    }

    override fun addNew() {
        TODO("Not yet implemented")
    }

    override fun remove(index: Int) {
        TODO("Not yet implemented")
    }

    override fun nextPage() {
        TODO("Not yet implemented")
    }

    override fun previousPage() {
        TODO("Not yet implemented")
    }

    override fun itemCount() {
        TODO("Not yet implemented")
    }
}