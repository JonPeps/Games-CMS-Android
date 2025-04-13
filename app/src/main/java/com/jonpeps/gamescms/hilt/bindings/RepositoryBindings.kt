package com.jonpeps.gamescms.hilt.bindings

import com.jonpeps.gamescms.data.repositories.IMoshiStringListRepository
import com.jonpeps.gamescms.ui.tabletemplates.repositories.ITableTemplateFileRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryBindings {
    @Binds
    @Singleton
    abstract fun provideMoshiStringListRepository(repository: IMoshiStringListRepository): IMoshiStringListRepository

    @Binds
    @Singleton
    abstract fun provideTableTemplateRepository(repository: ITableTemplateFileRepository): ITableTemplateFileRepository
}