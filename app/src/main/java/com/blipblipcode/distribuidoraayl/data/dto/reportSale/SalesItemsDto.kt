package com.blipblipcode.distribuidoraayl.data.dto.reportSale

import com.blipblipcode.distribuidoraayl.core.local.entities.openFactura.reportSale.SalesItemEntity
import com.blipblipcode.distribuidoraayl.data.mapper.ToEntity
import com.google.firebase.firestore.PropertyName

@Suppress("unused")
class SalesItemsDto(
    @get:PropertyName("index")
    @set:PropertyName("index")
    var index: Int,

    @get:PropertyName("sku")
    @set:PropertyName("sku")
    var sku:String,

    @get:PropertyName("barCode")
    @set:PropertyName("barCode")
    var barCode:String? = null,

    @get:PropertyName("name")
    @set:PropertyName("name")
    var name:String,

    @get:PropertyName("description")
    @set:PropertyName("description")
    var description:String? = null,

    @get:PropertyName("price")
    @set:PropertyName("price")
    var price:Int,

    @get:PropertyName("quantity")
    @set:PropertyName("quantity")
    var quantity:Int
):ToEntity<SalesItemEntity> {
    constructor(): this(0, "", null, "", null, 0, 0)

    override fun mapToEntity(): SalesItemEntity {
        return SalesItemEntity(
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