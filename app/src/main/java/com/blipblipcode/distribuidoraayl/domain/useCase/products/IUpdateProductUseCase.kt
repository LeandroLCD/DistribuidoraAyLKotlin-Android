package com.blipblipcode.distribuidoraayl.domain.useCase.products

import com.blipblipcode.distribuidoraayl.domain.models.ResultType
import com.blipblipcode.distribuidoraayl.domain.models.products.Product

interface IUpdateProductUseCase {
    suspend fun invoke(product: Product): ResultType<Unit>
}