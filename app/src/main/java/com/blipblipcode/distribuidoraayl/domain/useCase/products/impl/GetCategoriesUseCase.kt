package com.blipblipcode.distribuidoraayl.domain.useCase.products.impl

import com.blipblipcode.distribuidoraayl.domain.models.products.Category
import com.blipblipcode.distribuidoraayl.domain.useCase.products.IGetCategoriesUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.products.IProductsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class GetCategoriesUseCase @Inject constructor(private val repository: IProductsRepository):
    IGetCategoriesUseCase {
    override fun invoke(): Flow<List<Category>> {
        return repository.getCategoryList()
    }
}