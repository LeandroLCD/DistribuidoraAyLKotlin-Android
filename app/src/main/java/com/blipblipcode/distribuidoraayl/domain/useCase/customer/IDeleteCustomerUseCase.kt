package com.blipblipcode.distribuidoraayl.domain.useCase.customer

import com.blipblipcode.distribuidoraayl.domain.models.ResultType

interface IDeleteCustomerUseCase {
    suspend operator  fun invoke(rut: String): ResultType<Unit>
}