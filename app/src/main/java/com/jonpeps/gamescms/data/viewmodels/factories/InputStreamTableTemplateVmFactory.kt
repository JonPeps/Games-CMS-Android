package com.jonpeps.gamescms.data.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jonpeps.gamescms.data.repositories.IMoshiTableTemplateRepository
import com.jonpeps.gamescms.data.viewmodels.InputStreamTableTemplateVm
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import java.io.InputStream

@Suppress("UNCHECKED_CAST")
class InputStreamTableTemplateVmFactory {
    @AssistedFactory
    interface IInputStreamTableTemplateVmFactory {
        fun create(@Assisted("param1") inputStream: InputStream,
                   @Assisted("param2") directory: String,
                   @Assisted("param3") moshiTableTemplateRepository: IMoshiTableTemplateRepository)
        : InputStreamTableTemplateVm
    }

    companion object {
        fun provide(
            inputStream: InputStream,
            directory: String,
            moshiTableTemplateRepository: IMoshiTableTemplateRepository,
            assistedFactory: IInputStreamTableTemplateVmFactory
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(inputStream, directory, moshiTableTemplateRepository) as T
            }
        }
    }
}
