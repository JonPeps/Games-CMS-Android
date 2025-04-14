package com.jonpeps.gamescms.data.dataclasses.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jonpeps.gamescms.data.dataclasses.ItemType

@Entity
data class TableItemRoom(
    @PrimaryKey
    var id: Int = -1,
    @ColumnInfo(name = "templateName")
    var templateName: String = "",
    @ColumnInfo(name = "name")
    var name: String = "",
    @ColumnInfo(name = "dataType")
    var dataType: ItemType = ItemType.STRING,
    @ColumnInfo(name = "isPrimary")
    var isPrimary: Boolean = false,
    @ColumnInfo(name = "value")
    var value: String = "",
    @ColumnInfo(name = "editable")
    var editable: Boolean = true,
    @ColumnInfo(name = "isSortKey")
    var isSortKey: Boolean = false
)
