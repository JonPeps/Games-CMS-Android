package com.jonpeps.gamescms.data

import com.jonpeps.gamescms.data.repositories.IStringListItemsVmChangesCache
import com.jonpeps.gamescms.data.repositories.StringListItemsVmChangesCache
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class StringListItemsVmChangesCacheTests {
    private lateinit var dummyData: ArrayList<String>
    private lateinit var changesCache: IStringListItemsVmChangesCache
    private val dummyName = "test"

    @Before
    fun setup() {
        dummyData = arrayListOf("test1", "test2")
        changesCache = StringListItemsVmChangesCache()
    }

    @Test
    fun `test UPDATE current`() {
        changesCache.set(dummyName, dummyData)
        val currentData = changesCache.get(dummyName)
        currentData[0] = "name3"
        changesCache.updateCurrent(dummyName, currentData)
        assert(changesCache.get(dummyName)[0] == "name3")
    }

    @Test
    fun `test RESET`() {
        changesCache.set(dummyName, dummyData)
        val currentData = changesCache.get(dummyName)
        currentData[0] = "test3"
        changesCache.updateCurrent(dummyName, currentData)
        assert(changesCache.get(dummyName)[0] == "test3")
        changesCache.reset(dummyName)
        assert(changesCache.get(dummyName)[0] == "test1")
    }

    @Test
    fun `test HAS CHANGES`() {
        changesCache.set(dummyName, dummyData)
        val currentData = changesCache.get(dummyName)
        currentData[0] = "test3"
        changesCache.updateCurrent(dummyName, currentData)
        assert(changesCache.hasChanges(dummyName))
        changesCache.reset(dummyName)
        assert(!changesCache.hasChanges(dummyName))
    }
}