package com.blipblipcode.distribuidoraayl.domain.useCase.products.impl

import com.blipblipcode.distribuidoraayl.domain.models.ResultType
import com.blipblipcode.distribuidoraayl.domain.models.products.Product
import com.blipblipcode.distribuidoraayl.domain.useCase.products.IGetProductUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.products.IProductsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class GetProductUseCase @Inject constructor(private val repository: IProductsRepository):
    IGetProductUseCase {

    override suspend fun invoke(uid: String): Flow<Product> {
        return repository.getProduct(uid)
    }

    override suspend fun byUid(uid: String): ResultType<Product> {
        return repository.getProductByUid(uid)
    }

    override suspend fun bySku(sku: String): ResultType<Product> {
        return repository.getProductBySku(sku)
    }

    override suspend fun byBarcode(barcode: String): ResultType<Product> {
        return repository.getProductByBarcode(barcode)
    }
}