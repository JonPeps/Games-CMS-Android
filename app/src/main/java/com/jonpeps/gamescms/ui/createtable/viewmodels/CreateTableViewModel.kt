package com.jonpeps.gamescms.ui.createtable.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonpeps.gamescms.data.dataclasses.createtemplate.CreateTableItemData
import com.jonpeps.gamescms.dynamodb.mappers.IDynamoDbCreateTableMapper
import com.jonpeps.gamescms.dynamodb.services.IDynamoDbCreateTable
import com.jonpeps.gamescms.ui.createtable.viewmodels.data.TableRequestViewModelResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import software.amazon.awssdk.services.dynamodb.model.CreateTableResponse
import javax.inject.Inject

@HiltViewModel
class CreateTableViewModel
@Inject constructor(private val dynamoDbCreateTable: IDynamoDbCreateTable,
                    private var dynamoDbCreateTableMapper: IDynamoDbCreateTableMapper,
                    private val dispatcher: CoroutineDispatcher)
    : ViewModel() {

    private val _state = MutableStateFlow(TableRequestViewModelResponse<CreateTableResponse>())
    val state: StateFlow<TableRequestViewModelResponse<CreateTableResponse>> = _state

    fun createTable(tableName: String, items: List<CreateTableItemData>) {
        viewModelScope.launch(dispatcher) {
            val mappedItems = dynamoDbCreateTableMapper.mapToCreateTablePair(items)
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