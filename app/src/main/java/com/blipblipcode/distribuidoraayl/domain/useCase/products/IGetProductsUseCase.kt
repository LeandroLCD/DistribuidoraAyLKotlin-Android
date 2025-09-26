package com.blipblipcode.distribuidoraayl.domain.useCase.products

import com.blipblipcode.distribuidoraayl.domain.models.products.Product
import kotlinx.coroutines.flow.Flow

interface IGetProductsUseCase {
    operator fun invoke():Flow<List<Product>>
}