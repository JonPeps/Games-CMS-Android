package com.jonpeps.gamescms.dynamodb.mappers

import com.jonpeps.gamescms.data.ItemType

sealed class ItemTypeToAttType {
    companion object {
        fun get(type: ItemType): String {
            return when (type) {
                ItemType.STRING -> "S"
                ItemType.BOOLEAN -> "S"
                ItemType.UTC_DATE -> "S"
                ItemType.INT -> "N"
            }
        }
    }
}