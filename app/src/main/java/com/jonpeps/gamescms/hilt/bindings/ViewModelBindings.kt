package com.jonpeps.gamescms.hilt.bindings

import com.jonpeps.gamescms.ui.createtable.viewmodels.ICreateTableTemplateBridgeVm
import com.jonpeps.gamescms.ui.createtable.viewmodels.ICreateTableTemplatePageViewModel
import com.jonpeps.gamescms.ui.createtable.viewmodels.ICreateTableTemplateViewModel
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
    abstract fun bindCreateTableTemplateBridgeVm(viewModel: ICreateTableTemplateBridgeVm): ICreateTableTemplateBridgeVm

    @Binds
    @ActivityScoped
    abstract fun bindCreateTableTemplateViewModel(viewModel: ICreateTableTemplateViewModel): ICreateTableTemplateViewModel

    @Binds
    @ActivityScoped
    abstract fun bindCreateTableTemplatePageViewModel(viewModel: ICreateTableTemplatePageViewModel): ICreateTableTemplatePageViewModel
}