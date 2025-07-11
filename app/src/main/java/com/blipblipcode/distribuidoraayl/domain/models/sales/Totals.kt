package com.blipblipcode.distribuidoraayl.domain.models.sales

data class Totals(
    val netAmount:Int = 0,
    val tax: Int = 0,
    val taxAmount:Int = 0,
    val periodicAmount:Int = 0,
    val total:Int = 0
)