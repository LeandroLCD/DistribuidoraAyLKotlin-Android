package com.blipblipcode.distribuidoraayl.ui.customer.list

import androidx.lifecycle.viewModelScope
import com.blipblipcode.distribuidoraayl.domain.models.customer.Customer
import com.blipblipcode.distribuidoraayl.domain.models.onError
import com.blipblipcode.distribuidoraayl.domain.models.onSuccess
import com.blipblipcode.distribuidoraayl.domain.useCase.customer.IDeleteCustomerUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.customer.IGetCustomersUseCase
import com.blipblipcode.distribuidoraayl.ui.customer.models.FilterViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.reflect.full.memberProperties
import javax.inject.Inject

@HiltViewModel
class CustomerListViewModel @Inject constructor(
    getCustomersUseCase: dagger.Lazy<IGetCustomersUseCase>,
    private val deleteCustomerUseCase: dagger.Lazy<IDeleteCustomerUseCase>
) : FilterViewModel<Customer>() {

    /*State*/
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorException = MutableStateFlow<Throwable?>(null)
    val errorException = _errorException.asStateFlow()
    val customers = getCustomersUseCase.get().invoke()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(1_000), listOf())

    val customersFiltered: Flow<List<Customer>> =
        customers.combineTransform(mAppliedFilters) { customerList, filters ->
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


    override fun getFieldValue(data: Customer, field: String): Any? {
        return data::class.memberProperties
            .firstOrNull { it.name == field }
            ?.getter
            ?.call(data)
        /*return when (field) {
            "commune" -> data.commune
            "regionId" -> data.regionId
            "country" -> data.country
            "address" -> data.address
            "companyName" -> data.companyName
            "rut" -> data.rut
            "rubro" -> data.rubro
            "phone" -> data.phone
            "registrationDate" -> data.registrationDate
            "birthDate" -> data.birthDate
            "routeId" -> data.routeId
            else -> null
        }*/
    }


}