package com.jonpeps.gamescms.data.dataclasses

data class TableItemFinal(
    var name: String,
    var dataType: ItemType,
    var isPrimary: Boolean = false,
    var value: String = "",
    var editable: Boolean = true,
    var isSortKey: Boolean = false
)
