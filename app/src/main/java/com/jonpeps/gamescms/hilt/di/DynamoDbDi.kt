package com.jonpeps.gamescms.hilt.di

import com.jonpeps.gamescms.dynamodb.services.DynamoDbCreateTable
import com.jonpeps.gamescms.dynamodb.services.IDynamoDbCreateTable
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DynamoDbDiProvidings {
    @Provides
    fun provideDynamoDbCreateTable(): IDynamoDbCreateTable {
        return DynamoDbCreateTable()
    }
}

