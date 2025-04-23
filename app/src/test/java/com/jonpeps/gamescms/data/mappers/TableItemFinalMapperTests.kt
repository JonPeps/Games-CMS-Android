package com.jonpeps.gamescms.data.mappers

import com.jonpeps.gamescms.data.dataclasses.ItemType
import com.jonpeps.gamescms.data.dataclasses.TableItemFinal
import com.jonpeps.gamescms.data.dataclasses.mappers.TableItemFinalMapper
import com.jonpeps.gamescms.data.dataclasses.moshi.TableTemplateItemMoshi
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class TableItemFinalMapperTests {
    @Test
    fun `test from template moshi mapped values`() {
        val result = TableItemFinalMapper
            .fromTableTemplateMoshi(
                TableTemplateItemMoshi("name", ItemType.STRING, true, "value",
            editable = true,
            isSortKey = true
        ))
        assert(result.name == "name")
        assert(result.dataType == ItemType.STRING)
        assert(result.isPrimary)
        assert(result.value == "value")
        assert(result.editable)
        assert(result.isSortKey)
    }

    @Test
    fun `to template moshi mapped values`() {
        val result = TableItemFinalMapper
            .toTableTemplateItemMoshi(
                TableItemFinal("name", ItemType.STRING, true, "value",
                    editable = true, isSortKey = true))
        assert(result.name == "name")
        assert(result.dataType == ItemType.STRING)
        assert(result.isPrimary)
        assert(result.value == "value")
        assert(result.editable)
        assert(result.isSortKey)
    }

    @Test
    fun `test from template moshi list mapped values`() {
        val result = TableItemFinalMapper
            .fromTableTemplateListMoshi(listOf(TableTemplateItemMoshi(
                "name1", ItemType.STRING, false, "value1",
                editable = true,
                isSortKey = false),
                TableTemplateItemMoshi(
                    "name2", ItemType.BOOLEAN, true, "value2",
                editable = false,
                isSortKey = true)))


    }
}