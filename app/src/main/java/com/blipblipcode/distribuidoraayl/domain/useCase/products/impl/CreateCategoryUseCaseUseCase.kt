package com.blipblipcode.distribuidoraayl.domain.useCase.products.impl

import com.blipblipcode.distribuidoraayl.domain.models.ResultType
import com.blipblipcode.distribuidoraayl.domain.models.products.Category
import com.blipblipcode.distribuidoraayl.domain.useCase.products.ICreateCategoryUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.products.IProductsRepository
import javax.inject.Inject

internal class CreateCategoryUseCaseUseCase @Inject constructor(private val repository: IProductsRepository):
    ICreateCategoryUseCase {
    override suspend fun invoke(category: Category): ResultType<Unit> {
        return repository.createCategory(category)
    }
}