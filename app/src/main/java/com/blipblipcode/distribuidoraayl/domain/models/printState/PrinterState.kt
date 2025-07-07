package com.blipblipcode.distribuidoraayl.domain.models.printState

sealed interface PrinterState {

    data object Idle: PrinterState

    data object Disconnected : PrinterState

    data object Connected : PrinterState

    data object Printing : PrinterState

    data object Ready : PrinterState

    data object BluetoothDisabled : PrinterState

    data class Exception(val throwable: Throwable): PrinterState
}