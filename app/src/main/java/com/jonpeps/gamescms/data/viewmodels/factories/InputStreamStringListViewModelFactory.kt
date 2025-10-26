package com.jonpeps.gamescms.data.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jonpeps.gamescms.data.viewmodels.InputStreamStringListViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import java.io.InputStream

@Suppress("UNCHECKED_CAST")
class InputStreamStringListViewModelFactory {
    @AssistedFactory
    interface IInputStreamToStringListVmFactory {
        fun create(@Assisted("param1") inputStream: InputStream,
                   @Assisted("param2") directory: String
        ): InputStreamStringListViewModel
    }

    companion object {
        fun provide(
            inputStream: InputStream,
            directory: String,
            assistedFactory: IInputStreamToStringListVmFactory
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(inputStream, directory) as T
            }
        }
    }
}