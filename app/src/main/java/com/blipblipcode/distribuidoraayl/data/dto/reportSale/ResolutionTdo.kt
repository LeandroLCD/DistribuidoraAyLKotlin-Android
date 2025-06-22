package com.blipblipcode.distribuidoraayl.data.dto.reportSale

import com.blipblipcode.distribuidoraayl.core.local.entities.reportSale.ResolutionEntity
import com.blipblipcode.distribuidoraayl.data.mapper.ToEntity
import com.google.firebase.firestore.PropertyName

@Suppress("unused")
data class ResolutionTdo(
    @get:PropertyName("number")
    @set:PropertyName("number")
    var number: Int,
    @get:PropertyName("date")
    @set:PropertyName("date")
    var date: String
):ToEntity<ResolutionEntity>{
    constructor(): this(0, "")
    override fun mapToEntity(): ResolutionEntity {
        return ResolutionEntity(
            number = number,
            date = date
        )
    }
}