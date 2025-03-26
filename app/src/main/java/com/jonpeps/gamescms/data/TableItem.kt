package com.jonpeps.gamescms.data

data class TableItem(
    var id: Int = -1,
    var name: String = "",
    var dataType: ItemType = ItemType.STRING,
    var isPrimary: Boolean = false,
    var value: String = "",
    var editable: Boolean = true,
    var isSortKey: Boolean = false
)