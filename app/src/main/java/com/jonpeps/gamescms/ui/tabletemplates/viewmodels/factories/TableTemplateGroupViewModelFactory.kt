package com.jonpeps.gamescms.ui.tabletemplates.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jonpeps.gamescms.ui.tabletemplates.viewmodels.TableTemplateGroupViewModel
import dagger.assisted.AssistedFactory

@Suppress("UNCHECKED_CAST")
class TableTemplateGroupViewModelFactory {
    @AssistedFactory
    interface ITableTemplateGroupViewModelFactory {
        fun create(tableTemplateFilesPath: String): TableTemplateGroupViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: ITableTemplateGroupViewModelFactory,
            tableTemplateFilesPath: String
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(tableTemplateFilesPath) as T
            }
        }
    }
}

/*
@InstallIn(FragmentComponent::class)
@AssistedModule
@Module
interface AssistedInjectModule {}

@AndroidEntryPoint
class ItemFragment : Fragment() {
    private val args: ItemScreenFragmentArgs by navArgs()
    @Inject lateinit var itemViewModelAssistedFactory: ItemViewModel.AssistedFactory
    private val itemViewModel: ItemViewModel by viewModels {
            ItemViewModel.provideFactory(itemViewModelAssistedFactory, args.itemId)
    }
}
*/