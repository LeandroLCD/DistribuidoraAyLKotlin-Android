package com.blipblipcode.distribuidoraayl.domain.useCase.openFactura

import com.blipblipcode.distribuidoraayl.domain.models.ResultType
import com.blipblipcode.distribuidoraayl.domain.models.sales.DocumentElectronic
import com.blipblipcode.distribuidoraayl.domain.models.sales.Payment
import com.blipblipcode.distribuidoraayl.domain.models.sales.Sale

interface IGenerateInvoiceUseCase {
    suspend operator fun invoke(
        payment: Payment,
        sale: Sale,
        isLetter: Boolean = false
    ): ResultType<DocumentElectronic>
}