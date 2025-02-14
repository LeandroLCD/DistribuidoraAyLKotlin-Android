package com.blipblipcode.distribuidoraayl.domain.useCase.customer.impl

import com.blipblipcode.distribuidoraayl.domain.useCase.customer.ICustomerRepository
import com.blipblipcode.distribuidoraayl.domain.useCase.customer.IDeleteCustomerUseCase
import javax.inject.Inject

internal class DeleteCustomerUseCase @Inject constructor(private val repository: ICustomerRepository) :
    IDeleteCustomerUseCase {
    override suspend operator fun invoke(rut: String) = repository.deleteCustomer(rut)
}