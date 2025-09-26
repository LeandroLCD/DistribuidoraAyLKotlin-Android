package com.blipblipcode.distribuidoraayl.core.local.entities.reportSale

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.blipblipcode.distribuidoraayl.data.mapper.Mappable
import com.blipblipcode.distribuidoraayl.domain.models.sales.SalesItem

@Entity(tableName = "sale_items")
data class SalesItemEntity(
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
):Mappable<SalesItem>{
    override fun mapToDomain(): SalesItem {
        return SalesItem(
            index = index,
            sku = sku,
            barCode = barCode,
            name = name,
            description = description,
            price = price,
            quantity = quantity
        )
    }
}