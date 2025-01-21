package com.blipblipcode.distribuidoraayl.data.dto

import com.blipblipcode.distribuidoraayl.data.mapper.Mappable
import com.blipblipcode.distribuidoraayl.domain.models.customer.Route
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

@Suppress("unused")
data class RouteDto(
    @get:DocumentId
    var uid: String = "",
    @get:PropertyName("name")
    @set:PropertyName("name")
    var name: String,
): Mappable<Route> {
    constructor() : this(uid="", name="")
    override fun mapToDomain(): Route {
        return Route(uid, name)
    }

}