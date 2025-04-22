package com.blipblipcode.distribuidoraayl.ui.products.add

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.blipblipcode.distribuidoraayl.domain.models.onError
import com.blipblipcode.distribuidoraayl.domain.models.onSuccess
import com.blipblipcode.distribuidoraayl.domain.useCase.preferences.IGetEcommerceUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.products.IAddProductUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.products.ICreateCategoryUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.products.IGetCategoriesUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.products.IGetProductBrandsUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.products.IGetProductUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.products.IGetProductsUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.products.IGetUdmUseCase
import com.blipblipcode.distribuidoraayl.ui.products.ProductBaseViewModel
import com.blipblipcode.distribuidoraayl.ui.products.models.ProductModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@OptIn(FlowPreview::class)
@HiltViewModel
class AddProductViewModel @Inject constructor(
    getCategoriesUseCase: dagger.Lazy<IGetCategoriesUseCase>,
    getUdmUseCase: dagger.Lazy<IGetUdmUseCase>,
    getEcommerceUseCase: dagger.Lazy<IGetEcommerceUseCase>,
    getProductBrandsUseCase: dagger.Lazy<IGetProductBrandsUseCase>,
    addCategoryUseCase: dagger.Lazy<ICreateCategoryUseCase>,
    getProductUseCase: dagger.Lazy<IGetProductUseCase>,
    private val addProductUseCase: dagger.Lazy<IAddProductUseCase>,
    private val getProductsUseCase: dagger.Lazy<IGetProductsUseCase>
) : ProductBaseViewModel(
    getCategoriesUseCase,
    getUdmUseCase,
    getEcommerceUseCase,
    getProductBrandsUseCase,
    addCategoryUseCase,
    getProductUseCase) {


    /*Event*/

    init {


        viewModelScope.launch {
            getProductsUseCase.get().invoke().collect { productList ->
                val products = productList.associate { it.sku to it.name }
                Log.d("ProductRemoteMediator", "Products: $products.")
            }
        }
    }

    fun onSaveProduct(product: ProductModel, onSuccess: () -> Unit) {
        mIsLoading.tryEmit(true)
        viewModelScope.launch {
            addProductUseCase.get().invoke(product.mapToDomain())
                .onSuccess {
                    mProduct.tryEmit(ProductModel(0.19))
                    mSku.tryEmit("")
                    mBarcode.tryEmit("")
                    onSuccess.invoke()
                }
                .onError {
                    mErrorException.update {
                        it
                    }
                }
            mIsLoading.tryEmit(false)
        }
    }



}