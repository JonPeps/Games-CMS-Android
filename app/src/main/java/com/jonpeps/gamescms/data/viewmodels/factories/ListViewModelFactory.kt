package com.jonpeps.gamescms.data.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jonpeps.gamescms.data.viewmodels.StartFlowStringListViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory

@Suppress("UNCHECKED_CAST")
class ListViewModelFactory {
    @AssistedFactory
    interface ICommonStringListViewModelFactory {
        fun create(@Assisted("param1") directoryPath: String, @Assisted("param2") listPath: String): StartFlowStringListViewModel
    }

    companion object {
        fun provideFactory(
            directoryPath: String,
            stringListPath: String,
            assistedFactory: ICommonStringListViewModelFactory
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(directoryPath, stringListPath) as T
            }
        }
    }
}