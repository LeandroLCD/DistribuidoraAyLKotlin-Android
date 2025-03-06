package com.blipblipcode.distribuidoraayl.ui.products.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blipblipcode.distribuidoraayl.domain.models.products.Category
import com.blipblipcode.distribuidoraayl.domain.models.products.ProductBrands
import com.blipblipcode.distribuidoraayl.domain.models.products.Udm
import com.blipblipcode.distribuidoraayl.domain.useCase.products.IGetCategoriesUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.products.IGetProductBrandsUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.products.IGetUdmUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class AddProductViewModel @Inject constructor(
    private val getCategoriesUseCase: dagger.Lazy<IGetCategoriesUseCase>,
    private val getUdmUseCase: dagger.Lazy<IGetUdmUseCase>,
    private val getProductBrandsUseCase: dagger.Lazy<IGetProductBrandsUseCase>
) : ViewModel() {

    /*States*/
    val categories = getCategoriesUseCase.get().invoke()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), emptyList())

    val udm = getUdmUseCase.get().invoke()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), emptyList())

    val productBrands = getProductBrandsUseCase.get().invoke()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), emptyList())

    /*Event*/

    fun onChangedBarcode(value: String) {

    }

    fun onChangedSKU(value: String) {

    }

    fun onChangedUdm(value: Udm) {

    }

    fun onChangedCategory(data: Category) {

    }

    fun onChangedBrands(data: ProductBrands) {

    }

}