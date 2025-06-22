package com.blipblipcode.distribuidoraayl.data.repositiry.product

import android.content.Context
import com.blipblipcode.distribuidoraayl.core.local.room.dao.ProductDao
import com.blipblipcode.distribuidoraayl.data.dto.products.CategoryDto
import com.blipblipcode.distribuidoraayl.data.dto.products.ProductBrandsDto
import com.blipblipcode.distribuidoraayl.data.dto.products.ProductDto
import com.blipblipcode.distribuidoraayl.data.dto.products.UdmDto
import com.blipblipcode.distribuidoraayl.data.mapper.toDto
import com.blipblipcode.distribuidoraayl.data.repositiry.BaseFireStoreRepository
import com.blipblipcode.distribuidoraayl.data.repositiry.remoteMediator
import com.blipblipcode.distribuidoraayl.domain.models.ResultType
import com.blipblipcode.distribuidoraayl.domain.models.products.Category
import com.blipblipcode.distribuidoraayl.domain.models.products.Product
import com.blipblipcode.distribuidoraayl.domain.models.products.ProductBrands
import com.blipblipcode.distribuidoraayl.domain.models.products.Udm
import com.blipblipcode.distribuidoraayl.domain.useCase.products.IProductsRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProductsRepository @Inject constructor(
    dispatcher: CoroutineDispatcher,
    context: Context,
    override val fireStore: FirebaseFirestore,
    private val remoteMediator: ProductRemoteMediator,
    private val productDao: ProductDao
):BaseFireStoreRepository(dispatcher, context, fireStore), IProductsRepository {

    companion object{
        const val CATALOGUE = "catalogue_products"
        const val CATEGORIES = "categories"
        const val BRANDS = "brands"
        const val UDM = "udm"
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

    override fun getProduct(uid: String): Flow<Product> {
        return getDocumentFlow<ProductDto, Product>(CATALOGUE, uid )
    }

    override suspend fun getProductByBarcode(barcode: String): ResultType<Product> {
        return makeCallDatabase {
            productDao.getProductByBarCode(barcode)!!.mapToDomain()
        }
    }

    override suspend fun getProductBySku(sku: String): ResultType<Product> {
        return makeCallDatabase {
            productDao.getProductBySku(sku)!!.mapToDomain()
        }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getProducts(): Flow<List<Product>> {

        return productDao.getProducts()
            .remoteMediator(remoteMediator)
            .map { listProductEntity ->
                listProductEntity.map { it.mapToDomain() }
            }
    }
    
    override fun getCategoryList(): Flow<List<Category>> {
        return getDocumentsFlow<CategoryDto, Category>(CATEGORIES)
    }

    override fun getProductBrandsList(): Flow<List<ProductBrands>> {
        return getDocumentsFlow<ProductBrandsDto, ProductBrands>(BRANDS)
    }

    override fun getUds(): Flow<List<Udm>>{
        return getDocumentsFlow<UdmDto, Udm>(UDM)
    }

    override suspend fun getProductByUid(uid: String): ResultType<Product> {
        return makeCallDatabase {
            productDao.getProduct(uid)!!.mapToDomain()
        }
    }

    override fun searchProducts(search: String): Flow<List<Product>> {
        return productDao.searchProducts(search).map { list->
            list.map { it.mapToDomain() }
        }
    }


}