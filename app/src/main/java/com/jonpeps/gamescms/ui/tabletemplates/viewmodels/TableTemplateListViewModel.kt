package com.jonpeps.gamescms.ui.tabletemplates.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonpeps.gamescms.ui.tabletemplates.viewmodels.factories.ListViewModelFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class TableTemplateListStatus(
    val success: Boolean,
    val items: ArrayList<String>,
    val message: String?,
    val ex: Exception?)

interface ITableTemplateListViewModel {
    fun load(name: String, loadFromCacheIfExists: Boolean = true)
    fun add(name: String)
    fun delete(name: String)
}

@HiltViewModel(assistedFactory = ListViewModelFactory.IListViewModelFactory::class)
class TableTemplateListViewModel
@AssistedInject constructor(
    @Assisted private val tableTemplateListsPath: String,
    private val coroutineDispatcher: CoroutineDispatcher
): ViewModel(), ITableTemplateListViewModel {
    private val _status = MutableStateFlow(TableTemplateListStatus(true, arrayListOf(), "", null))
    val status: StateFlow<TableTemplateListStatus> = _status

    private var _isProcessing = MutableStateFlow(false)
    val isProcessing: StateFlow<Boolean> = _isProcessing

    override fun load(name: String, loadFromCacheIfExists: Boolean) {
        viewModelScope.launch(coroutineDispatcher) {
            _isProcessing.value = true

        }
    }

    override fun add(name: String) {
        TODO("Not yet implemented")
    }

    override fun delete(name: String) {
        TODO("Not yet implemented")
    }
}