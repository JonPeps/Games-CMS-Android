package com.jonpeps.gamescms.ui.tabletemplates.repositories

import com.jonpeps.gamescms.data.dataclasses.moshi.StringListMoshi
import com.jonpeps.gamescms.data.helpers.BasicStringGenericItemCache
import com.jonpeps.gamescms.data.helpers.IBasicStringGenericItemCache
import javax.inject.Inject

interface IStringListMoshiJsonCache : IBasicStringGenericItemCache<StringListMoshi>

class StringListMoshiJsonCache@Inject constructor()
    : BasicStringGenericItemCache<StringListMoshi>(), IStringListMoshiJsonCache {
    override val cache: MutableMap<String, StringListMoshi> = mutableMapOf()

    override fun getDefaultItem(): StringListMoshi = StringListMoshi(items = listOf())
}