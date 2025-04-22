package com.blipblipcode.distribuidoraayl.core.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.blipblipcode.distribuidoraayl.core.local.entities.product.ProductEntity

@Database(
    entities = [ProductEntity::class],
    version = 1,
    exportSchema = false
)
abstract class DataBaseApp:RoomDatabase() {
    abstract fun productDao(): ProductDao
}