package com.blipblipcode.distribuidoraayl.domain.useCase.openFactura

import com.blipblipcode.distribuidoraayl.domain.models.ResultType
import com.blipblipcode.distribuidoraayl.domain.models.of.Taxpayer

interface IOpenFacturaRepository {
    suspend fun getTaxpayer(rut: String): ResultType<Taxpayer>

}