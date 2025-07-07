package com.blipblipcode.distribuidoraayl.domain.di

import com.blipblipcode.distribuidoraayl.domain.useCase.openFactura.IGenerateInvoiceUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import com.blipblipcode.distribuidoraayl.domain.useCase.openFactura.IGetTaxpayerUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.openFactura.impl.GenerateInvoiceUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.openFactura.impl.GetTaxpayerUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.pdfManager.IGeneratePreviewUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.pdfManager.impl.GeneratePreviewUseCase

@Module
@InstallIn(ViewModelComponent::class)
internal abstract class OpenFacturaModule {

    @Binds
    abstract fun bindGetTaxpayer(useCase: GetTaxpayerUseCase): IGetTaxpayerUseCase

    @Binds
    abstract fun bindGenerateInvoice(useCase: GenerateInvoiceUseCase): IGenerateInvoiceUseCase

    @Binds
    abstract fun bindGeneratePreview(useCase: GeneratePreviewUseCase): IGeneratePreviewUseCase
}