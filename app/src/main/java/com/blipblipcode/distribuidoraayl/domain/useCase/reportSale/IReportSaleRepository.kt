package com.blipblipcode.distribuidoraayl.domain.useCase.reportSale

import androidx.paging.PagingData
import com.blipblipcode.distribuidoraayl.domain.models.ResultType
import com.blipblipcode.distribuidoraayl.domain.models.filters.DataFilter
import com.blipblipcode.distribuidoraayl.domain.models.reportSale.ReportSale
import com.blipblipcode.library.model.DateTimeRange
import kotlinx.coroutines.flow.Flow

interface IReportSaleRepository {

    suspend fun syncReportSales(): ResultType<Unit>

    fun getReportSales(range: DateTimeRange, dataFilter: List<DataFilter>): Flow<PagingData<ReportSale>>
}