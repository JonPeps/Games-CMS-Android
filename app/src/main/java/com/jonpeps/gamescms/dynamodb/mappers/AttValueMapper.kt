package com.jonpeps.gamescms.dynamodb.mappers

import com.jonpeps.gamescms.data.ItemType
import com.jonpeps.gamescms.data.TableItem
import software.amazon.awssdk.services.dynamodb.model.AttributeValue

sealed class AttValueMapper {
    companion object {
        fun get(item: TableItem): AttributeValue {
            val builder = AttributeValue.builder()
            when (item.dataType) {
                ItemType.STRING -> builder.s(item.value)
                ItemType.UTC_DATE -> builder.s(item.value)
                ItemType.INT -> builder.n(item.value)
                ItemType.BOOLEAN -> builder.bool(toBoolean(item.value))
            }
            return builder.build()
        }

        private fun toBoolean(value: String): Boolean {
            return value == "true" || value == "1"
        }
    }
}