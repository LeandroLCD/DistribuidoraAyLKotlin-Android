package com.blipblipcode.distribuidoraayl.domain.useCase.customer

import com.blipblipcode.distribuidoraayl.domain.models.ResultType
import com.blipblipcode.distribuidoraayl.domain.models.customer.Customer
import kotlinx.coroutines.flow.Flow

interface IGetCustomerByRutUseCase {
    suspend operator fun invoke(rut: String): ResultType<Customer>
}