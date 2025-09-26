package com.blipblipcode.distribuidoraayl.core.local.entities.reportSale

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.blipblipcode.distribuidoraayl.data.mapper.Mappable
import com.blipblipcode.distribuidoraayl.domain.models.sales.ClientReceiver

@Entity(tableName = "client_receiver")
data class ClientReceiverEntity (
    @PrimaryKey
    val rut: String,
    val address: String,
    val name: String,
    val turn: String?,
    val commune: String,
):Mappable<ClientReceiver> {
    override fun mapToDomain(): ClientReceiver {
        return ClientReceiver(
            rut = rut,
            address = address,
            name = name,
            turn = turn,
            commune = commune
        )
    }
}