package com.blipblipcode.distribuidoraayl.domain.useCase.printer

import com.blipblipcode.distribuidoraayl.domain.models.printState.PrinterState
import com.blipblipcode.distribuidoraayl.domain.models.sales.DocumentElectronic
import kotlinx.coroutines.flow.SharedFlow

interface IPrinterUseCase {
    operator fun invoke(): SharedFlow<PrinterState>
    fun connect(): Boolean
    fun disconnect()
    fun print(document: DocumentElectronic)
}