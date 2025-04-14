package com.jonpeps.gamescms.ui.tabletemplates.repositories

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.Query
import com.jonpeps.gamescms.data.dataclasses.room.TableItemRoom

@Dao
@Entity(tableName = "template_table")
interface TableTemplateRoomRepository {
    @Query("SELECT * FROM template_table WHERE (:templateName)")
    fun loadAllByTemplateName(templateName: String): List<TableItemRoom>

    @Query("SELECT * FROM template_table")
    fun loadAll(): List<TableItemRoom>

    @Insert
    fun insertAll(vararg items: TableItemRoom)

    @Query("DELETE FROM template_table WHERE (:templateName)")
    fun deleteAll(templateName: String)
}