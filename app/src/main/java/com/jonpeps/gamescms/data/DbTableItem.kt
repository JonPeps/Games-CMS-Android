package com.jonpeps.gamescms.data

data class DbTableItem<T>(
    val name: String,
    val dataType: DbDataItemType,
    val isPrimary: Boolean = false,
    val value: T,
    val editable: Boolean = true,
    val isSortKey: Boolean = false
)