package com.blipblipcode.distribuidoraayl.domain.useCase.customer.impl

import com.blipblipcode.distribuidoraayl.domain.models.customer.Route
import com.blipblipcode.distribuidoraayl.domain.useCase.customer.ICustomerRepository
import com.blipblipcode.distribuidoraayl.domain.useCase.customer.IUpdateRouteUseCase
import javax.inject.Inject

class UpdateRouteUseCase @Inject constructor(private val repository: ICustomerRepository) :
    IUpdateRouteUseCase {
    override suspend operator fun invoke(route: Route) = repository.updateRoute(route)
}