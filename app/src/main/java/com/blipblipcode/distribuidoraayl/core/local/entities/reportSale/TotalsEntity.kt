package com.blipblipcode.distribuidoraayl.core.local.entities.reportSale

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.blipblipcode.distribuidoraayl.data.mapper.Mappable
import com.blipblipcode.distribuidoraayl.domain.models.sales.Totals

@Entity(tableName = "sale_totals")
data class TotalsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo(name = "sale_id")
    val saleId: Long = 0L,
    @ColumnInfo(name = "net_amount")
    val netAmount:Int = 0,
    val tax: Int = 0,
    @ColumnInfo(name = "tax_amount")
    val taxAmount:Int = 0,
    @ColumnInfo(name = "periodic_amount")
    val periodicAmount:Int = 0,
    val total:Int = 0
):Mappable<Totals>{
    override fun mapToDomain(): Totals {
        return Totals(
            netAmount = netAmount,
            tax = tax,
            taxAmount = taxAmount,
            periodicAmount = periodicAmount,
            total = total
        )
    }
}
