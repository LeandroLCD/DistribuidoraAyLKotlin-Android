package com.blipblipcode.distribuidoraayl.domain.useCase.customer

import com.blipblipcode.distribuidoraayl.domain.models.ResultType
import com.blipblipcode.distribuidoraayl.domain.models.customer.Route

interface IUpdateRouteUseCase {
    suspend operator fun invoke(route: Route): ResultType<Unit>
}