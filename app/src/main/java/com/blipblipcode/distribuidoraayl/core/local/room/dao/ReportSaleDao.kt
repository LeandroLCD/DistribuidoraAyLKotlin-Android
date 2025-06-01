package com.blipblipcode.distribuidoraayl.core.local.room.dao

import androidx.room.*
import com.blipblipcode.distribuidoraayl.core.local.entities.openFactura.reportSale.ClientReceiverEntity
import com.blipblipcode.distribuidoraayl.core.local.entities.openFactura.reportSale.ReportSaleEntity
import com.blipblipcode.distribuidoraayl.core.local.entities.openFactura.reportSale.SaleDataEntity
import com.blipblipcode.distribuidoraayl.core.local.entities.openFactura.reportSale.SalesItemEntity

@Dao
interface ReportSaleDao {

    @Transaction
    @Query("SELECT * FROM sale_data WHERE isSynchronized = 0")
    suspend fun getUnSynchronizedSales(): List<ReportSaleEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSaleItems(items: List<SalesItemEntity>)

    @Update
    suspend fun updateSaleSync(sale: SaleDataEntity)

    @Query("DELETE FROM sale_data WHERE isSynchronized = 1")
    suspend fun deleteSynchronizedSales()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSale(sale: SaleDataEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: SalesItemEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertClient(client: ClientReceiverEntity)

}