package com.jonpeps.gamescms.data.dataclasses.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TableTemplateNameListRoom(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "template_name")
    val templateName: String
)