package com.blipblipcode.distribuidoraayl.core.local.room.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Transaction
import androidx.room.Update
import androidx.sqlite.db.SupportSQLiteQuery
import com.blipblipcode.distribuidoraayl.core.local.entities.reportSale.ClientReceiverEntity
import com.blipblipcode.distribuidoraayl.core.local.entities.reportSale.ReportSaleEntity
import com.blipblipcode.distribuidoraayl.core.local.entities.reportSale.SaleDataEntity
import com.blipblipcode.distribuidoraayl.core.local.entities.reportSale.SalesItemEntity
import com.blipblipcode.distribuidoraayl.core.local.entities.reportSale.TotalsEntity

@Dao
interface ReportSaleDao {

    @Transaction
    @Query("SELECT * FROM sale_data WHERE isSynchronized = 0")
    suspend fun getUnSynchronizedSales(): List<ReportSaleEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(items: List<SalesItemEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(totals: TotalsEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(sale: SaleDataEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(sales: List<SaleDataEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(sale: SaleDataEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: SalesItemEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(client: ClientReceiverEntity)


    @Query("SELECT * FROM sale_data WHERE isSynchronized = 1 ORDER BY date DESC LIMIT 1")
    suspend fun getLastSynchronizedSale(): SaleDataEntity?


    @Transaction
    @RawQuery(observedEntities = [SaleDataEntity::class])
    fun getReportSalePaging(query: SupportSQLiteQuery): PagingSource<Int, ReportSaleEntity>

}