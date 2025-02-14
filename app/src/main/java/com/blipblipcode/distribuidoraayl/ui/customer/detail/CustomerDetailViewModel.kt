package com.blipblipcode.distribuidoraayl.ui.customer.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.blipblipcode.distribuidoraayl.domain.models.customer.Customer
import com.blipblipcode.distribuidoraayl.domain.models.onError
import com.blipblipcode.distribuidoraayl.domain.models.onSuccess
import com.blipblipcode.distribuidoraayl.domain.useCase.customer.ICreateOrUpdateCustomerUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.customer.IGetCustomerFlowUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.customer.IGetRegionsUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.customer.IGetRoutesUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.customer.IGetRubrosUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.openFactura.IGetTaxpayerUseCase
import com.blipblipcode.distribuidoraayl.ui.customer.CustomerBaseViewModel
import com.blipblipcode.distribuidoraayl.ui.navigationGraph.routes.CustomerScreen
import com.blipblipcode.distribuidoraayl.ui.utils.removeAccents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomerDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getRegionsUseCase: dagger.Lazy<IGetRegionsUseCase>,
    getRubrosUseCase: dagger.Lazy<IGetRubrosUseCase>,
    getRoutsUseCase: dagger.Lazy<IGetRoutesUseCase>,
    private val getCustomerFlowUseCase: dagger.Lazy<IGetCustomerFlowUseCase>,
    private val getTaxpayerUseCase: dagger.Lazy<IGetTaxpayerUseCase>,
    private val createOrUpdateCustomerUseCase: dagger.Lazy<ICreateOrUpdateCustomerUseCase>,
) : CustomerBaseViewModel(true) {


    /*States*/
    private val detail = savedStateHandle.toRoute<CustomerScreen.Detail>()

    val regions = getRegionsUseCase.get().invoke()

    @OptIn(ExperimentalCoroutinesApi::class)
    val rubros = getRubrosUseCase.get().invoke().mapLatest { rubro ->
        rubro.sortedBy { it.id }
    }

    val routes = getRoutsUseCase.get().invoke()

    private val _isEditable = MutableStateFlow(detail.isEditable)
    val isEditable = _isEditable.asStateFlow()

    private val customerFireStore = getCustomerFlowUseCase.get().invoke(detail.rut)
        .onEach {
            misLoading.tryEmit(false)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), Customer())


    override val customer = combineCustomer(customerFireStore, mCustomer)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), Customer())


    private fun combineCustomer(customerF: Flow<Customer>, customer: Flow<Customer>): Flow<Customer> {
        return if(isEditable.value){
            customer
        }else{
            mCustomer.tryEmit(customerFireStore.value)
            customerF
        }
    }

    fun syncTaxpayerData(rut: String) {
        misLoading.update {
            true
        }

        viewModelScope.launch {
            getTaxpayerUseCase.get().invoke(rut).onSuccess { taxpayer ->
                val commune = taxpayer.commune.removeAccents()
                val region = regions.first().find { region ->
                    region.communes.any {
                        it.name.contains(commune, true)
                    }
                }
                mCustomer.update { old ->
                    old.copy(
                        rut = taxpayer.rut,
                        commune = commune,
                        address = taxpayer.address,
                        regionId = region?.id.orEmpty(),
                        branches = taxpayer.branches,
                        activities = taxpayer.activities,
                        companyName = taxpayer.company,
                        phone = taxpayer.phone.orEmpty()
                    )
                }
                misLoading.update {
                    false
                }
            }.onError {
                misLoading.update {
                    false
                }
                mErrorException.tryEmit(it)
            }
        }
    }

    fun onEditCustomer() {
        _isEditable.update {
            true
        }
    }

    fun onSavedCustomer(customer: Customer, onCompleted: () -> Unit) {
        misLoading.tryEmit(true)
        viewModelScope.launch {
            createOrUpdateCustomerUseCase.get().invoke(customer).onSuccess {
                onCompleted.invoke()
                misLoading.update {
                    false
                }
            }.onError {
                misLoading.update {
                    false
                }
                mErrorException.tryEmit(it)
            }
        }
    }


}