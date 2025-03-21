package com.jonpeps.gamescms.dynamodb.mappers

import com.jonpeps.gamescms.data.TableItem
import software.amazon.awssdk.services.dynamodb.model.AttributeAction
import software.amazon.awssdk.services.dynamodb.model.AttributeValueUpdate

class AttValueUpdateMapper {
    companion object {
        fun get(item: TableItem, action: AttributeAction): AttributeValueUpdate {
            val attValue = AttValueMapper.get(item)
            return AttributeValueUpdate.builder().value(attValue).action(action).build()
        }
    }
}