package com.jonpeps.gamescms.data.helpers

interface IBasicStringGenericItemCache<T> {
    fun set(name: String, item: T?)
    fun get(name: String): T?
    fun exists(name: String): Boolean
    fun getDefaultItem(): T
}

abstract class BasicStringGenericItemCache<T> : IBasicStringGenericItemCache<T> {
    protected abstract val cache: MutableMap<String, T>

    override fun set(name: String, item: T?) {
        cache[name] = item?: getDefaultItem()
    }
    override fun get(name: String): T? {
        return if (cache.containsKey(name)) {
            cache[name]
        } else {
            null
        }
    }

    override fun exists(name: String): Boolean {
        return cache.containsKey(name)
    }
}
