package com.blipblipcode.distribuidoraayl.domain.useCase.products

import com.blipblipcode.distribuidoraayl.domain.models.products.Category
import kotlinx.coroutines.flow.Flow

interface IGetCategoriesUseCase {
    operator fun invoke(): Flow<List<Category>>
}