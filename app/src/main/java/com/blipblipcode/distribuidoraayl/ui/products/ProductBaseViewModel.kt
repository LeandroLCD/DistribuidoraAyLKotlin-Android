package com.blipblipcode.distribuidoraayl.ui.products

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blipblipcode.distribuidoraayl.domain.models.onError
import com.blipblipcode.distribuidoraayl.domain.models.onSuccess
import com.blipblipcode.distribuidoraayl.domain.models.products.Category
import com.blipblipcode.distribuidoraayl.domain.models.products.ProductBrands
import com.blipblipcode.distribuidoraayl.domain.models.products.Udm
import com.blipblipcode.distribuidoraayl.domain.throwable.BackendErrorException
import com.blipblipcode.distribuidoraayl.domain.throwable.BarcodeOrSkuAlreadyExistsException
import com.blipblipcode.distribuidoraayl.domain.throwable.FormatInvalidException
import com.blipblipcode.distribuidoraayl.domain.throwable.RequiredFieldException
import com.blipblipcode.distribuidoraayl.domain.useCase.preferences.IGetEcommerceUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.products.ICreateCategoryUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.products.IGetCategoriesUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.products.IGetProductBrandsUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.products.IGetProductUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.products.IGetUdmUseCase
import com.blipblipcode.distribuidoraayl.ui.auth.login.models.DataField
import com.blipblipcode.distribuidoraayl.ui.products.models.ProductModel
import com.blipblipcode.library.throwable.InvalidFormatException
import dagger.Lazy
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

open class ProductBaseViewModel(
    getCategoriesUseCase: Lazy<IGetCategoriesUseCase>,
    getUdmUseCase: Lazy<IGetUdmUseCase>,
    getEcommerceUseCase: Lazy<IGetEcommerceUseCase>,
    getProductBrandsUseCase: Lazy<IGetProductBrandsUseCase>,
    private val addCategoryUseCase: Lazy<ICreateCategoryUseCase>,
    private val getProductUseCase: Lazy<IGetProductUseCase>
) : ViewModel() {

    protected val mProduct = MutableStateFlow(ProductModel(0.0))

    val product = getEcommerceUseCase.get().invoke()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), null)
        .combineTransform(mProduct) { eCommerce, productModel ->
            emit(productModel.copy(iva = eCommerce?.iva ?: 0.0))
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), ProductModel(0.0))

    val categories = getCategoriesUseCase.get().invoke()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), emptyList())

    val udm = getUdmUseCase.get().invoke()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), emptyList())

    val productBrands = getProductBrandsUseCase.get().invoke()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), emptyList())

    protected val mErrorException = MutableStateFlow<Throwable?>(null)
    val errorException = mErrorException.asStateFlow()

    protected val mIsLoading = MutableStateFlow(false)
    val isLoading = mIsLoading.asStateFlow()

    protected val mSku = MutableStateFlow("")
    val sku = mSku.asStateFlow()

    protected val mBarcode = MutableStateFlow("")
    val barcode = mBarcode.asStateFlow()


    private lateinit var skuJob: Job
    private lateinit var barcodeJob: Job

    @OptIn(FlowPreview::class)
    open fun onStarted() {
        skuJob = viewModelScope.launch {
            mSku.debounce(300L).collectLatest { value ->
                val isValid = value.length in 6..10
                val exception = when {
                    value.isBlank() -> RequiredFieldException()
                    isValid -> null
                    else -> FormatInvalidException("SKU")
                }
                mProduct.update {
                    it.copy(sku = DataField(value, !isValid, exception))
                }
                if (isValid) {
                    getProductUseCase.get().bySku(value).onSuccess {
                        val product = mProduct.value
                        val isError = it.uid != product.uid
                        mProduct.tryEmit(
                            product.copy(
                                sku = DataField(
                                    value,
                                    isError,
                                    if (isError) BarcodeOrSkuAlreadyExistsException()
                                    else null
                                )
                            )
                        )

                    }.onError {
                        Log.d("AddProductViewModel", "onChangedSKU: $it")
                    }
                }
            }
        }
        barcodeJob = viewModelScope.launch {
            mBarcode.debounce(300L).collectLatest { value ->
                val isValid = value.length in 12..13
                val exception = when {
                    !isValid && value.isNotBlank() -> FormatInvalidException("BarCode")
                    else -> null
                }
                mProduct.update {
                    it.copy(barCode = DataField(value, !isValid, exception))
                }
                if (isValid) {
                    getProductUseCase.get().byBarcode(value).onSuccess {
                        val product = mProduct.value
                        val isError = it.uid != product.uid
                        mProduct.tryEmit(
                            product.copy(
                                barCode = DataField(
                                    value,
                                    isError,
                                    if (isError) BarcodeOrSkuAlreadyExistsException()
                                    else null
                                )
                            )
                        )
                    }
                }
            }
        }
    }

    open fun onDisposed() {
        if (::skuJob.isInitialized) {
            skuJob.cancel()
        }
        if (::barcodeJob.isInitialized) {
            barcodeJob.cancel()
        }
    }

    fun onChangedBarcode(value: String) {
        mBarcode.update {
            value
        }
    }

    fun onChangedSKU(value: String) {
        mSku.update {
            value
        }
    }

    fun onChangedUdm(value: Udm) {
        mProduct.update {
            it.copy(udm = DataField(value))
        }

    }

    fun onChangedCategory(data: Category) {
        mProduct.update {
            it.copy(category = DataField(data))
        }
    }

    fun onChangedBrands(data: ProductBrands) {
        mProduct.update {
            it.copy(brandId = DataField(data.uid))
        }
    }

    fun onError(error: String?) {
        mErrorException.update {
            if (error != null) {
                BackendErrorException(error)
            } else {
                null
            }
        }
    }

    fun onChangedName(value: String) {
        mProduct.update {
            val isValid = it.name.value.isNotBlank()
            val exception = if (isValid) null else RequiredFieldException()
            it.copy(name = DataField(value, !isValid, exception))
        }
    }

    fun onChangedDescription(value: String) {
        mProduct.update {
            it.copy(description = DataField(value))
        }
    }

    fun onChangedOffer(value: Boolean) {
        mProduct.update {
            it.copy(offer = it.offer.copy(value = it.offer.value.copy(isActive = value)))
        }
    }

    fun onChangedOfferValue(value: String) {
        val percentage = value.toDoubleOrNull() ?: 0.0
        val isValid = percentage in 0.0..100.0
        mProduct.update {
            it.copy(
                offer = it.offer.copy(
                    value = it.offer.value.copy(percentage = percentage),
                    isError = !isValid,
                    errorException = if (isValid) null else InvalidFormatException("Invalid percentage")
                )
            )
        }

    }

    fun onChangedGrossPrice(value: String) {
        mProduct.update {
            val number = value.toDoubleOrNull() ?: 0.0
            val exception = if (number > 0.0) null else RequiredFieldException()
            it.copy(grossPrice = DataField(number, number == 0.0, exception))
        }
    }

    fun onAddCategory(value: String) {
        viewModelScope.launch {
            mIsLoading.tryEmit(true)
            addCategoryUseCase.get().invoke(Category(value)).onSuccess {
                mIsLoading.tryEmit(false)
            }.onError {
                mIsLoading.tryEmit(false)
                mErrorException.tryEmit(it)

            }
        }
    }
}