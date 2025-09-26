package com.blipblipcode.distribuidoraayl.domain.useCase.products.impl

import com.blipblipcode.distribuidoraayl.domain.models.products.ProductBrands
import com.blipblipcode.distribuidoraayl.domain.useCase.products.IGetProductBrandsUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.products.IProductsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class GetProductBrandsUseCase @Inject constructor(private val repository: IProductsRepository):
    IGetProductBrandsUseCase {
    override fun invoke(): Flow<List<ProductBrands>> {
        return repository.getProductBrandsList()
    }
}