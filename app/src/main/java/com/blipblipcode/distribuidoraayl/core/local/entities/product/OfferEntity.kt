package com.blipblipcode.distribuidoraayl.core.local.entities.product

import com.blipblipcode.distribuidoraayl.data.mapper.Mappable
import com.blipblipcode.distribuidoraayl.domain.models.products.Offer

class OfferEntity(val percentage: Double, val isActive: Boolean): Mappable<Offer> {
    override fun mapToDomain(): Offer {
        return Offer(
            percentage, isActive
        )

    }
}
