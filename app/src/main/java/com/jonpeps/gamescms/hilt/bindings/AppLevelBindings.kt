package com.jonpeps.gamescms.hilt.bindings

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppLevelBindings {
    @Binds
    @Singleton
    abstract fun provideApplicationContext(context: ApplicationContext): ApplicationContext
}