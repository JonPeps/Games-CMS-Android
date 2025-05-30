package com.jonpeps.gamescms.ui.tabletemplates.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jonpeps.gamescms.ui.tabletemplates.viewmodels.TableTemplateListViewModel
import dagger.assisted.AssistedFactory

@Suppress("UNCHECKED_CAST")
class ListViewModelFactory {
    @AssistedFactory
    interface IListViewModelFactory {
        fun create(listPath: String): TableTemplateListViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: IListViewModelFactory,
            listPath: String
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(listPath) as T
            }
        }
    }
}