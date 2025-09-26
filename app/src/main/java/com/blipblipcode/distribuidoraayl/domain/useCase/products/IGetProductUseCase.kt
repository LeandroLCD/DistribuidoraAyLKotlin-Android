package com.blipblipcode.distribuidoraayl.domain.useCase.products

import com.blipblipcode.distribuidoraayl.domain.models.ResultType
import com.blipblipcode.distribuidoraayl.domain.models.products.Product
import kotlinx.coroutines.flow.Flow

interface IGetProductUseCase {
    suspend operator fun invoke(
        uid: String
    ): Flow<Product>
    suspend fun byUid(uid:String):ResultType<Product>

    fun search(search:String):Flow<List<Product>>

    suspend fun bySku(sku:String):ResultType<Product>

    suspend fun byBarcode(barcode:String):ResultType<Product>
}