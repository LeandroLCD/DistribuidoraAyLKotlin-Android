package com.blipblipcode.distribuidoraayl.domain.useCase.products

import com.blipblipcode.distribuidoraayl.domain.models.ResultType
import com.blipblipcode.distribuidoraayl.domain.models.products.Category

interface ICreateCategoryUseCase {
    suspend operator fun invoke(category: Category): ResultType<Unit>
}