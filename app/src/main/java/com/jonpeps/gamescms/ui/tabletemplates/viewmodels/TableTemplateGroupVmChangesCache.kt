package com.jonpeps.gamescms.ui.tabletemplates.viewmodels

import com.jonpeps.gamescms.data.dataclasses.TableItemFinal
import com.jonpeps.gamescms.data.helpers.GenericSerializationChangesCache
import com.jonpeps.gamescms.data.helpers.IGenericSerializationCache
import javax.inject.Inject

interface ITableTemplateGroupVmChangesCache : IGenericSerializationCache<ArrayList<TableItemFinal>>

class TableTemplateGroupVmChangesCache@Inject constructor() : GenericSerializationChangesCache<ArrayList<TableItemFinal>>(),
    ITableTemplateGroupVmChangesCache {
    override var initialItem: ArrayList<TableItemFinal> = arrayListOf()
    override var currentItem: ArrayList<TableItemFinal> = arrayListOf()

    override fun deepCopy(item: ArrayList<TableItemFinal>): ArrayList<TableItemFinal> {
            val arrayList = arrayListOf<TableItemFinal>()
            item.forEach {
                arrayList.add(
                    TableItemFinal(it.name,
                        it.dataType,
                        it.isPrimary,
                        it.value,
                        it.editable,
                        it.isSortKey))
            }
            return arrayList
        }

    override fun equalsOther(item: ArrayList<TableItemFinal>): Boolean {
        return initialItem == item
    }
}