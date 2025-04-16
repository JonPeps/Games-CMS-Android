package com.jonpeps.gamescms.ui.tabletemplates.repositories

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.Query
import com.jonpeps.gamescms.data.dataclasses.room.TableItemRoom

@Dao
@Entity(tableName = "template_table")
interface ITableTemplateRoomRepository {
    @Query("SELECT * FROM template_table WHERE (:templateName)")
    fun load(templateName: String): List<TableItemRoom>

    @Insert
    fun insert(vararg items: TableItemRoom)

    @Delete
    fun delete(vararg items: TableItemRoom)
}