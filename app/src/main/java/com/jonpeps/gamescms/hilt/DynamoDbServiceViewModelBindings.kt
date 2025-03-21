package com.jonpeps.gamescms.hilt

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DynamoDbServiceViewModelBindings {
    @Singleton
    @Binds
    fun bindDispatcher(dispatcher: CoroutineDispatcher): CoroutineDispatcher = Dispatchers.IO
}