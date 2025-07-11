package com.blipblipcode.distribuidoraayl.core.local.entities.openFactura.reportSale

import androidx.room.Embedded
import androidx.room.Relation

data class ReportSaleEntity(
    @Embedded 
    val sale: SaleDataEntity,
    
    @Relation(
        parentColumn = "clientRut",
        entityColumn = "rut"
    )
    val client: ClientReceiverEntity,
    
    @Relation(
        parentColumn = "id",
        entityColumn = "sale_id"
    )
    val items: List<SalesItemEntity>
)