package com.blipblipcode.distribuidoraayl.data.dto.products

import com.blipblipcode.distribuidoraayl.core.local.entities.product.OfferEntity
import com.blipblipcode.distribuidoraayl.data.mapper.Mappable
import com.blipblipcode.distribuidoraayl.data.mapper.ToEntity
import com.blipblipcode.distribuidoraayl.domain.models.products.Offer
import com.google.firebase.firestore.PropertyName
@Suppress("unused")
data class OfferDto(
    @get:PropertyName("percentage")
    @set:PropertyName("percentage")
    var percentage: Double,
    @get:PropertyName("isActive")
    @set:PropertyName("isActive")
    var isActive: Boolean
): Mappable<Offer>, ToEntity<OfferEntity> {
    constructor(): this(0.0, false)
    override fun mapToDomain(): Offer {
        return Offer(percentage, isActive)
    }

    override fun mapToEntity(): OfferEntity {
        return OfferEntity(
            percentage = percentage,
            isActive = isActive
        )
    }
}