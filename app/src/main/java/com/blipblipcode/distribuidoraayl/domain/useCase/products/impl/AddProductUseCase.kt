package com.blipblipcode.distribuidoraayl.domain.useCase.products.impl

import com.blipblipcode.distribuidoraayl.domain.models.ResultType
import com.blipblipcode.distribuidoraayl.domain.models.products.Product
import com.blipblipcode.distribuidoraayl.domain.useCase.products.IAddProductUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.products.IProductsRepository
import javax.inject.Inject

internal class AddProductUseCase @Inject constructor(private val repository: IProductsRepository):
    IAddProductUseCase {
    override suspend fun invoke(product: Product): ResultType<Unit> {
        return repository.createProduct(product)
    }
}