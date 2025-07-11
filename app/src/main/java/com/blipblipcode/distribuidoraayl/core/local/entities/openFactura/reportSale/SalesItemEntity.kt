package com.blipblipcode.distribuidoraayl.core.local.entities.openFactura.reportSale

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sale_items")
class SalesItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo(name = "sale_id")
    val saleId: Long = 0L,
    val index: Int,
    val sku:String,
    @ColumnInfo(name = "bar_code")
    val barCode:String? = null,
    val name:String,
    val description:String? = null,
    val price:Int,
    val quantity:Int
)