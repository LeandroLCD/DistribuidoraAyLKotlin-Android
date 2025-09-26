package com.blipblipcode.distribuidoraayl.data.dto.reportSale

import com.blipblipcode.distribuidoraayl.core.local.entities.reportSale.ClientReceiverEntity
import com.blipblipcode.distribuidoraayl.data.mapper.ToEntity
import com.google.firebase.firestore.PropertyName

@Suppress("unused")
class ClientReceiverDto (

    @get:PropertyName("rut")
    @set:PropertyName("rut")
    var rut: String,

    @get:PropertyName("address")
    @set:PropertyName("address")
    var address: String,

    @get:PropertyName("name")
    @set:PropertyName("name")
    var name: String,

    @get:PropertyName("turn")
    @set:PropertyName("turn")
    var turn: String?,

    @get:PropertyName("commune")
    @set:PropertyName("commune")
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