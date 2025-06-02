package com.jonpeps.gamescms.ui.tabletemplates.viewmodels

import com.jonpeps.gamescms.data.helpers.ChangesCachePair
import com.jonpeps.gamescms.data.helpers.GenericSerializationChangesCache
import com.jonpeps.gamescms.data.helpers.IGenericSerializationCache
import javax.inject.Inject

interface IStringListItemsVmChangesCache : IGenericSerializationCache<ArrayList<String>>

class StringListItemsVmChangesCache@Inject constructor()
    : GenericSerializationChangesCache<ArrayList<String>>(), IStringListItemsVmChangesCache {
    override val cache: MutableMap<String, ChangesCachePair<ArrayList<String>>> = mutableMapOf()

    override fun copy(item: ArrayList<String>): ArrayList<String> {
        val arrayList = arrayListOf<String>()
        arrayList.addAll(item)
        return arrayList
    }

    override fun areEqual(name: String): Boolean {
        val initialItem = cache[name]?.initialItem
        val currentItem = cache[name]?.currentItem
        if (initialItem?.size != currentItem?.size) return false
        val asList = currentItem?.toArray()?.asList()
        if (asList != null) {
            return initialItem?.containsAll(asList) == true
        }
        return true
    }

    override fun getDefault(): ArrayList<String> = arrayListOf()
}