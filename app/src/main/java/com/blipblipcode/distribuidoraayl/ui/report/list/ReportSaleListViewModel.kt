package com.blipblipcode.distribuidoraayl.ui.report.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.blipblipcode.distribuidoraayl.domain.models.filters.DataFilter
import com.blipblipcode.distribuidoraayl.domain.models.filters.TypeFilter
import com.blipblipcode.distribuidoraayl.domain.models.reportSale.ReportSale
import com.blipblipcode.distribuidoraayl.domain.useCase.reportSale.IGetReportSaleUseCase
import com.blipblipcode.library.DateTime
import com.blipblipcode.library.model.DateTimeRange
import com.google.android.gms.common.util.ArrayUtils.removeAll
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ReportSaleListViewModel @Inject constructor(
    private val getReportSaleUseCase: dagger.Lazy<IGetReportSaleUseCase>
): ViewModel() {

    private val _filters = MutableStateFlow<Map<String, DataFilter>>(emptyMap())

    @OptIn(ExperimentalCoroutinesApi::class)
    val filters = _filters.flatMapLatest {
        flowOf(it.values.filter { it.isVisible }.toList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _dateTime = MutableStateFlow<DateTimeRange>(
        DateTimeRange
        .builder()
        .startingNow()
        .build()
    )
    val dataTime = _dateTime.asSharedFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val reportSales: Flow<PagingData<ReportSale>> = combine(_filters, _dateTime) { filters, dateTime ->
        dateTime to filters
    }.flatMapLatest { (dateTime, filters) ->
        getReportSaleUseCase.get().invoke(dateTime, filters.values.toList())
    }.cachedIn(viewModelScope)

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asSharedFlow()


    fun updateDateTime(dateTime: DateTime) {
        //_dateTime.tryEmit(dateTime)
    }

    fun addFilter(filter: DataFilter) {
        _filters.update {
            it.toMutableMap().apply {
                put(filter.field, filter)
            }
        }
    }

    fun addFilters(filters: List<DataFilter>) {
        _filters.update {
            it.toMutableMap().apply {
                putAll(filters.associate { it.field to it })
            }
        }
    }

    fun removeFilter(filter: DataFilter) {
        _filters.update {
            it.toMutableMap().apply {
                remove(filter.field)
            }
        }
    }

    fun removeFilters(filters: List<DataFilter>) {
        _filters.update {
            it.toMutableMap().apply {
                removeAll(filters.map { it.field }.toTypedArray())
            }
        }
    }

    fun clearFilters() {
        _filters.tryEmit(emptyMap())
    }

    fun onSearchQueryChanged(query: String) {
        val mapFilter = buildMap {
            put("name", DataFilter(
                type = TypeFilter.Text.Contains,
                field = "name",
                value = query,
                displayValue = "Query",
                isVisible = false
            ))
            put("commune", DataFilter(
                type = TypeFilter.Text.Contains,
                field = "commune",
                value = query,
                displayValue = "Query",
                isVisible = false
            ))
            put("rut", DataFilter(
                type = TypeFilter.Text.Contains,
                field = "rut",
                value = query,
                displayValue = "Query",
                isVisible = false
            ))
            put("number", DataFilter(
                type = TypeFilter.Number.Contains,
                field = "number",
                value = query,
                displayValue = "Query",
                isVisible = false
            ))
        }
        _filters.update {
            it.toMutableMap().apply {
               if(query.isBlank()){
                    removeAll(mapFilter.keys.toTypedArray())
               }else{
                    putAll(mapFilter)
               }

            }
        }
        _searchQuery.tryEmit(query)
    }
}