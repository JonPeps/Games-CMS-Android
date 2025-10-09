package com.jonpeps.gamescms.data.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jonpeps.gamescms.data.serialization.ICommonSerializationRepoHelper
import com.jonpeps.gamescms.data.serialization.debug.IInputStreamSerializationRepoHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import java.io.InputStream

@Suppress("UNCHECKED_CAST")
class JsonAssetsFileToInternalStorageVmFactory {
    @AssistedFactory
    interface IJsonAssetsFileToInternalStorageVm {
        fun create(@Assisted("param1") inputStream: InputStream,
                   @Assisted("param2") directory: String): JsonAssetsFileToInternalStorageVm
    }

    companion object {
        fun provideFactory(
            inputStream: InputStream,
            directory: String,
            assistedFactory: IJsonAssetsFileToInternalStorageVm
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(inputStream, directory) as T
            }
        }
    }
}

@HiltViewModel(assistedFactory = JsonAssetsFileToInternalStorageVmFactory.IJsonAssetsFileToInternalStorageVm::class)
class JsonAssetsFileToInternalStorageVm @AssistedInject constructor(
    @Assisted("param1") private val inputStream: InputStream,
    @Assisted("param2") private val directory: String,
    private val commonSerializationRepoHelper: ICommonSerializationRepoHelper,
    private val inputStreamSerializationRepoHelper: IInputStreamSerializationRepoHelper,
    private val coroutineDispatcher: CoroutineDispatcher): ViewModel() {

    fun serialize(): Boolean {
                return true
    }
}