package com.blipblipcode.distribuidoraayl.core.local.entities.reportSale

import com.blipblipcode.distribuidoraayl.data.mapper.Mappable
import com.blipblipcode.distribuidoraayl.domain.models.reportSale.Resolution

data class ResolutionEntity(
    val number: Int,
    val date: String
):Mappable<Resolution> {
    override fun mapToDomain(): Resolution {
        return Resolution(
            number = number,
            date = date
        )
    }
}