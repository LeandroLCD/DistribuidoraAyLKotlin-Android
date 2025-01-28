package com.blipblipcode.distribuidoraayl.domain.useCase.customer.impl

import com.blipblipcode.distribuidoraayl.domain.models.customer.Route
import com.blipblipcode.distribuidoraayl.domain.useCase.customer.ICustomerRepository
import com.blipblipcode.distribuidoraayl.domain.useCase.customer.IGetRoutesUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class GetRoutesUseCase @Inject constructor(private val repository: ICustomerRepository) :
    IGetRoutesUseCase {
    override operator fun invoke(): Flow<List<Route>> {
        return repository.getRoutes()
    }
}