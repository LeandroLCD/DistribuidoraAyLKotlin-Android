package com.blipblipcode.distribuidoraayl.ui.products.details


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.blipblipcode.distribuidoraayl.domain.models.onError
import com.blipblipcode.distribuidoraayl.domain.models.onSuccess
import com.blipblipcode.distribuidoraayl.domain.useCase.preferences.IGetEcommerceUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.products.ICreateCategoryUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.products.IGetCategoriesUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.products.IGetProductBrandsUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.products.IGetProductUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.products.IGetUdmUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.products.IUpdateProductUseCase
import com.blipblipcode.distribuidoraayl.ui.navigationGraph.routes.ProductScreen
import com.blipblipcode.distribuidoraayl.ui.products.ProductBaseViewModel
import com.blipblipcode.distribuidoraayl.ui.products.models.ProductModel
import com.blipblipcode.distribuidoraayl.ui.products.models.toModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getCategoriesUseCase: dagger.Lazy<IGetCategoriesUseCase>,
    getUdmUseCase: dagger.Lazy<IGetUdmUseCase>,
    getEcommerceUseCase: dagger.Lazy<IGetEcommerceUseCase>,
    getProductBrandsUseCase: dagger.Lazy<IGetProductBrandsUseCase>,
    addCategoryUseCase: dagger.Lazy<ICreateCategoryUseCase>,
    private val getProductUseCase: dagger.Lazy<IGetProductUseCase>,
    private val updateProductUseCase: dagger.Lazy<IUpdateProductUseCase>
    ): ProductBaseViewModel(
        getCategoriesUseCase,
        getUdmUseCase,
        getEcommerceUseCase,
        getProductBrandsUseCase,
        addCategoryUseCase,
        getProductUseCase
    ) {
        private val _productDetail = savedStateHandle.toRoute<ProductScreen.Detail>()

    private val _isEditable = MutableStateFlow(_productDetail.isEditable)
    val isEditable = _isEditable.asStateFlow()


    fun getProduct() {
        mIsLoading.tryEmit(true)
        viewModelScope.launch {
            getProductUseCase.get().byUid(_productDetail.uId).onSuccess {
                mProduct.tryEmit(it.toModel(0.0))
                mSku.tryEmit(it.sku)
                mBarcode.tryEmit(it.barCode)
                mIsLoading.tryEmit(false)
            }
        }
    }

    fun onEdict(){
        _isEditable.update {
            true
        }
        onStarted()
    }

    fun onSaveProduct(product: ProductModel, function: () -> Unit) {
        viewModelScope.launch {
            mIsLoading.tryEmit(true)
            updateProductUseCase.get().invoke(product.mapToDomain()).onSuccess {
                function.invoke()
            }.onError {
                mErrorException.tryEmit(it)
            }
            mIsLoading.tryEmit(false)

        }
    }

}