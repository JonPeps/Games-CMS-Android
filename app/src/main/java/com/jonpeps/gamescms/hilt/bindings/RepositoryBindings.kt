package com.jonpeps.gamescms.hilt.bindings

import com.jonpeps.gamescms.data.io.ISerializeString
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryBindings {
    @Binds
    @Singleton
    abstract fun provideSerializeString(serializeString: ISerializeString): ISerializeString
}