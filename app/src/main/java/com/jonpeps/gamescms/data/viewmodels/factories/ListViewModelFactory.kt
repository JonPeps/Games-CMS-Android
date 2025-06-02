package com.jonpeps.gamescms.data.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jonpeps.gamescms.data.viewmodels.CommonStringListViewModel
import dagger.assisted.AssistedFactory

@Suppress("UNCHECKED_CAST")
class ListViewModelFactory {
    @AssistedFactory
    interface ICommonStringListViewModelFactory {
        fun create(listPath: String): CommonStringListViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: ICommonStringListViewModelFactory,
            listPath: String
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(listPath) as T
            }
        }
    }
}