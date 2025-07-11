package com.blipblipcode.distribuidoraayl.domain.models.sales


sealed class Payment {
    data object Credit: Payment()
    data object Cash: Payment()
}