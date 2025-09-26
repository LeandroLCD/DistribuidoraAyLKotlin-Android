package com.blipblipcode.distribuidoraayl.domain.useCase.products.impl

import com.blipblipcode.distribuidoraayl.domain.models.ResultType
import com.blipblipcode.distribuidoraayl.domain.models.products.Product
import com.blipblipcode.distribuidoraayl.domain.useCase.products.IProductsRepository
import com.blipblipcode.distribuidoraayl.domain.useCase.products.IUpdateProductUseCase
import javax.inject.Inject

internal class UpdateProductUseCase @Inject constructor(private val productRepository: IProductsRepository) :
    IUpdateProductUseCase {
    override suspend fun invoke(product: Product): ResultType<Unit> {
        return productRepository.updateProduct(product)
    }
}