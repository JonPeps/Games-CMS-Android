package com.jonpeps.gamescms.data.helpers

interface IGenericSerializationCache<T> {
    fun set(name: String, item: T)
    fun updateCurrent(name: String, item: T)
    fun get(name: String): T
    fun hasChanges(name: String): Boolean
    fun reset(name: String)
    fun isPopulated(): Boolean
}

abstract class ChangesCachePair<T> {
    abstract var initialItem: T
    abstract var currentItem: T
}

abstract class GenericSerializationChangesCache<T> : IGenericSerializationCache<T> {
    protected abstract val cache: MutableMap<String, ChangesCachePair<T>>
    private var isPopulated = false

    override fun set(name: String, item: T) {
        val changesCachePair = object : ChangesCachePair<T>() {
            override var initialItem: T = deepCopy(item)
            override var currentItem: T = deepCopy(item)
        }
        cache[name] = changesCachePair
        isPopulated = true
    }

    override fun updateCurrent(name: String, item: T) {
        if (cache.containsKey(name)) {
            cache[name]?.currentItem = deepCopy(item)
        }
    }

    override fun get(name: String): T {
        if (cache.containsKey(name)) {
            return cache[name]?.currentItem ?: getDefault()
        }
        return getDefault()
    }

    override fun hasChanges(name: String): Boolean {
        return !areEqual(name)
    }

    override fun isPopulated(): Boolean = isPopulated

    override fun reset(name: String) {
        if (cache.containsKey(name)) {
            val initialItem = cache[name]?.initialItem
            if (initialItem != null) {
                cache[name]?.currentItem = deepCopy(initialItem)
            }
        }
    }

    protected abstract fun deepCopy(item: T): T

    protected abstract fun areEqual(name: String): Boolean

    protected abstract fun getDefault(): T
}