package com.blipblipcode.distribuidoraayl.domain.useCase.printer.impl

import com.blipblipcode.distribuidoraayl.domain.models.ResultType
import com.blipblipcode.distribuidoraayl.domain.models.printState.PrinterState
import com.blipblipcode.distribuidoraayl.domain.models.sales.DocumentElectronic
import com.blipblipcode.distribuidoraayl.domain.useCase.printer.IPrinterRepository
import com.blipblipcode.distribuidoraayl.domain.useCase.printer.IPrinterUseCase
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

internal class PrinterUseCase @Inject constructor(
    private val printerRepository: IPrinterRepository
) : IPrinterUseCase {
    override fun invoke(): SharedFlow<PrinterState> = printerRepository.printerState
    override suspend fun connect(): ResultType<Unit> {
        return printerRepository.connect()
    }

    override fun disconnect() {
        return printerRepository.disconnect()
    }

    override suspend fun print(document: DocumentElectronic): ResultType<Unit> {
        return printerRepository.print(document)
    }
}