package com.blipblipcode.distribuidoraayl.domain.useCase.customer

import com.blipblipcode.distribuidoraayl.domain.models.customer.Rubro
import kotlinx.coroutines.flow.Flow

interface IGetRubrosUseCase {
    operator fun invoke(): Flow<List<Rubro>>
}