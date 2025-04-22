package com.blipblipcode.distribuidoraayl.domain.useCase.products.impl

import com.blipblipcode.distribuidoraayl.domain.models.products.Product
import com.blipblipcode.distribuidoraayl.domain.useCase.products.IGetProductsUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.products.IProductsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class GetProductsUseCase @Inject constructor(private val repository: IProductsRepository) :
    IGetProductsUseCase {
    override fun invoke(): Flow<List<Product>> = repository.getProducts()
}