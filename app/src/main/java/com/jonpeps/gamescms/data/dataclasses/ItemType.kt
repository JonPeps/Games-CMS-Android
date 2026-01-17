package com.jonpeps.gamescms.data.dataclasses

enum class ItemType {
    STRING, INT, BOOLEAN, UTC_DATE;

    override fun toString(): String {
        return when (this) {
            STRING -> "String"
            INT -> "Integer"
            BOOLEAN -> "Boolean"
            UTC_DATE -> "UtcDate"
        }
    }
}