package com.jonpeps.gamescms.ui.tabletemplates.viewmodels

import com.jonpeps.gamescms.data.helpers.ChangesCachePair
import com.jonpeps.gamescms.data.helpers.GenericSerializationChangesCache
import com.jonpeps.gamescms.data.helpers.IGenericSerializationCache
import javax.inject.Inject

interface ITableTemplateListItemsVmChangesCache : IGenericSerializationCache<List<String>>

class TableTemplateListItemsVmChangesCache@Inject constructor()
    : GenericSerializationChangesCache<List<String>>(), ITableTemplateListItemsVmChangesCache {
    override val cache: MutableMap<String, ChangesCachePair<List<String>>> = mutableMapOf()

    override fun copy(item: List<String>): List<String> {
        val arrayList = arrayListOf<String>()
        arrayList.addAll(item)
        return arrayList
    }

    override fun areEqual(name: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun getDefault(): List<String> = emptyList()
}