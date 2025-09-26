package com.blipblipcode.distribuidoraayl.domain.useCase.openFactura.impl

import com.blipblipcode.distribuidoraayl.domain.models.ResultType
import com.blipblipcode.distribuidoraayl.domain.models.of.Taxpayer
import com.blipblipcode.distribuidoraayl.domain.useCase.openFactura.IOpenFacturaRepository
import com.blipblipcode.distribuidoraayl.domain.useCase.openFactura.IGetTaxpayerUseCase
import javax.inject.Inject

internal class GetTaxpayerUseCase @Inject constructor(private val repository: IOpenFacturaRepository) :
    IGetTaxpayerUseCase {
    override suspend fun invoke(rut: String): ResultType<Taxpayer> {
        return repository.getTaxpayer(rut)
    }
}