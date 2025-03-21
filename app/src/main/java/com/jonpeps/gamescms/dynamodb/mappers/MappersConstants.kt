package com.jonpeps.gamescms.dynamodb.mappers

class MappersConstants {
    companion object {
        const val TRUE_STR = "true"
        const val FALSE_STR = "false"
        const val TRUE_INT = "1"
        const val FALSE_INT = "0"

        fun toBoolean(value: String): Boolean {
            return value == MappersConstants.TRUE_STR || value == MappersConstants.TRUE_INT
        }
    }
}