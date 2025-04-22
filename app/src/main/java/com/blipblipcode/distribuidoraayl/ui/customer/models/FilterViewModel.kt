package com.blipblipcode.distribuidoraayl.ui.customer.models

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update

abstract class FilterViewModel<T>() : ViewModel() {

    internal val mAppliedFilters = MutableStateFlow<List<DataFilter>>(emptyList())
    val appliedFilters = mAppliedFilters.asStateFlow()

    fun onAddFilter(filter: DataFilter) {
        mAppliedFilters.update { currentFilters ->
            currentFilters.toMutableList().apply { add(filter) }
        }
    }

    fun onAddFilter(filters: List<DataFilter>) {
        mAppliedFilters.update { currentFilters ->
            if (filters.isEmpty()) currentFilters else currentFilters.toMutableList()
                .apply { addAll(filters) }
        }
    }
    fun onDeleteFilter(filter: DataFilter) {
        mAppliedFilters.update { currentFilters ->
            currentFilters.toMutableList().apply { remove(filter) }
        }
    }


    protected fun applyFilters(
        list: List<T>,
        filters: List<DataFilter>
    ): Flow<List<T>> = flow {
        val filteredList = list.filter { data ->
            filters.all { filter ->
                when (filter.type) {
                    is TypeFilter.Text -> filterText(filter, data)
                    is TypeFilter.Number -> filterNumber(filter, data)
                    is TypeFilter.Date -> filterDate(filter, data)
                    is TypeFilter.Boolean -> filterBoolean(filter, data)
                }
            }
        }
        emit(filteredList)
    }

    private fun filterText(filter: DataFilter, data: T): Boolean {
        val fieldValue = getFieldValue(data, filter.field) as? String ?: return false
        return when (filter.type) {
            TypeFilter.Text.Equals -> fieldValue == filter.value
            TypeFilter.Text.Contains -> fieldValue.contains(filter.value, ignoreCase = true)
            else -> false
        }
    }

    private fun filterNumber(filter: DataFilter, data: T): Boolean {
        val fieldValue =
            getFieldValue(data, filter.field)?.toString()?.toDoubleOrNull() ?: return false
        val filterValue = filter.value.toDoubleOrNull() ?: return false
        return when (filter.type) {
            TypeFilter.Number.GreaterThan -> fieldValue > filterValue
            TypeFilter.Number.LessThan -> fieldValue < filterValue
            TypeFilter.Number.Equals -> fieldValue == filterValue
            else -> false
        }
    }

    private fun filterDate(filter: DataFilter, data: T): Boolean {
        val fieldValue = getFieldValue(data, filter.field)?.toString() ?: return false
        val filterValue = filter.value

        return when (filter.type) {
            TypeFilter.Date.Equals -> fieldValue == filterValue
            TypeFilter.Date.GreaterThan -> fieldValue > filterValue
            TypeFilter.Date.LessThan -> fieldValue < filterValue
            TypeFilter.Date.Between -> {
                val dateRange = filterValue.split(",").takeIf { it.size == 2 } ?: return false
                fieldValue in dateRange[0]..dateRange[1]
            }

            else -> false
        }
    }

    private fun filterBoolean(filter: DataFilter, data: T): Boolean {
        val fieldValue = getFieldValue(data, filter.field)?.toString()?.toBooleanStrictOrNull()
            ?: return false
        val filterValue = filter.value.toBooleanStrictOrNull() ?: return false
        return fieldValue == filterValue
    }

    protected abstract fun getFieldValue(data: T, field: String): Any?

}