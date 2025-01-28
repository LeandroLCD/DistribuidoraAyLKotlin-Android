package com.blipblipcode.distribuidoraayl.domain.useCase.customer.impl

import com.blipblipcode.distribuidoraayl.domain.useCase.customer.ICustomerRepository
import com.blipblipcode.distribuidoraayl.domain.useCase.customer.IGetCustomerUseCase
import javax.inject.Inject


internal class GetCustomerUseCase @Inject constructor(
    private val repository: ICustomerRepository
) : IGetCustomerUseCase {
    override operator fun invoke() = repository.getCustomers()
}