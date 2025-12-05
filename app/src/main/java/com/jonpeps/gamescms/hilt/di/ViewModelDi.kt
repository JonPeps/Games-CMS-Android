package com.jonpeps.gamescms.hilt.di

import com.jonpeps.gamescms.data.repositories.MoshiTableTemplateStatusListRepository
import com.jonpeps.gamescms.ui.tabletemplates.serialization.SerializeTableTemplates
import com.jonpeps.gamescms.ui.tabletemplates.viewmodels.ISerializeTableTemplatesViewModel
import com.jonpeps.gamescms.ui.viewmodels.dynamodb.CreateTableViewModel
import com.jonpeps.gamescms.ui.tabletemplates.viewmodels.ITableTemplateGroupVmChangesCache
import com.jonpeps.gamescms.ui.tabletemplates.viewmodels.SerializeTableTemplatesViewModel
import com.jonpeps.gamescms.ui.tabletemplates.viewmodels.TableTemplateGroupVmChangesCache
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.CoroutineDispatcher
import software.amazon.awssdk.services.dynamodb.DynamoDbClient

@Module
@InstallIn(ViewModelComponent::class)
class ViewModelDiProvider {
    @Provides
    fun provideCreateTableViewModel(dynamoDbClient: DynamoDbClient, dispatcher: CoroutineDispatcher
    ): CreateTableViewModel {
        return CreateTableViewModel(dynamoDbClient, dispatcher)
    }

    @Provides
    fun provideSerializeTableTemplatesViewModel(coroutineDispatcher: CoroutineDispatcher,
                                                serializeTableTemplates: SerializeTableTemplates,
                                                moshiTableTemplateStatusListRepository: MoshiTableTemplateStatusListRepository
    )
    : ISerializeTableTemplatesViewModel {
        return SerializeTableTemplatesViewModel(
            coroutineDispatcher,
            serializeTableTemplates,
            moshiTableTemplateStatusListRepository)
        }
}

@Module
@InstallIn(ViewModelComponent::class)
abstract class ViewModelHelperDiBindings {
    @Binds
    abstract fun bindTableTemplateGroupVmChangesCache(
        tableTemplateGroupVmChangesCacheImpl: TableTemplateGroupVmChangesCache
    ): ITableTemplateGroupVmChangesCache
}