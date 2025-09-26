package com.blipblipcode.distribuidoraayl.domain.useCase.customer

import com.blipblipcode.distribuidoraayl.domain.models.ResultType
import com.blipblipcode.distribuidoraayl.domain.models.customer.Customer

interface ICreateOrUpdateCustomerUseCase {
    suspend operator fun invoke(customer: Customer): ResultType<Unit>
}