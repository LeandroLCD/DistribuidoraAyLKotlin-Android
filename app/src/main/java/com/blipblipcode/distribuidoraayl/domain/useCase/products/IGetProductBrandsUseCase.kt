package com.blipblipcode.distribuidoraayl.domain.useCase.products

import com.blipblipcode.distribuidoraayl.domain.models.products.ProductBrands
import kotlinx.coroutines.flow.Flow

interface IGetProductBrandsUseCase {
    operator fun invoke(): Flow<List<ProductBrands>>
}