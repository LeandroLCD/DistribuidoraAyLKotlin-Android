package com.blipblipcode.distribuidoraayl.ui.products.list

import androidx.lifecycle.viewModelScope
import com.blipblipcode.distribuidoraayl.domain.models.onError
import com.blipblipcode.distribuidoraayl.domain.models.products.Product
import com.blipblipcode.distribuidoraayl.domain.useCase.products.IDeleteProductsUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.products.IGetProductsUseCase
import com.blipblipcode.distribuidoraayl.ui.customer.models.FilterViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.reflect.full.memberProperties

@HiltViewModel
class ProductListViewModel @Inject constructor(
    getProductsUseCase: dagger.Lazy<IGetProductsUseCase>,
    private val deleteProductsUseCase: dagger.Lazy<IDeleteProductsUseCase>
): FilterViewModel<Product>() {

    val products = getProductsUseCase.get().invoke()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(1_000), listOf())

    /*State*/
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorException = MutableStateFlow<Throwable?>(null)
    val errorException = _errorException.asStateFlow()
    override fun getFieldValue(data: Product, field: String): Any? {
        return data::class.memberProperties
            .firstOrNull { it.name == field }
            ?.getter
            ?.call(data)
    }
    fun onDeleteProduct(item:Product){
        _isLoading.tryEmit(true)
        viewModelScope.launch {
            deleteProductsUseCase.get().invoke(item).onError {
                _errorException.tryEmit(it)
            }

            _isLoading.tryEmit(false)
        }
    }
}