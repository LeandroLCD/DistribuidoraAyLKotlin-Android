package com.blipblipcode.distribuidoraayl.domain.di

import com.blipblipcode.distribuidoraayl.domain.useCase.printer.IPrinterUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.printer.impl.PrinterUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
internal abstract class PrinterModule {

    @Binds
    abstract fun bindsPrinter(useCase: PrinterUseCase): IPrinterUseCase
}