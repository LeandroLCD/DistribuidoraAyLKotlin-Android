package com.blipblipcode.distribuidoraayl.core.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.blipblipcode.distribuidoraayl.core.local.entities.RemoteKey
import com.blipblipcode.distribuidoraayl.core.local.entities.product.ProductEntity
import com.blipblipcode.distribuidoraayl.core.local.entities.reportSale.ClientReceiverEntity
import com.blipblipcode.distribuidoraayl.core.local.entities.reportSale.SaleDataEntity
import com.blipblipcode.distribuidoraayl.core.local.entities.reportSale.SalesItemEntity
import com.blipblipcode.distribuidoraayl.core.local.entities.reportSale.TotalsEntity
import com.blipblipcode.distribuidoraayl.core.local.room.dao.ProductDao
import com.blipblipcode.distribuidoraayl.core.local.room.dao.RemoteKeyDao
import com.blipblipcode.distribuidoraayl.core.local.room.dao.ReportSaleDao

@Database(
    entities = [
        ProductEntity::class,
        SaleDataEntity::class,
        SalesItemEntity::class,
        ClientReceiverEntity::class,
        TotalsEntity::class,
        RemoteKey::class
    ],
    version = 6,
    exportSchema = false
)
abstract class DataBaseApp : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun reportSaleDao(): ReportSaleDao
    abstract fun remoteKeyDao(): RemoteKeyDao
}