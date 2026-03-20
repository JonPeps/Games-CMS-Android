package com.jonpeps.gamescms.ui.tabletemplates.serialization

import com.jonpeps.gamescms.data.dataclasses.TableItemFinal
import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateItemListMoshi
import com.jonpeps.gamescms.data.repositories.IMoshiTableTemplateRepository
import com.jonpeps.gamescms.data.serialization.GenericRepoLoader
import com.jonpeps.gamescms.data.serialization.ICommonSerializationRepoHelper
import com.jonpeps.gamescms.ui.tabletemplates.viewmodels.ITableTemplateGroupVmChangesCache
import javax.inject.Inject

class TableTemplateLoader
@Inject constructor(repository: IMoshiTableTemplateRepository,
                    repoHelper: ICommonSerializationRepoHelper,
                    cache: ITableTemplateGroupVmChangesCache
)
    : GenericRepoLoader<TableTemplateItemListMoshi, List<TableItemFinal>>(
        repository,
        repoHelper,
        cache
    ) {

    override fun getParsedValue(item: TableTemplateItemListMoshi)
    : List<TableItemFinal>? {
        val arrayList = arrayListOf<TableItemFinal>()
        item.items.forEach {
            arrayList.add(
                TableItemFinal(
                    it.name,
                    it.dataType,
                    it.isPrimary,
                    it.value,
                    it.editable,
                    it.isSortKey
                )
            )
        }
        return arrayList
    }
}