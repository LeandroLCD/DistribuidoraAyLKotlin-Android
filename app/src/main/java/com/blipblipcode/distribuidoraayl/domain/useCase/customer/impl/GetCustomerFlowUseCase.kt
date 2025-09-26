package com.blipblipcode.distribuidoraayl.domain.useCase.customer.impl

import com.blipblipcode.distribuidoraayl.domain.models.customer.Customer
import com.blipblipcode.distribuidoraayl.domain.useCase.customer.ICustomerRepository
import com.blipblipcode.distribuidoraayl.domain.useCase.customer.IGetCustomerFlowUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class GetCustomerFlowUseCase @Inject constructor(private val repository: ICustomerRepository) :
    IGetCustomerFlowUseCase {
    override fun invoke(rut: String): Flow<Customer>  = repository.getCustomerFlow(rut)
}