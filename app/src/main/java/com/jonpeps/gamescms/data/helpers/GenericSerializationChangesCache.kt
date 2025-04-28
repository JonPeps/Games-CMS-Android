package com.jonpeps.gamescms.data.helpers

interface IGenericSerializationCache<T> {
    fun set(name: String, item: T)
    fun updateCurrent(item: T)
    fun get(): T
    fun hasChanges(): Boolean
    fun reset()
}

abstract class GenericSerializationChangesCache<T> : IGenericSerializationCache<T> {
    private var cachedItemName: String? = null
    protected abstract var initialItem: T
    protected abstract var currentItem: T

    override fun set(name: String, item: T) {
        cachedItemName = name
        initialItem = deepCopy(item)
        currentItem = deepCopy(item)
    }

    override fun updateCurrent(item: T) {
        currentItem = deepCopy(item)
    }

    override fun get(): T = currentItem

    override fun hasChanges(): Boolean {
        return !equalsOther(currentItem)
    }

    override fun reset() {
        currentItem = deepCopy(initialItem)
    }

    protected abstract fun deepCopy(item: T): T

    protected abstract fun equalsOther(item: T): Boolean
}