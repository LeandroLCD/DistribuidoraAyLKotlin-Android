package com.blipblipcode.distribuidoraayl.domain.useCase.printer

import com.blipblipcode.distribuidoraayl.domain.models.ResultType
import com.blipblipcode.distribuidoraayl.domain.models.printState.PrinterState
import com.blipblipcode.distribuidoraayl.domain.models.sales.DocumentElectronic
import kotlinx.coroutines.flow.SharedFlow

interface IPrinterRepository {
    val printerState: SharedFlow<PrinterState>
    suspend fun connect(): ResultType<Unit>
    fun disconnect()
    suspend fun print(document: DocumentElectronic): ResultType<Unit>
}