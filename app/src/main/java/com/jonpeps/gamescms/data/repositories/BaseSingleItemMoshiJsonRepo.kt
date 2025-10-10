package com.jonpeps.gamescms.data.repositories

interface IBaseSingleItemMoshiJsonRepository<T>: IBaseMoshiRepo {
    fun getItem(): T?
    fun setItem(item: T?)
}

