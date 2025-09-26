package com.blipblipcode.distribuidoraayl.domain.useCase.reportSale

import androidx.paging.PagingData
import com.blipblipcode.distribuidoraayl.domain.models.filters.DataFilter
import com.blipblipcode.distribuidoraayl.domain.models.reportSale.ReportSale
import com.blipblipcode.library.DateTime
import com.blipblipcode.library.model.DateTimeRange
import kotlinx.coroutines.flow.Flow

interface IGetReportSaleUseCase {
    operator fun invoke(date: DateTimeRange, filters: List<DataFilter>): Flow<PagingData<ReportSale>>
}