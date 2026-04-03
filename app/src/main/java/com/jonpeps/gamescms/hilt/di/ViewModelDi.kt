package com.jonpeps.gamescms.hilt.di

import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateDetailsListMoshi
import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateItemListMoshi
import com.jonpeps.gamescms.data.repositories.base.ISingleItemMoshiJsonRepository
import com.jonpeps.gamescms.data.serialization.ICommonSerializationRepoHelper
import com.jonpeps.gamescms.data.serialization.moshi.ISToJsonTypeToStorage
import com.jonpeps.gamescms.ui.tabletemplates.viewmodels.ISerializeTableTemplatesViewModel
import com.jonpeps.gamescms.ui.viewmodels.dynamodb.CreateTableViewModel
import com.jonpeps.gamescms.ui.tabletemplates.viewmodels.ITableTemplateGroupVmChangesCache
import com.jonpeps.gamescms.ui.tabletemplates.viewmodels.SerializeTableTemplatesViewModel
import com.jonpeps.gamescms.ui.tabletemplates.viewmodels.TableTemplateGroupVmChangesCache
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
                                                inputStreamTableTemplateStatus: ISToJsonTypeToStorage<TableTemplateDetailsListMoshi>,
                                                moshiTableTemplateRepository: ISingleItemMoshiJsonRepository<TableTemplateItemListMoshi>,
                                                moshiTableTemplateDetailsListRepository: ISingleItemMoshiJsonRepository<TableTemplateDetailsListMoshi>,
                                                commonSerializationRepoHelper: ICommonSerializationRepoHelper
    )
    : ISerializeTableTemplatesViewModel {
        return SerializeTableTemplatesViewModel(
            coroutineDispatcher,
            inputStreamTableTemplateStatus,
            moshiTableTemplateRepository,
            moshiTableTemplateDetailsListRepository,
            commonSerializationRepoHelper)
    }

    @Provides
    fun provideTableTemplateGroupVmChangesCache(): ITableTemplateGroupVmChangesCache {
        return TableTemplateGroupVmChangesCache()
    }
}