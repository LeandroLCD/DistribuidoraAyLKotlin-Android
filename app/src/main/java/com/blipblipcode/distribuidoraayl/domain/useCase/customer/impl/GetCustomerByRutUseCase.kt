package com.blipblipcode.distribuidoraayl.domain.useCase.customer.impl

import com.blipblipcode.distribuidoraayl.domain.models.customer.Customer
import com.blipblipcode.distribuidoraayl.domain.useCase.customer.ICustomerRepository
import com.blipblipcode.distribuidoraayl.domain.useCase.customer.IGetCustomerByRutUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class GetCustomerByRutUseCase @Inject constructor(private val repository: ICustomerRepository) :
    IGetCustomerByRutUseCase {
    override suspend operator fun invoke(rut:String) = repository.getCustomer(rut)
}