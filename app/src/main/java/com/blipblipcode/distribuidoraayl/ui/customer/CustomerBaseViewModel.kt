package com.blipblipcode.distribuidoraayl.ui.customer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blipblipcode.distribuidoraayl.domain.models.customer.Branch
import com.blipblipcode.distribuidoraayl.domain.models.customer.Commune
import com.blipblipcode.distribuidoraayl.domain.models.customer.Customer
import com.blipblipcode.distribuidoraayl.domain.models.customer.Region
import com.blipblipcode.distribuidoraayl.domain.models.customer.Route
import com.blipblipcode.distribuidoraayl.domain.models.customer.Rubro
import com.blipblipcode.distribuidoraayl.ui.auth.login.models.DataField
import com.blipblipcode.library.DateTime
import com.blipblipcode.library.model.FormatType
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

open class CustomerBaseViewModel(loadingIni:Boolean = false,
                                 internal val mErrorException: MutableStateFlow<Throwable?> = MutableStateFlow<Throwable?>(
                                     null
                                 )
): ViewModel() {

    /*States*/
    internal val misLoading = MutableStateFlow(loadingIni)
    val isLoading = misLoading.asStateFlow()

    val errorException = mErrorException.asStateFlow()
    internal open val mCustomer = MutableStateFlow(Customer())

    open val customer: StateFlow<Customer>
        get() = mCustomer.asStateFlow()

    internal val mBirthDate = MutableStateFlow(DataField(""))

    val birthDate = mBirthDate.asStateFlow().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(2000),
        DataField("")
    )
    private val _communes = MutableStateFlow(listOf<Commune>())
    val communes = _communes.asStateFlow()


    /*Event*/
    fun onNameChanged(value: String) {
        mCustomer.update { old ->
            old.copy(companyName = value)
        }
    }

    fun onPhoneChanged(value: String) {
        mCustomer.update { old ->
            old.copy(phone = value)
        }
    }

    fun onAddressChanged(value: String) {
        mCustomer.update { old ->
            old.copy(address = value)
        }
    }

    fun onBirthdayChanged(date: String) {
            try{

                val value = DateTime.fromString(date).format(FormatType.Short('/'))
                mBirthDate.update { old ->
                    old.copy(
                        value = value,
                        isError = false,
                        errorException = null
                    )
                }
                mCustomer.update {
                    it.copy(birthDate = date)
                }
            }catch (e:Throwable){
                e.printStackTrace()
                mBirthDate.update { old ->
                    old.copy(
                        value = date,
                        isError = true,
                        errorException = e
                    )
                }
            }

    }
    fun onBirthdayChanged(date: Long) {
        val value = DateTime.fromMillis(date).format(FormatType.Short('-'))
        mBirthDate.update { old ->
            old.copy(value = value)
        }
        mCustomer.update {
            it.copy(birthDate = value)
        }
    }

    fun onRegionChanged(region: Region) {
        _communes.update {
            region.communes
        }
        mCustomer.update { old ->
            old.copy(regionId = region.id)
        }
    }

    fun onRubrosChanged(data: Rubro) {
        mCustomer.update { old ->
            old.copy(rubro = data)
        }
    }

    fun onCommuneChanged(data: Commune) {
        mCustomer.update { old ->
            old.copy(commune = data.name)
        }

    }

    fun onRouteChanged(data: Route) {
        mCustomer.update {
            it.copy(routeId = data.uid)
        }
    }

    fun onChangedBranch(branch: Branch, sac: String) {
        mCustomer.update { old ->
            old.copy(branches = old.branches?.map {
                if (it.code == branch.code) {
                    it.copy(sapCode = sac)
                } else {
                    it
                }
            })
        }
    }


}