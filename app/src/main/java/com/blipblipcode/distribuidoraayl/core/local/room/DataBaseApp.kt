package com.blipblipcode.distribuidoraayl.core.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.blipblipcode.distribuidoraayl.core.local.entities.openFactura.reportSale.ClientReceiverEntity
import com.blipblipcode.distribuidoraayl.core.local.entities.openFactura.reportSale.SaleDataEntity
import com.blipblipcode.distribuidoraayl.core.local.entities.openFactura.reportSale.SalesItemEntity
import com.blipblipcode.distribuidoraayl.core.local.entities.product.ProductEntity
import com.blipblipcode.distribuidoraayl.core.local.room.dao.ProductDao
import com.blipblipcode.distribuidoraayl.core.local.room.dao.ReportSaleDao

@Database(
    entities = [
        ProductEntity::class,
        SaleDataEntity::class,
        SalesItemEntity::class,
        ClientReceiverEntity::class
    ],
    version = 4,
    exportSchema = false
)
abstract class DataBaseApp : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun reportSaleDao(): ReportSaleDao
}