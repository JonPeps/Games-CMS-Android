package com.jonpeps.gamescms.hilt.bindings

import com.jonpeps.gamescms.ui.createtable.viewmodels.ITableTemplateGroupViewModel
import com.jonpeps.gamescms.ui.createtable.viewmodels.ITableTemplateSingleItemViewModel
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ViewModelBindings {
    @Binds
    @ActivityScoped
    abstract fun bindCreateTableTemplateViewModel(viewModel: ITableTemplateSingleItemViewModel): ITableTemplateSingleItemViewModel

    @Binds
    @ActivityScoped
    abstract fun bindCreateTableTemplatePageViewModel(viewModel: ITableTemplateGroupViewModel): ITableTemplateGroupViewModel
}