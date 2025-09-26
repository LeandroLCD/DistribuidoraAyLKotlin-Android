package com.blipblipcode.distribuidoraayl.domain.useCase.customer.impl

import com.blipblipcode.distribuidoraayl.domain.useCase.customer.ICustomerRepository
import com.blipblipcode.distribuidoraayl.domain.useCase.customer.IGetRegionsUseCase
import javax.inject.Inject

internal class GetRegionsUseCase @Inject constructor(private val repository: ICustomerRepository) :
    IGetRegionsUseCase {
    override operator fun invoke() = repository.getRegions()
}