package com.blipblipcode.distribuidoraayl.domain.useCase.customer.impl

import com.blipblipcode.distribuidoraayl.domain.useCase.customer.ICustomerRepository
import com.blipblipcode.distribuidoraayl.domain.useCase.customer.IDeleteCustomerUseCase

internal class DeleteCustomerUseCase(private val repository: ICustomerRepository) :
    IDeleteCustomerUseCase {
    override suspend operator fun invoke(rut: String) = repository.deleteCustomer(rut)
}