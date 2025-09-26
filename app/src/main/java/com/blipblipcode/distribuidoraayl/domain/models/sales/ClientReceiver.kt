package com.blipblipcode.distribuidoraayl.domain.models.sales

data class ClientReceiver (
    val rut: String,
    val address: String,
    val name: String,
    val turn: String?,
    val commune: String,
)