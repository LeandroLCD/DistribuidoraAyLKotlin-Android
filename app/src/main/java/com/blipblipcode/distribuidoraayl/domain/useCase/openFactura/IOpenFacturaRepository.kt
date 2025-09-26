package com.blipblipcode.distribuidoraayl.domain.useCase.openFactura

import com.blipblipcode.distribuidoraayl.domain.models.ResultType
import com.blipblipcode.distribuidoraayl.domain.models.of.Taxpayer
import com.blipblipcode.distribuidoraayl.domain.models.sales.DocumentElectronic
import com.blipblipcode.distribuidoraayl.domain.models.sales.Payment
import com.blipblipcode.distribuidoraayl.domain.models.sales.Sale

interface IOpenFacturaRepository {
    suspend fun getTaxpayer(rut: String): ResultType<Taxpayer>

    suspend fun generateInvoice(
        payment: Payment,
        sale: Sale,
        isLetter: Boolean
    ): ResultType<DocumentElectronic>
}