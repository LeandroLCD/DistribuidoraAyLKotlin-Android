package com.blipblipcode.distribuidoraayl.ui.sales

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blipblipcode.distribuidoraayl.domain.models.customer.Activity
import com.blipblipcode.distribuidoraayl.domain.models.customer.Branch
import com.blipblipcode.distribuidoraayl.domain.models.customer.Customer
import com.blipblipcode.distribuidoraayl.domain.models.customer.Route
import com.blipblipcode.distribuidoraayl.domain.models.onError
import com.blipblipcode.distribuidoraayl.domain.models.onSuccess
import com.blipblipcode.distribuidoraayl.domain.models.preferences.ECommerce
import com.blipblipcode.distribuidoraayl.domain.models.products.Category
import com.blipblipcode.distribuidoraayl.domain.models.products.Product
import com.blipblipcode.distribuidoraayl.domain.models.sales.ClientReceiver
import com.blipblipcode.distribuidoraayl.domain.models.sales.Payment
import com.blipblipcode.distribuidoraayl.domain.models.sales.Sale
import com.blipblipcode.distribuidoraayl.domain.models.sales.SalesItem
import com.blipblipcode.distribuidoraayl.domain.models.sales.Totals
import com.blipblipcode.distribuidoraayl.domain.throwable.BackendErrorException
import com.blipblipcode.distribuidoraayl.domain.useCase.customer.IGetCustomersUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.customer.IGetRoutesUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.openFactura.IGenerateInvoiceUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.pdfManager.IGeneratePreviewUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.preferences.IGetEcommerceUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.products.IGetCategoriesUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.products.IGetProductUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.products.IGetProductsUseCase
import com.blipblipcode.distribuidoraayl.ui.sales.models.ProductQuantity
import com.blipblipcode.distribuidoraayl.ui.sales.models.ProductSelected
import com.blipblipcode.distribuidoraayl.ui.sales.models.SaleUiState
import com.blipblipcode.library.DateTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class SaleViewModel @Inject constructor(
    getProductsUseCase: dagger.Lazy<IGetProductsUseCase>,
    getCustomersFlowUseCase: dagger.Lazy<IGetCustomersUseCase>,
    getRoutesUseCase: dagger.Lazy<IGetRoutesUseCase>,
    getEcommerceUseCase: dagger.Lazy<IGetEcommerceUseCase>,
    getCategoriesUseCase: dagger.Lazy<IGetCategoriesUseCase>,
    private val getProductUseCase: dagger.Lazy<IGetProductUseCase>,
    private val generatePreviewUseCase: dagger.Lazy<IGeneratePreviewUseCase>,
    private val generateInvoiceUseCase: dagger.Lazy<IGenerateInvoiceUseCase>
) : ViewModel() {
    /*Event*/

    private val _uiState = MutableStateFlow<SaleUiState>(SaleUiState.NewSale)
    val uiState = _uiState.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val ecommerce = getEcommerceUseCase.get().invoke()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000L), null)

    val categories = getCategoriesUseCase.get().invoke()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000L), emptyList())

    private val _date = MutableStateFlow(DateTime.now())
    val date = _date.asStateFlow()

    private val _routeSelected = MutableStateFlow<Route?>(null)
    val routeSelected = _routeSelected.asStateFlow()

    val routes = getRoutesUseCase.get().invoke()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000L), emptyList())

    private val _customerSelected = MutableStateFlow<Customer?>(null)
    val customerSelected = _customerSelected.asStateFlow()

    private val _categorySelected = MutableStateFlow<Category?>(null)
    val categorySelected = _categorySelected.asStateFlow()

    private val _searchCustomer = MutableStateFlow("")
    val searchCustomer = _searchCustomer.asStateFlow()

    private val _activitySelected = MutableStateFlow<Activity?>(null)
    val activitySelected = _activitySelected.asStateFlow()

    private val _branchSelected = MutableStateFlow<Branch?>(null)
    val branchSelected = _branchSelected.asStateFlow()

    private val _errorException = MutableStateFlow<Throwable?>(null)
    val errorException = _errorException.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    @OptIn(FlowPreview::class)
    val customers = getCustomersFlowUseCase.get().invoke()
        .combine(_routeSelected) { list, route ->
            if (route != null) {
                list.filter { it.routeId == route.uid }
            } else {
                list
            }
        }
        .combine(_searchCustomer.debounce(300L)) { list, search ->
            if (search.isNotEmpty()) {
                list.filter {
                    it.companyName.contains(search, true) || it.rut.contains(search, true)
                }
            } else {
                list
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000L), emptyList())


    private val card = MutableStateFlow<Map<String, Pair<Int, Boolean>>>(emptyMap())

    private val _products = getProductsUseCase.get().invoke()

    private val _productsSelected = mutableSetOf<String>()
    private val _productsSelectedFlow = MutableStateFlow(_productsSelected.toSet())

    val catalogue = _products.combine(_categorySelected) { products, category ->
        if (category != null) {
            products.filter { it.category?.uid == category.uid }
        } else {
            products
        }
    }.combine(_productsSelectedFlow) { products, selects ->
        products.map {
            ProductSelected(
                product = it,
                isSelected = selects.contains(it.uid)
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000L), emptyList())

    val productsInCard = _products.combine(card) { products, quantities ->
        products.filter { quantities.containsKey(it.uid) }.map {
            ProductQuantity(
                product = it,
                quantity = quantities[it.uid]?.first ?: 1,
                isOffer = quantities[it.uid]?.second ?: it.offer.isActive,
                onChangedOffer = { of ->
                    val new = quantities.toMutableMap()
                    new[it.uid] = new[it.uid]?.copy(second = of) ?: Pair(1, of)
                    card.tryEmit(new.toMap())
                }
            ) { qt ->
                val new = quantities.toMutableMap()
                new[it.uid] = new[it.uid]?.copy(first = qt) ?: Pair(qt, it.offer.isActive)
                card.tryEmit(new.toMap())
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000L), emptyList())

    val totals = productsInCard.combine(ecommerce) { products, ecommerce ->
        val iva = ecommerce?.iva ?: 0.0
        Totals(
            netAmount = products.sumOf { it.totalPrice.value },
            tax = iva.times(100).toInt(),
            taxAmount = products.sumOf { it.totalPrice.value }.times(iva).roundToInt(),
            periodicAmount = products.sumOf { it.totalPrice.value }.times(1 + iva).roundToInt(),
            total = products.sumOf { it.totalPrice.value }.times(1 + iva).roundToInt()
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000L), Totals())

    private val _search = MutableStateFlow("")
    val search = _search.asStateFlow()

    private val _searchProduct = MutableStateFlow("")
    val searchProduct = _searchProduct.asStateFlow()

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val productsFount = _search.debounce(300L).flatMapLatest {
        if (it.isBlank()) {
            flowOf<List<Product>>(emptyList())
        } else {
            getProductUseCase.get().search(it)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000L), emptyList())

    private val _isLetter = MutableStateFlow(true)
    val isLetter = _isLetter.asStateFlow()

    /*State*/


    fun onDeleteProduct(uid: String) {
        card.update {
            val copy = it.toMutableMap()
            copy.remove(uid)
            copy.toMap()
        }
    }

    fun onAddProduct(product: Product) {
        card.update {
            val copy = it.toMutableMap()
            if (copy.containsKey(product.uid)) {
                val old = copy[product.uid]
                copy[product.uid] = old!!.first + 1 to old.second
            } else {
                copy[product.uid] = 1 to product.offer.isActive
            }
            copy.toMap()
        }
    }

    fun onSearch(value: String) {
        _search.update {
            value
        }
    }

    fun onSearchCustomer(value: String) {
        _searchCustomer.update {
            value
        }
    }

    fun onDateChanged(date: DateTime) {
        _date.update {
            date
        }
    }

    fun onCustomerSelected(customer: Customer) {
        _customerSelected.update {
            customer
        }
        customer.activities?.let {
            onActivitySelected(it.first())
        }
        customer.branches?.let {
            onBranchSelected(it.first())
        }
    }

    fun onRouteSelected(route: Route?) {
        _routeSelected.update {
            route
        }
    }

    fun onActivitySelected(activity: Activity) {
        _activitySelected.update {
            activity
        }
    }

    fun onBranchSelected(branch: Branch) {
        _branchSelected.update {
            branch
        }
    }

    fun onError(value: String?) {
        if (!value.isNullOrBlank()) {
            _errorException.update {
                BackendErrorException(value)
            }
        }
    }

    fun onCategorySelected(category: Category?) {
        _categorySelected.update {
            category
        }
    }

    fun onSearchProduct(value: String) {
        _searchProduct.update {
            value
        }
    }

    fun onProductSelected(product: Product) {
        if (!_productsSelected.add(product.uid)) {
            _productsSelected.remove(product.uid)
        }
        _productsSelectedFlow.update {
            _productsSelected.toSet()
        }
    }

    fun onAddProducts(selects: List<ProductSelected>) {
        clearProductSelected()
        card.update {
            val copy = it.toMutableMap()
            selects.forEach {
                if (copy.containsKey(it.product.uid)) {
                    val old = copy[it.product.uid]
                    copy[it.product.uid] = old!!.first + 1 to old.second
                } else {
                    copy[it.product.uid] = 1 to it.product.offer.isActive
                }
            }

            copy.toMap()
        }
    }

    fun clearProductSelected() {
        _productsSelected.clear()
        _productsSelectedFlow.update {
            _productsSelected.toSet()
        }
    }

    fun onSale(
        customer: Customer,
        activity: Activity?,
        branch: Branch?,
        date: DateTime,
        eCommerce: ECommerce,
        total: Totals,
        isLetter: Boolean
    ) {
        Log.d("SaleViewModel", "onSale: $isLetter")
        viewModelScope.launch {
            val sale = Sale(
                date = date,
                eCommerce = eCommerce,
                receiver = customer.run {
                    ClientReceiver(
                        rut = rut,
                        address = branch?.address ?: address,
                        name = companyName,
                        turn = activity?.turn,
                        commune = branch?.commune ?: customer.commune
                    )
                },
                items = productsInCard.value.mapIndexed { id, it ->
                    SalesItem(
                        index = id.plus(1),
                        sku = it.product.sku,
                        barCode = it.product.barCode,
                        name = it.product.name,
                        description = it.product.description,
                        price = it.totalPrice.value,
                        quantity = it.quantity
                    )
                },
                totals = total
            )
            generatePreviewUseCase.get().invoke(sale, isLetter).onError {
                _errorException.tryEmit(it)
            }.onSuccess { doc ->
                onUiChanged(SaleUiState.PreviewSale(doc))
            }

        }
    }

    fun onUiChanged(ui: SaleUiState) {
        _uiState.update {
            ui
        }
    }

    fun onGenerateDte(payment: Payment, sale: Sale, isLetter: Boolean) {
        viewModelScope.launch {
            _isLoading.tryEmit(true)
            generateInvoiceUseCase.get().invoke(payment, sale, isLetter).onError {
                _errorException.tryEmit(it)
            }.onSuccess {
                clearSale()
                onUiChanged(SaleUiState.FinishSale(it))
            }
            _isLoading.tryEmit(false)
        }
    }

    fun clearSale() {
        _date.update {
            DateTime.now()
        }
        _customerSelected.update {
            null
        }
        _activitySelected.update {
            null
        }
        _branchSelected.update {
            null
        }
        card.update {
            emptyMap()
        }
        clearProductSelected()
    }

    fun onLetterChanged(bool: Boolean) {
        Log.d("SaleViewModel", "onLetterChanged: $bool")
        _isLetter.tryEmit(bool)
    }
}