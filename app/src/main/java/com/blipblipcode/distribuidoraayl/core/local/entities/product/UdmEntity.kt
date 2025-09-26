package com.blipblipcode.distribuidoraayl.core.local.entities.product

import com.blipblipcode.distribuidoraayl.data.mapper.Mappable
import com.blipblipcode.distribuidoraayl.domain.models.products.Udm

class UdmEntity(val udmName: String, val udmId: String): Mappable<Udm> {
    override fun mapToDomain(): Udm {
        return Udm(
            uid = udmId,
            name = udmName,
        )
    }
}
