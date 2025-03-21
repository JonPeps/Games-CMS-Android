package com.jonpeps.gamescms.viewmodels.dynamodbservices

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonpeps.gamescms.data.TableItem
import com.jonpeps.gamescms.dynamodb.mappers.PreCreateTablePairMapper
import com.jonpeps.gamescms.dynamodb.services.DynamoDbCreateTable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import software.amazon.awssdk.services.dynamodb.model.CreateTableResponse
import javax.inject.Inject

class CreateTableViewModel
    @Inject constructor(private val dynamoDbCreateTable: DynamoDbCreateTable,
                        private val dispatcher: CoroutineDispatcher)
    : ViewModel() {
    private val _state = MutableStateFlow(TableRequestViewModelResponse<CreateTableResponse>())
    val state: StateFlow<TableRequestViewModelResponse<CreateTableResponse>> = _state

    fun createTable(tableName: String, items: List<TableItem>) {
        viewModelScope.launch(dispatcher) {
            val mappedItems = PreCreateTablePairMapper.map(items)
            try {
                val response
                    = dynamoDbCreateTable
                        .create(tableName,
                            mappedItems.attDefinitions,
                            mappedItems.keySchema)

                _state.value = TableRequestViewModelResponse(true, response, null)
            } catch (ex: Exception) {
                _state.value = TableRequestViewModelResponse(false, null, ex)
            }
        }
    }
}