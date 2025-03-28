package com.jonpeps.gamescms.hilt.bindings

import com.jonpeps.gamescms.data.core.IMoshiJsonAdapterCreator
import com.jonpeps.gamescms.data.core.IStringSerialization
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
    abstract fun provideSerializeString(serializeString: IStringSerialization): IStringSerialization

    @Binds
    @Singleton
    abstract fun provideMoshiJsonAdapterCreator(moshiJsonAdapterCreator: IMoshiJsonAdapterCreator): IMoshiJsonAdapterCreator
}