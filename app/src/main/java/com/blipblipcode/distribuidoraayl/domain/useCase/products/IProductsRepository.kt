package com.blipblipcode.distribuidoraayl.domain.useCase.products

import com.blipblipcode.distribuidoraayl.domain.models.ResultType
import com.blipblipcode.distribuidoraayl.domain.models.products.Category
import com.blipblipcode.distribuidoraayl.domain.models.products.Product
import com.blipblipcode.distribuidoraayl.domain.models.products.ProductBrands
import com.blipblipcode.distribuidoraayl.domain.models.products.Udm
import kotlinx.coroutines.flow.Flow

interface IProductsRepository {

    suspend fun createBrand(brand: ProductBrands): ResultType<Unit>

    suspend fun createCategory(category: Category): ResultType<Unit>

    suspend fun createProduct(product: Product): ResultType<Unit>

    suspend fun updateProduct(product: Product): ResultType<Unit>

    suspend fun deleteProduct(product: Product): ResultType<Unit>

    fun getProduct(uid: String): Flow<Product>

    suspend fun getProductByBarcode(barcode: String): ResultType<Product>

    suspend fun getProductBySku(sku: String): ResultType<Product>

    fun getProducts(): Flow<List<Product>>

    fun getCategoryList(): Flow<List<Category>>

    fun getProductBrandsList(): Flow<List<ProductBrands>>

    fun getUds(): Flow<List<Udm>>

    suspend fun getProductByUid(uid: String): ResultType<Product>

    fun searchProducts(search: String): Flow<List<Product>>
}