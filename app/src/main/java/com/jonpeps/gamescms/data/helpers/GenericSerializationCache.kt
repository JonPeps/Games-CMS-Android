package com.jonpeps.gamescms.data.helpers

interface IGenericSerializationCache<T> {
    fun set(name: String, item: T)
    fun update(item: T)
    fun get(): T?
    fun hasChanges(): Boolean
    fun reset()
}

class GenericSerializationCache<T> : IGenericSerializationCache<T> {
    private var cachedItemName: String? = null
    private var initialItem: T? = null
    private var currentItem: T? = null

    override fun set(name: String, item: T) {
        TODO("Not yet implemented")
    }

    override fun update(item: T) {
        TODO("Not yet implemented")
    }

    override fun get(): T? {
        TODO("Not yet implemented")
    }

    override fun hasChanges(): Boolean {
        TODO("Not yet implemented")
    }

    override fun reset() {
        TODO("Not yet implemented")
    }
}