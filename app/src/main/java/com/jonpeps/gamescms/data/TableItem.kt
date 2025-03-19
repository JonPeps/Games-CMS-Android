package com.jonpeps.gamescms.data

data class TableItem<T>(
    val name: String,
    val dataType: ItemType,
    val isPrimary: Boolean = false,
    val value: T,
    val editable: Boolean = true,
    val isSortKey: Boolean = false
)