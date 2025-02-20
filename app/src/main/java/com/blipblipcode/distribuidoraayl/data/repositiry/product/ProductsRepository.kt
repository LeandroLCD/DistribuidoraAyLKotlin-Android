package com.blipblipcode.distribuidoraayl.data.repositiry.product

import android.content.Context
import com.blipblipcode.distribuidoraayl.data.dto.products.CategoryDto
import com.blipblipcode.distribuidoraayl.data.dto.products.ProductBrandsDto
import com.blipblipcode.distribuidoraayl.data.dto.products.ProductDto
import com.blipblipcode.distribuidoraayl.data.mapper.toDto
import com.blipblipcode.distribuidoraayl.data.repositiry.BaseFireStoreRepository
import com.blipblipcode.distribuidoraayl.domain.models.ResultType
import com.blipblipcode.distribuidoraayl.domain.models.products.Category
import com.blipblipcode.distribuidoraayl.domain.models.products.Product
import com.blipblipcode.distribuidoraayl.domain.models.products.ProductBrands
import com.blipblipcode.distribuidoraayl.domain.useCase.products.IProductsRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProductsRepository @Inject constructor(
    dispatcher: CoroutineDispatcher,
    context: Context,
    override val fireStore: FirebaseFirestore
):BaseFireStoreRepository(dispatcher, context, fireStore), IProductsRepository {
    companion object{
        const val CATALOGUE = "catalogue_products"
        const val CATEGORIES = "categories"
        const val BRANDS = "brands"
    }

    override suspend fun createBrand(brand: ProductBrands): ResultType<Unit> {
        return makeCallNetwork {
            fireStore.collection(BRANDS).add(brand.toDto()).await()
        }
    }

    override suspend fun createCategory(category: Category): ResultType<Unit> {
        return makeCallNetwork {
            fireStore.collection(CATEGORIES).add(category.toDto()).await()
        }
    }

    override suspend fun createProduct(product: Product): ResultType<Unit> {
        return makeCallNetwork {
            fireStore.collection(CATALOGUE).add(product.toDto()).await()
        }
    }

    override suspend fun updateProduct(product: Product): ResultType<Unit> {
        return makeCallNetwork {
            val ref = fireStore.collection(CATALOGUE).document(product.uid)
            ref.set(product.toDto()).await()
        }
    }

    override suspend fun deleteProduct(product: Product):ResultType<Unit>{
        return makeCallNetwork {
            fireStore.collection(CATALOGUE).document(product.uid).delete().await()
        }

    }

    override fun getProducts(uid: String): Flow<Product> {
        return getDocumentFlow<ProductDto, Product>(CATALOGUE, uid )
    }

    override fun getCategoryList(): Flow<List<Category>> {
        return getDocumentsFlow<CategoryDto, Category>(CATEGORIES)
    }

    override fun getProductBrandsList(): Flow<List<ProductBrands>> {
        return getDocumentsFlow<ProductBrandsDto, ProductBrands>(BRANDS)
    }


}