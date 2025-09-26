package com.blipblipcode.distribuidoraayl.domain.useCase.customer.impl

import com.blipblipcode.distribuidoraayl.domain.models.customer.Route
import com.blipblipcode.distribuidoraayl.domain.useCase.customer.ICreateRouteUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.customer.ICustomerRepository
import javax.inject.Inject


internal class CreateRouteUseCase @Inject constructor(private val repository: ICustomerRepository) :
    ICreateRouteUseCase {

    override suspend operator fun invoke(route: Route) = repository.createRoute(route)
}