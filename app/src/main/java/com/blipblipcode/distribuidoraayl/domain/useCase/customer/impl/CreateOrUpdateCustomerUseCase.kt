package com.blipblipcode.distribuidoraayl.domain.useCase.customer.impl

import com.blipblipcode.distribuidoraayl.domain.models.customer.Customer
import com.blipblipcode.distribuidoraayl.domain.useCase.customer.ICreateOrUpdateCustomerUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.customer.ICustomerRepository
import javax.inject.Inject

internal class CreateOrUpdateCustomerUseCase @Inject constructor(private val repository: ICustomerRepository) :
    ICreateOrUpdateCustomerUseCase {
    override suspend operator fun invoke(customer: Customer) = repository.createOrUpdateCustomer(customer)
}