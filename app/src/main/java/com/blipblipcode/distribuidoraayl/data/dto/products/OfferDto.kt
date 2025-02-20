package com.blipblipcode.distribuidoraayl.data.dto.products

import com.blipblipcode.distribuidoraayl.data.mapper.Mappable
import com.blipblipcode.distribuidoraayl.domain.models.products.Offer
import com.google.firebase.firestore.PropertyName

data class OfferDto(
    @get:PropertyName("percentage")
    @set:PropertyName("percentage")
    var percentage: Double,
    @get:PropertyName("isActive")
    @set:PropertyName("isActive")
    var isActive: Boolean
): Mappable<Offer> {
    constructor(): this(0.0, false)
    override fun mapToDomain(): Offer {
        return Offer(percentage, isActive)
    }
}