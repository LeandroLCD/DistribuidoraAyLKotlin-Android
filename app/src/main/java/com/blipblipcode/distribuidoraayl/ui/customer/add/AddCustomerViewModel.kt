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
import com.blipblipcode.distribuidoraayl.domain.useCase.openFactura.IGetTaxpayerUseCase
import com.blipblipcode.distribuidoraayl.ui.auth.login.models.DataField
import com.blipblipcode.distribuidoraayl.ui.customer.CustomerBaseViewModel
import com.blipblipcode.distribuidoraayl.ui.utils.removeAccents
import com.blipblipcode.distribuidoraayl.ui.utils.toFormattedString
import com.blipblipcode.distribuidoraayl.ui.widgets.choices.FieldChoice
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
) : CustomerBaseViewModel() {

    /*States*/
    private val rutRegex = "^\\d{6,9}-[0-9K]\$".toRegex()

    val regions = getRegionsUseCase.get().invoke()

    @OptIn(ExperimentalCoroutinesApi::class)
    val rubros = getRubrosUseCase.get().invoke().mapLatest { rubro ->
        rubro.sortedBy { it.id }.map{ FieldChoice(it, it.label()) }
    }
    val routes = getRoutsUseCase.get().invoke()



    private val _rut = MutableStateFlow(DataField("13351250-0"))
    val rut = _rut.asStateFlow()

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



    fun onSavedCustomer(customer: Customer, onCompleted: () -> Unit){
        misLoading.tryEmit(true)
        viewModelScope.launch {
            createOrUpdateCustomerUseCase.get().invoke(customer).onSuccess {
                onCompleted.invoke()
                misLoading.update {
                    false
                }
            }.onError {
                mErrorException.tryEmit(it)
                misLoading.update {
                    false
                }
            }
        }
    }

    fun onFindRut(rut:String){
        misLoading.tryEmit(true)
        viewModelScope.launch {
            val date = Date()

            getCustomerUseCase.get().invoke(rut).onSuccess {
                misLoading.update {
                    false
                }
                mErrorException.tryEmit(CustomerAlreadyExistsException())

            }.onError {
                getTaxpayerUseCase.get().invoke(rut).onSuccess { taxpayer->
                    val commune = taxpayer.commune.removeAccents()
                    val region = regions.first().find { region->
                        region.communes.any{
                            it.name.contains(commune, true)
                        } }
                    mCustomer.update { old ->
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
}