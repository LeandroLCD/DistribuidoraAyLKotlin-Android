package com.blipblipcode.distribuidoraayl.data.dto.products

import com.blipblipcode.distribuidoraayl.data.mapper.Mappable
import com.blipblipcode.distribuidoraayl.domain.models.products.Udm
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

@Suppress("unused")
data class UdmDto (
    @DocumentId
    var uid: String,
    @set:PropertyName("name")
    @get:PropertyName("name")
    var name: String
): Mappable<Udm> {
    constructor():this("", "")
    override fun mapToDomain(): Udm {
       return Udm(uid = uid, name = name)
    }
}