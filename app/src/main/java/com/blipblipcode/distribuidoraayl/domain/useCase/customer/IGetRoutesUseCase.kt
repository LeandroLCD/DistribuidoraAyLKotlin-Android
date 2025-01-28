package com.blipblipcode.distribuidoraayl.domain.useCase.customer

import com.blipblipcode.distribuidoraayl.domain.models.customer.Route
import kotlinx.coroutines.flow.Flow

interface IGetRoutesUseCase {
    operator fun invoke(): Flow<List<Route>>
}