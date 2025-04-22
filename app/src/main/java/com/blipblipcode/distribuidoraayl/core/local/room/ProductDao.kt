package com.blipblipcode.distribuidoraayl.core.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.blipblipcode.distribuidoraayl.core.local.entities.product.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(product: ProductEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(products: List<ProductEntity>)

    @Query("DELETE FROM products WHERE uid = :uid")
    suspend fun deleteById(uid: String)

    @Query("DELETE FROM products")
    suspend fun deleteAll()

    @Query("SELECT * FROM products WHERE uid = :uid")
    suspend fun getProduct(uid: String): ProductEntity?

    @Query("SELECT * FROM products WHERE sku = :sku")
    suspend fun getProductBySku(sku: String): ProductEntity?

    @Query("SELECT * FROM products WHERE barCode = :barCode")
    suspend fun getProductByBarCode(barCode: String): ProductEntity?

    @Query("SELECT * FROM products")
    fun getProducts(): Flow<List<ProductEntity>>
}