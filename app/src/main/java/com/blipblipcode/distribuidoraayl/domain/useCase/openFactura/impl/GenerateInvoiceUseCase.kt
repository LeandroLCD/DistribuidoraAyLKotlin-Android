package com.blipblipcode.distribuidoraayl.domain.useCase.openFactura.impl

import com.blipblipcode.distribuidoraayl.domain.models.ResultType
import com.blipblipcode.distribuidoraayl.domain.models.sales.DocumentElectronic
import com.blipblipcode.distribuidoraayl.domain.models.sales.Payment
import com.blipblipcode.distribuidoraayl.domain.models.sales.Sale
import com.blipblipcode.distribuidoraayl.domain.useCase.openFactura.IGenerateInvoiceUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.openFactura.IOpenFacturaRepository
import javax.inject.Inject

internal class GenerateInvoiceUseCase @Inject constructor(
    private val repository: IOpenFacturaRepository
):IGenerateInvoiceUseCase {
    override suspend fun invoke(
        payment: Payment,
        sale: Sale,
        isLetter: Boolean
    ): ResultType<DocumentElectronic> {
        return repository.generateInvoice(payment, sale, isLetter)
    }
}