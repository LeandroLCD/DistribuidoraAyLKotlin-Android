package com.blipblipcode.distribuidoraayl.ui.customer.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blipblipcode.distribuidoraayl.domain.models.customer.Customer
import com.blipblipcode.distribuidoraayl.domain.models.onError
import com.blipblipcode.distribuidoraayl.domain.models.onSuccess
import com.blipblipcode.distribuidoraayl.domain.useCase.customer.IDeleteCustomerUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.customer.IGetCustomersUseCase
import com.blipblipcode.distribuidoraayl.ui.customer.models.DataFilter
import com.blipblipcode.distribuidoraayl.ui.customer.models.TypeFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomerListViewModel @Inject constructor(
    getCustomersUseCase: dagger.Lazy<IGetCustomersUseCase>,
    private val deleteCustomerUseCase: dagger.Lazy<IDeleteCustomerUseCase>
) : ViewModel() {

    /*State*/
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorException = MutableStateFlow<Throwable?>(null)
    val errorException = _errorException.asStateFlow()
    val customers = getCustomersUseCase.get().invoke()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(1_000), listOf())

    private val _appliedFilters = MutableStateFlow<List<DataFilter>>(emptyList())
    val appliedFilters = _appliedFilters.asStateFlow()

    val customersFiltered: Flow<List<Customer>> =
        customers.combineTransform(_appliedFilters) { customerList, filters ->
            applyFilters(customerList, filters)
        }

    /*Event*/
    fun onDeleteCustomer(customer: Customer) {
        _isLoading.tryEmit(true)
        viewModelScope.launch {
            deleteCustomerUseCase.get().invoke(customer.rut).onSuccess {
                _isLoading.tryEmit(false)
            }.onError {
                _errorException.tryEmit(it)
                _isLoading.tryEmit(false)
            }
        }
    }

    fun onAddFilter(filter: DataFilter) {
        _appliedFilters.update { currentFilters ->
            currentFilters.toMutableList().apply { add(filter) }
        }
    }

    fun onAddFilter(filters: List<DataFilter>) {
        _appliedFilters.update { currentFilters ->
            if (filters.isEmpty()) currentFilters else currentFilters.toMutableList()
                .apply { addAll(filters) }
        }
    }
    fun onDeleteFilter(filter: DataFilter) {
        _appliedFilters.update { currentFilters ->
            currentFilters.toMutableList().apply { remove(filter) }
        }
    }


    private fun applyFilters(
        customerList: List<Customer>,
        filters: List<DataFilter>
    ): Flow<List<Customer>> = flow {
        val filteredList = customerList.filter { customer ->
            filters.all { filter ->
                when (filter.type) {
                    is TypeFilter.Text -> filterText(filter, customer)
                    is TypeFilter.Number -> filterNumber(filter, customer)
                    is TypeFilter.Date -> filterDate(filter, customer)
                    is TypeFilter.Boolean -> filterBoolean(filter, customer)
                }
            }
        }
        emit(filteredList)
    }

    private fun filterText(filter: DataFilter, customer: Customer): Boolean {
        val fieldValue = getFieldValue(customer, filter.field) as? String ?: return false
        return when (filter.type) {
            TypeFilter.Text.Equals -> fieldValue == filter.value
            TypeFilter.Text.Contains -> fieldValue.contains(filter.value, ignoreCase = true)
            else -> false
        }
    }

    private fun filterNumber(filter: DataFilter, customer: Customer): Boolean {
        val fieldValue =
            getFieldValue(customer, filter.field)?.toString()?.toDoubleOrNull() ?: return false
        val filterValue = filter.value.toDoubleOrNull() ?: return false
        return when (filter.type) {
            TypeFilter.Number.GreaterThan -> fieldValue > filterValue
            TypeFilter.Number.LessThan -> fieldValue < filterValue
            TypeFilter.Number.Equals -> fieldValue == filterValue
            else -> false
        }
    }

    private fun filterDate(filter: DataFilter, customer: Customer): Boolean {
        val fieldValue = getFieldValue(customer, filter.field)?.toString() ?: return false
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

    private fun filterBoolean(filter: DataFilter, customer: Customer): Boolean {
        val fieldValue = getFieldValue(customer, filter.field)?.toString()?.toBooleanStrictOrNull()
            ?: return false
        val filterValue = filter.value.toBooleanStrictOrNull() ?: return false
        return fieldValue == filterValue
    }

    private fun getFieldValue(customer: Customer, field: String): Any? {
        return when (field) {
            "commune" -> customer.commune
            "regionId" -> customer.regionId
            "country" -> customer.country
            "address" -> customer.address
            "companyName" -> customer.companyName
            "rut" -> customer.rut
            "rubro" -> customer.rubro
            "phone" -> customer.phone
            "registrationDate" -> customer.registrationDate
            "birthDate" -> customer.birthDate
            "routeId" -> customer.routeId
            else -> null
        }
    }


}