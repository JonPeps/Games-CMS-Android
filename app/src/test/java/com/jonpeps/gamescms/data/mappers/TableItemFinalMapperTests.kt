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
    fun `FROM table template moshi TO table item final`() {
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
    fun `FROM table item final TO table template moshi`() {
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
    fun `FROM template moshi list TO table item final list`() {
        val result = TableItemFinalMapper
            .fromTableTemplateListMoshi(listOf(TableTemplateItemMoshi(
                "name1", ItemType.STRING, false, "value1",
                editable = true,
                isSortKey = false),
                TableTemplateItemMoshi(
                    "name2", ItemType.BOOLEAN, true, "value2",
                editable = false,
                isSortKey = true)))

        assert(result.size == 2)

        assert(result[0].name == "name1")
        assert(result[0].dataType == ItemType.STRING)
        assert(!result[0].isPrimary)
        assert(result[0].value == "value1")
        assert(result[0].editable)
        assert(!result[0].isSortKey)

        assert(result[1].name == "name2")
        assert(result[1].dataType == ItemType.BOOLEAN)
        assert(result[1].isPrimary)
        assert(result[1].value == "value2")
        assert(!result[1].editable)
        assert(result[1].isSortKey)
    }
}