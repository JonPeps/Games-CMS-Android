package com.jonpeps.gamescms.data.dataclasses.createtemplate

import com.jonpeps.gamescms.data.dataclasses.ItemType

data class CreateTableItemData(
    val name: String,
    val dataType: ItemType,
    val isPrimary: Boolean = false,
    val isSortKey: Boolean = false
)