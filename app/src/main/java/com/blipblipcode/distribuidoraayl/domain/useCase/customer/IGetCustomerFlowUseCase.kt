package com.blipblipcode.distribuidoraayl.domain.useCase.customer

import com.blipblipcode.distribuidoraayl.domain.models.customer.Customer
import kotlinx.coroutines.flow.Flow

interface IGetCustomerFlowUseCase {
    operator fun invoke(rut:String): Flow<Customer>
}