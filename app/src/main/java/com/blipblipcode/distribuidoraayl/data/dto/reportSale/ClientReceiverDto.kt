package com.blipblipcode.distribuidoraayl.data.dto.reportSale

import com.blipblipcode.distribuidoraayl.core.local.entities.openFactura.reportSale.ClientReceiverEntity
import com.blipblipcode.distribuidoraayl.data.mapper.ToEntity
import com.google.firebase.firestore.PropertyName
@Suppress("unused")
class ClientReceiverDto (

    @get:PropertyName("number")
    @set:PropertyName("number")
    var rut: String,

    @get:PropertyName("number")
    @set:PropertyName("number")
    var address: String,

    @get:PropertyName("number")
    @set:PropertyName("number")
    var name: String,

    @get:PropertyName("number")
    @set:PropertyName("number")
    var turn: String?,

    @get:PropertyName("number")
    @set:PropertyName("number")
    var commune: String
):ToEntity<ClientReceiverEntity>{
    constructor():this(rut = "", address = "", name = "", turn = null , commune = "")

    override fun mapToEntity(): ClientReceiverEntity {
        return ClientReceiverEntity(
            rut = rut,
            address = address,
            name = name,
            turn = turn,
            commune = commune
        )
    }
}