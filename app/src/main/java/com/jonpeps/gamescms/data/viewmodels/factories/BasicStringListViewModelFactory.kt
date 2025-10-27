package com.jonpeps.gamescms.data.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jonpeps.gamescms.data.viewmodels.BasicStringListViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory

@Suppress("UNCHECKED_CAST")
class BasicStringListViewModelFactory {
    @AssistedFactory
    interface IBasicStringListViewModelFactory {
        fun create(@Assisted("param1") directoryPath: String, @Assisted("param2") listPath: String): BasicStringListViewModel
    }

    companion object {
        fun provide(
            directoryPath: String,
            stringListPath: String,
            assistedFactory: IBasicStringListViewModelFactory
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(directoryPath, stringListPath) as T
            }
        }
    }
}