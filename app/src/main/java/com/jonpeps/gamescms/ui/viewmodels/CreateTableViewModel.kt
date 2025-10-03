package com.jonpeps.gamescms.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonpeps.gamescms.data.dataclasses.createtemplate.CreateTableItemData
import com.jonpeps.gamescms.dynamodb.mappers.DynamoDbCreateTableMapper
import com.jonpeps.gamescms.dynamodb.services.DynamoDbCreateTable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.CreateTableResponse
import javax.inject.Inject

class TableRequestViewModelResponse<T>(
    val success: Boolean = false,
    val response: T? = null,
    val exception: Exception? = null)

@HiltViewModel
class CreateTableViewModel
@Inject constructor(private val dynamoDbClient: DynamoDbClient,
                    private val dispatcher: CoroutineDispatcher)
    : ViewModel() {

    private val _state = MutableStateFlow(TableRequestViewModelResponse<CreateTableResponse>())
    val state: StateFlow<TableRequestViewModelResponse<CreateTableResponse>> = _state

    fun createTable(tableName: String, items: List<CreateTableItemData>) {
        viewModelScope.launch(dispatcher) {
            try {
                val mappedItems = DynamoDbCreateTableMapper.mapToCreateTablePair(items)
                val dynamoDbCreateTable = DynamoDbCreateTable(dynamoDbClient)
                val response = dynamoDbCreateTable.create(tableName, mappedItems.attDefinitions, mappedItems.keySchema)
                _state.value = TableRequestViewModelResponse(true, response, null)
            } catch (ex: Exception) {
                _state.value = TableRequestViewModelResponse(false, null, ex)
            }
        }
    }
}