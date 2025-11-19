package com.jonpeps.gamescms.viewmodels

import com.jonpeps.gamescms.data.dataclasses.ItemType
import com.jonpeps.gamescms.data.dataclasses.TableItemFinal
import com.jonpeps.gamescms.ui.tabletemplates.viewmodels.ITableTemplateGroupVmChangesCache
import com.jonpeps.gamescms.ui.tabletemplates.viewmodels.TableTemplateGroupVmChangesCache
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class TableTemplateGroupVmChangesCacheTests {
    private lateinit var dummyData: ArrayList<TableItemFinal>
    private lateinit var changesCache: ITableTemplateGroupVmChangesCache
    private val dummyName = "test"

    @Before
    fun setup() {
        dummyData = arrayListOf(
            TableItemFinal(
                name = "name1",
                dataType = ItemType.STRING,
                isPrimary = true,
                value = "value1",
                editable = true,
            ), TableItemFinal(
                name = "name2",
                dataType = ItemType.BOOLEAN,
                isPrimary = false,
                value = "true",
                editable = true,
            )
        )
        changesCache = TableTemplateGroupVmChangesCache()
    }

    @Test
    fun `test UPDATE current`() {
        changesCache.set(dummyName, dummyData)
        val currentData = changesCache.get(dummyName)
        currentData[0].name = "name3"
        changesCache.updateCurrent(dummyName, currentData)
        assert(changesCache.get(dummyName)[0].name == "name3")
    }

    @Test
    fun `test RESET`() {
        changesCache.set(dummyName, dummyData)
        val currentData = changesCache.get(dummyName)
        currentData[0].name = "name3"
        changesCache.updateCurrent(dummyName, currentData)
        assert(changesCache.get(dummyName)[0].name == "name3")
        changesCache.reset(dummyName)
        assert(changesCache.get(dummyName)[0].name == "name1")
    }

    @Test
    fun `test HAS CHANGES`() {
        changesCache.set(dummyName, dummyData)
        val currentData = changesCache.get(dummyName)
        currentData[0].name = "name3"
        changesCache.updateCurrent(dummyName, currentData)
        assert(changesCache.hasChanges(dummyName))
        changesCache.reset(dummyName)
        assert(!changesCache.hasChanges(dummyName))
    }
}