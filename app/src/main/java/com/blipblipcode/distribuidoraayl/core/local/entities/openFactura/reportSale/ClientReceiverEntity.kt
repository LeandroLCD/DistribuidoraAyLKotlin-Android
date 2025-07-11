package com.blipblipcode.distribuidoraayl.core.local.entities.openFactura.reportSale

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "client_receiver")
data class ClientReceiverEntity (
    @PrimaryKey
    val rut: String,
    val address: String,
    val name: String,
    val turn: String?,
    val commune: String,
)