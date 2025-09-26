package com.blipblipcode.distribuidoraayl.data.dto.customer.regions

import com.blipblipcode.distribuidoraayl.data.mapper.Mappable
import com.blipblipcode.distribuidoraayl.domain.models.customer.Commune
import com.google.firebase.firestore.PropertyName

data class CommuneDto(
    @get:PropertyName("name")
    @set:PropertyName("name")
    var name: String
): Mappable<Commune> {

    constructor() : this("")

    override fun mapToDomain(): Commune {
        return Commune(name)
    }
}