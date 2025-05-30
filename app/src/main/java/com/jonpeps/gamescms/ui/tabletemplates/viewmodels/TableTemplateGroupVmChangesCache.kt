package com.jonpeps.gamescms.ui.tabletemplates.viewmodels

import com.jonpeps.gamescms.data.dataclasses.TableItemFinal
import com.jonpeps.gamescms.data.helpers.ChangesCachePair
import com.jonpeps.gamescms.data.helpers.GenericSerializationChangesCache
import com.jonpeps.gamescms.data.helpers.IGenericSerializationCache
import javax.inject.Inject

interface ITableTemplateGroupVmChangesCache : IGenericSerializationCache<List<TableItemFinal>>

class TableTemplateGroupVmChangesCache@Inject constructor()
    : GenericSerializationChangesCache<List<TableItemFinal>>(), ITableTemplateGroupVmChangesCache {
    override val cache: MutableMap<String, ChangesCachePair<List<TableItemFinal>>> = mutableMapOf()

    override fun copy(item: List<TableItemFinal>): List<TableItemFinal> {
        val arrayList = arrayListOf<TableItemFinal>()
        item.forEach {
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

    override fun areEqual(name: String): Boolean {
        val initialItem = cache[name]?.initialItem
        val currentItem = cache[name]?.currentItem
        for (i in 0 until (initialItem?.size ?: 0)) {
            if (initialItem?.get(i) != currentItem?.get(i)) {
                return false
            }
        }
        return true
    }

    override fun getDefault(): List<TableItemFinal> = arrayListOf()
}