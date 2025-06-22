package com.blipblipcode.distribuidoraayl.data.dto.reportSale

import com.blipblipcode.distribuidoraayl.core.local.entities.reportSale.TotalsEntity
import com.blipblipcode.distribuidoraayl.data.mapper.ToEntity
import com.google.firebase.firestore.PropertyName

data class TotalsSaleDto(
    @get:PropertyName("tax")
    @set:PropertyName("tax")
    var tax: Int = 0,

    @get:PropertyName("netAmount")
    @set:PropertyName("netAmount")
    var netAmount:Int = 0,

    @get:PropertyName("taxAmount")
    @set:PropertyName("taxAmount")
    var taxAmount:Int = 0,

    @get:PropertyName("periodicAmount")
    @set:PropertyName("periodicAmount")
    var periodicAmount:Int = 0,

    @get:PropertyName("total")
    @set:PropertyName("total")
    var total:Int = 0
): ToEntity<TotalsEntity>{
    constructor(): this(0, 0, 0, 0, 0)
    override fun mapToEntity(): TotalsEntity {
        return TotalsEntity(
            tax = tax,
            netAmount = netAmount,
            taxAmount = taxAmount,
            periodicAmount = periodicAmount,
            total = total
        )
    }
}