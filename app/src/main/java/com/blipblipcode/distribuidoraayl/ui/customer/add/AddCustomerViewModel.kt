package com.blipblipcode.distribuidoraayl.ui.customer.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blipblipcode.distribuidoraayl.domain.models.customer.Branch
import com.blipblipcode.distribuidoraayl.domain.models.customer.Commune
import com.blipblipcode.distribuidoraayl.domain.models.customer.Customer
import com.blipblipcode.distribuidoraayl.domain.models.customer.Region
import com.blipblipcode.distribuidoraayl.domain.models.customer.Route
import com.blipblipcode.distribuidoraayl.domain.models.customer.Rubro
import com.blipblipcode.distribuidoraayl.domain.models.onError
import com.blipblipcode.distribuidoraayl.domain.models.onSuccess
import com.blipblipcode.distribuidoraayl.domain.throwable.CustomerAlreadyExistsException
import com.blipblipcode.distribuidoraayl.domain.throwable.InvalidRutException
import com.blipblipcode.distribuidoraayl.domain.useCase.customer.ICreateOrUpdateCustomerUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.customer.IGetCustomerByRutUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.customer.IGetRegionsUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.customer.IGetRoutesUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.customer.IGetRubrosUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.customer.impl.CreateOrUpdateCustomerUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.openFactura.IGetTaxpayerUseCase
import com.blipblipcode.distribuidoraayl.ui.auth.login.models.DataField
import com.blipblipcode.distribuidoraayl.ui.utils.removeAccents
import com.blipblipcode.distribuidoraayl.ui.utils.toFormattedString
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class AddCustomerViewModel @Inject constructor(
    getRegionsUseCase: Lazy<IGetRegionsUseCase>,
    getRubrosUseCase: Lazy<IGetRubrosUseCase>,
    getRoutsUseCase: Lazy<IGetRoutesUseCase>,
    private val createOrUpdateCustomerUseCase: Lazy<ICreateOrUpdateCustomerUseCase>,
    private val getTaxpayerUseCase: Lazy<IGetTaxpayerUseCase>,
    private val getCustomerUseCase: Lazy<IGetCustomerByRutUseCase>
) : ViewModel() {

    /*States*/
    private val rutRegex = "^\\d{6,9}-[0-9K]\$".toRegex()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorException = MutableStateFlow<Throwable?>(null)
    val errorException = _errorException.asStateFlow()

    val regions = getRegionsUseCase.get().invoke()

    @OptIn(ExperimentalCoroutinesApi::class)
    val rubros = getRubrosUseCase.get().invoke().mapLatest { rubro->
        rubro.sortedBy { it.id }
    }

    val routes = getRoutsUseCase.get().invoke()

    private val _communes = MutableStateFlow<List<Commune>>(listOf())
    val communes = _communes.asStateFlow()

    private val _rut = MutableStateFlow(DataField("13351250-1"))
    val rut = _rut.asStateFlow()

    private val _customer = MutableStateFlow(Customer())
    val customer = _customer.asStateFlow()

    init {
        viewModelScope.launch {
            _rut.debounce(200).collectLatest { data ->
                if (data.value.matches(rutRegex) && !isLoading.value) {
                    onFindRut(data.value)
                    _rut.value = data.copy(errorException = null, isError = false)
                }
            }
        }
    }

    /*Event*/
    fun onRutChanged(value: String) {
        _rut.update {oldRut->
            if (!value.matches(rutRegex)) {
                oldRut.copy(value = value, isError = true, errorException = InvalidRutException())
            } else {
                oldRut.copy(value = value, isError = false, errorException = null)

            }
        }
    }

    fun onNameChanged(value: String) {
        _customer.update { old ->
            old.copy(companyName = value)
        }
    }

    fun onPhoneChanged(value: String) {
        _customer.update { old ->
            old.copy(phone = value)
        }
    }

    fun onAddressChanged(value: String) {
        _customer.update { old ->
            old.copy(address = value)
        }
    }

    fun onBirthdayChanged(date: String) {
        _customer.update { old ->
            old.copy(birthDate = date)
        }
    }

    fun onRegionChanged(region: Region) {
        _communes.update {
            region.communes
        }
        _customer.update { old ->
            old.copy(regionId = region.id)
        }
    }

    fun onRubrosChanged(data: Rubro) {
        _customer.update { old ->
            old.copy(rubro = data)
        }
    }

    fun onCommuneChanged(commune: Commune) {
        _customer.update { old ->
            old.copy(commune = commune.name)
        }

    }

    fun onRouteChanged(route: Route) {
        _customer.update {
            it.copy(routeId = route.uid)
        }
    }

    fun onChangedBranch(branch: Branch, sac: String) {
        _customer.update { old->
            old.copy(branches = old.branches?.map {
                if (it.code == branch.code) {
                    it.copy(sapCode = sac)
                } else {
                    it
                }
            })
        }
    }

    fun onSavedCustomer(customer: Customer, onCompleted: () -> Unit){
        _isLoading.tryEmit(true)
        viewModelScope.launch {
            createOrUpdateCustomerUseCase.get().invoke(customer).onSuccess {
                onCompleted.invoke()
                _isLoading.update {
                    false
                }
            }.onError {
                _errorException.tryEmit(it)
                _isLoading.update {
                    false
                }
            }
        }
    }

    private fun onFindRut(rut:String){
        _isLoading.tryEmit(true)
        viewModelScope.launch {
            val date = Date()

            getCustomerUseCase.get().invoke(rut).onSuccess {
                _isLoading.update {
                    false
                }
                _errorException.tryEmit(CustomerAlreadyExistsException())

            }.onError {
                getTaxpayerUseCase.get().invoke(rut).onSuccess { taxpayer->
                    val commune = taxpayer.commune.removeAccents()
                    val region = regions.first().find { region->
                        region.communes.any{
                            it.name.contains(commune, true)
                        } }
                    _customer.update { old ->
                        old.copy(
                            rut = taxpayer.rut,
                            commune = commune,
                            address = taxpayer.address,
                            regionId = region?.id.orEmpty(),
                            branches = taxpayer.branches,
                            activities = taxpayer.activities,
                            companyName = taxpayer.company,
                            phone = taxpayer.phone.orEmpty(),
                            registrationDate = date.toFormattedString()
                        )
                    }
                    _isLoading.update {
                        false
                    }

                }.onError {
                    _isLoading.update {
                        false
                    }
                    _errorException.tryEmit(it)
                }
            }

        }

    }
}