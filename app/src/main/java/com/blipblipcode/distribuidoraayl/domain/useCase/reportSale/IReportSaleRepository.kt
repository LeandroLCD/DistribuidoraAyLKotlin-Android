package com.blipblipcode.distribuidoraayl.domain.useCase.reportSale

import com.blipblipcode.distribuidoraayl.domain.models.ResultType

interface IReportSaleRepository {
    suspend fun syncReportSales(): ResultType<Unit>
}