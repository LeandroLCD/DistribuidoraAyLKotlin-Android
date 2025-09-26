package com.blipblipcode.distribuidoraayl.domain.useCase.customer.impl

import com.blipblipcode.distribuidoraayl.domain.useCase.customer.ICustomerRepository
import com.blipblipcode.distribuidoraayl.domain.useCase.customer.IGetRubrosUseCase
import javax.inject.Inject

internal class GetRubrosUseCase @Inject constructor(private val repository: ICustomerRepository) :
    IGetRubrosUseCase {
    override operator fun invoke() = repository.getRubros()
}