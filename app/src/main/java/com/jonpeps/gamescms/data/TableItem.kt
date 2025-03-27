package com.jonpeps.gamescms.data

import com.squareup.moshi.Json

data class TableItem(
    @Json(name = "id")
    var id: Int = -1,
    @Json(name = "name")
    var name: String = "",
    @Json(name = "dataType")
    var dataType: ItemType = ItemType.STRING,
    @Json(name = "isPrimary")
    var isPrimary: Boolean = false,
    @Json(name = "value")
    var value: String = "",
    @Json(name = "editable")
    var editable: Boolean = true,
    @Json(name = "isSortKey")
    var isSortKey: Boolean = false
)