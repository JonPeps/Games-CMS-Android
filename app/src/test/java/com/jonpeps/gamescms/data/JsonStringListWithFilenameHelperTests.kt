package com.jonpeps.gamescms.data

import com.jonpeps.gamescms.data.dataclasses.moshi.StringListMoshi
import com.jonpeps.gamescms.data.helpers.JsonStringListHelper
import org.junit.Test

class JsonStringListWithFilenameHelperTests {
    @Test
    fun `test split item`() {
        val splitter = JsonStringListHelper()
        val result = splitter.splitItem(
            StringListMoshi(
                arrayListOf(
                    "name1:file1",
                    "name2:file2"
                )
            )
        )
        assert(result.itemNames.size == 2)
        assert(result.itemNames[0] == "name1")
        assert(result.itemNames[1] == "name2")
        assert(result.fileNames.size == 2)
        assert(result.fileNames[0] == "file1")
        assert(result.fileNames[1] == "file2")
    }
}