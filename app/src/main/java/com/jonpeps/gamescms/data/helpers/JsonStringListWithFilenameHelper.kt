package com.jonpeps.gamescms.data.helpers

import com.jonpeps.gamescms.data.DataConstants.Companion.COMMON_SPLIT_PER_STRING_ITEM
import com.jonpeps.gamescms.data.dataclasses.moshi.StringListMoshi

data class JsonStringFilenamePair(val itemNames: List<String>, val fileNames: List<String>)

class JsonStringListWithFilenameHelper {
    companion object {
        fun splitItem(item: StringListMoshi?): JsonStringFilenamePair {
            val lhs = arrayListOf<String>()
            val rhs = arrayListOf<String>()
            item?.let { item ->
                item.items.forEach { listItem ->
                    val splitList = listItem.split(COMMON_SPLIT_PER_STRING_ITEM)
                    if (splitList.size == 2) {
                        lhs.add(splitList[0])
                        rhs.add(splitList[1])
                    }
                }
            }
            return JsonStringFilenamePair(lhs, rhs)
        }
    }
}