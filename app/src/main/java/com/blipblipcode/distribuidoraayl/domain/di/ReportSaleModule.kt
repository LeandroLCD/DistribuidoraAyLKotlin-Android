package com.blipblipcode.distribuidoraayl.domain.di

import com.blipblipcode.distribuidoraayl.domain.useCase.reportSale.IGetReportSaleUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.reportSale.impl.GetReportSaleUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class ReportSaleModule {

    @Binds
    abstract fun bindGetReportSaleUseCase(impl: GetReportSaleUseCase): IGetReportSaleUseCase

}