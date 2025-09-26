package com.blipblipcode.distribuidoraayl.domain.useCase.products.impl

import com.blipblipcode.distribuidoraayl.domain.models.ResultType
import com.blipblipcode.distribuidoraayl.domain.models.products.Product
import com.blipblipcode.distribuidoraayl.domain.useCase.products.IDeleteProductsUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.products.IProductsRepository
import javax.inject.Inject

internal class DeleteProductsUseCase @Inject constructor(private val repository: IProductsRepository):
    IDeleteProductsUseCase {
    override suspend fun invoke(product: Product): ResultType<Unit> {
        return repository.deleteProduct(product)
    }
}