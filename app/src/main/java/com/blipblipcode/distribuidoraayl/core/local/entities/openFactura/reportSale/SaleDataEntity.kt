package com.blipblipcode.distribuidoraayl.core.local.entities.openFactura.reportSale

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sale_data")
data class SaleDataEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val number: Long,
    val clientRut: String,
    val date: String,
    val token: String?,
    @Embedded(prefix = "resolution_") val resolution: ResolutionEntity?,
    val timbre: String?,
    val isSynchronized: Boolean = false
)