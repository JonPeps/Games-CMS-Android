package com.jonpeps.gamescms.data.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jonpeps.gamescms.data.repositories.IMoshiStringListRepository
import com.jonpeps.gamescms.data.viewmodels.InputStreamStringListViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import java.io.InputStream

@Suppress("UNCHECKED_CAST")
class InputStreamStringListViewModelFactory {
    @AssistedFactory
    interface IInputStreamToJsonViewModelFactory {
        fun createStringListVm(@Assisted("param1") inputStream: InputStream,
                               @Assisted("param2") directory: String,
                               @Assisted("param3") moshiStringListRepository: IMoshiStringListRepository
        ): InputStreamStringListViewModel
    }

    companion object {
        fun provideStringListVmFactory(
            inputStream: InputStream,
            directory: String,
            moshiStringListRepository: IMoshiStringListRepository,
            assistedFactory: IInputStreamToJsonViewModelFactory
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.createStringListVm(inputStream, directory, moshiStringListRepository) as T
            }
        }
    }
}