package com.jonpeps.gamescms.hilt.bindings

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
abstract class AppLevelBindings {
    @Binds
    abstract fun provideApplicationContext(context: ApplicationContext): ApplicationContext
}

@Module
@InstallIn(SingletonComponent::class)
class AppLevelProvider {
    @Provides
    fun bindDispatcher(): CoroutineDispatcher = Dispatchers.IO
}

