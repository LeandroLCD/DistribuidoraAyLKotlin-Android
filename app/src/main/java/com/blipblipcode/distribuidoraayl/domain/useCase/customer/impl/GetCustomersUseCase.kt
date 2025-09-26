package com.blipblipcode.distribuidoraayl.domain.useCase.customer.impl

import com.blipblipcode.distribuidoraayl.domain.useCase.customer.ICustomerRepository
import com.blipblipcode.distribuidoraayl.domain.useCase.customer.IGetCustomersUseCase
import javax.inject.Inject


internal class GetCustomersUseCase @Inject constructor(
    private val repository: ICustomerRepository
) : IGetCustomersUseCase {
    override operator fun invoke() = repository.getCustomers()
}