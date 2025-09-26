package com.blipblipcode.distribuidoraayl.domain.useCase.reportSale.impl

import androidx.paging.PagingData
import com.blipblipcode.distribuidoraayl.domain.models.filters.DataFilter
import com.blipblipcode.distribuidoraayl.domain.models.reportSale.ReportSale
import com.blipblipcode.distribuidoraayl.domain.useCase.reportSale.IGetReportSaleUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.reportSale.IReportSaleRepository
import com.blipblipcode.library.model.DateTimeRange
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class GetReportSaleUseCase @Inject constructor(
    private val repository: IReportSaleRepository
):IGetReportSaleUseCase{
    override fun invoke(date: DateTimeRange, filter: List<DataFilter>): Flow<PagingData<ReportSale>> {
        return repository.getReportSales(date, filter)
    }
}