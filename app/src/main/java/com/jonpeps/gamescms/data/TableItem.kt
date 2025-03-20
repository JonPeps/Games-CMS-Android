package com.jonpeps.gamescms.data

data class TableItem(
    val name: String,
    val dataType: ItemType,
    val isPrimary: Boolean = false,
    val value: String,
    val editable: Boolean = true,
    val isSortKey: Boolean = false
)