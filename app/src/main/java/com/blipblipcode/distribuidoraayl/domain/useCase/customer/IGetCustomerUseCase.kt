package com.blipblipcode.distribuidoraayl.domain.useCase.customer

import com.blipblipcode.distribuidoraayl.domain.models.customer.Customer
import kotlinx.coroutines.flow.Flow

interface IGetCustomerUseCase {
    operator fun invoke(): Flow<List<Customer>>
}