package com.blipblipcode.distribuidoraayl.data.dto.customer

import com.blipblipcode.distribuidoraayl.data.mapper.Mappable
import com.blipblipcode.distribuidoraayl.domain.models.customer.Region
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

data class RegionDto(
    @get:PropertyName("communes")
    @set:PropertyName("communes")
    var communes: List<CommuneDto>,
    @DocumentId
    var id: String,
    @get:PropertyName("name")
    @set:PropertyName("name")
    var name: String
): Mappable<Region> {

    constructor(): this(emptyList(), "", "")

    override fun mapToDomain(): Region {
        return Region(id, name, communes.map { it.mapToDomain() })
    }
}