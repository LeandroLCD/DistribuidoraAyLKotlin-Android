package com.blipblipcode.distribuidoraayl.domain.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import com.blipblipcode.distribuidoraayl.domain.useCase.openFactura.IGetTaxpayerUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.openFactura.impl.GetTaxpayerUseCase


@Module
@InstallIn(ViewModelComponent::class)
internal abstract class OpenFacturaModule {

    @Binds
    abstract fun bindGetTaxpayer(useCase: GetTaxpayerUseCase): IGetTaxpayerUseCase
}