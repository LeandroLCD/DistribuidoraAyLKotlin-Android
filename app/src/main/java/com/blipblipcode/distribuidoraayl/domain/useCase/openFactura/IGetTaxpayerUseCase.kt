package com.blipblipcode.distribuidoraayl.domain.useCase.openFactura

import com.blipblipcode.distribuidoraayl.domain.models.ResultType
import com.blipblipcode.distribuidoraayl.domain.models.of.Taxpayer

interface IGetTaxpayerUseCase {
    suspend operator fun invoke(rut: String): ResultType<Taxpayer>
}