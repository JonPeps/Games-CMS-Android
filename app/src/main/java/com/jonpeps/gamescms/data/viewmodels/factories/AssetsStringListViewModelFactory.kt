package com.jonpeps.gamescms.data.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jonpeps.gamescms.data.viewmodels.AssetsStringListViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory

@Suppress("UNCHECKED_CAST")
class AssetsStringListViewModelFactory {
    @AssistedFactory
    interface IAssetsStringListViewModelFactory {
        fun create(@Assisted("param2") listPath: String): AssetsStringListViewModel
    }

    companion object {
        fun provideFactory(
            stringListPath: String,
            assistedFactory: IAssetsStringListViewModelFactory
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(stringListPath) as T
            }
        }
    }
}