package com.jonpeps.gamescms.data.dataclasses

data class CreateTableItemData(
    val name: String,
    val dataType: ItemType,
    val isPrimary: Boolean = false,
    val isSortKey: Boolean = false
)