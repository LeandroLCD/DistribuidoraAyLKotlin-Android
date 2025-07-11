package com.blipblipcode.distribuidoraayl.data.dto.products

import com.blipblipcode.distribuidoraayl.core.local.entities.product.UdmEntity
import com.blipblipcode.distribuidoraayl.data.mapper.Mappable
import com.blipblipcode.distribuidoraayl.data.mapper.ToEntity
import com.blipblipcode.distribuidoraayl.domain.models.products.Udm
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

@Suppress("unused")
data class ProductUdmDto(
    @set:PropertyName("uid")
    @get:PropertyName("uid")
    var uid: String,
    @set:PropertyName("name")
    @get:PropertyName("name")
    var name: String
): Mappable<Udm>, ToEntity<UdmEntity> {
    constructor():this("", "")
    override fun mapToDomain(): Udm {
        return Udm(uid = uid, name = name)
    }

    override fun mapToEntity(): UdmEntity {
        return UdmEntity(udmId = uid, udmName = name)
    }
}