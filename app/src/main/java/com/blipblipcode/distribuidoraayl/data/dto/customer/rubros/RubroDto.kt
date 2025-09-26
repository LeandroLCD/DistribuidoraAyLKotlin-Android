package com.blipblipcode.distribuidoraayl.data.dto.customer.rubros

import com.blipblipcode.distribuidoraayl.data.mapper.Mappable
import com.blipblipcode.distribuidoraayl.domain.models.customer.Rubro
import com.google.firebase.firestore.PropertyName

data class RubroDto(
    @set:PropertyName("id")
    @get:PropertyName("id")
    var id: Int,
    @set:PropertyName("soc")
    @get:PropertyName("soc")
    var soc: String,
    @set:PropertyName("description")
    @get:PropertyName("description")
    var description: String
): Mappable<Rubro> {

    constructor(): this(0, "", "")

    override fun mapToDomain(): Rubro {
        return Rubro(id, soc, description)
    }
}