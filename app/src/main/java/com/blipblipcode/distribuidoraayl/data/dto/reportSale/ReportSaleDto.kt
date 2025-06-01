package com.blipblipcode.distribuidoraayl.data.dto.reportSale

import com.blipblipcode.distribuidoraayl.core.local.entities.openFactura.reportSale.ReportSaleEntity
import com.blipblipcode.distribuidoraayl.core.local.entities.openFactura.reportSale.SaleDataEntity
import com.blipblipcode.distribuidoraayl.data.mapper.ToEntity
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

@Suppress("unused")
class ReportSaleDto(
    @DocumentId
    var uid: String,
    @get:PropertyName("number")
    @set:PropertyName("number")
    var number: Int,
    @get:PropertyName("date")
    @set:PropertyName("date")
    var date: String,
    @get:PropertyName("token")
    @set:PropertyName("token")
    var token: String?,
    @get:PropertyName("resolution")
    @set:PropertyName("resolution")
    var resolution: ResolutionTdo?,
    @get:PropertyName("timbre")
    @set:PropertyName("timbre")
    var timbre: String?,
    @get:PropertyName("receiver")
    @set:PropertyName("receiver")
    var receiver: ClientReceiverDto,
    @get:PropertyName("items")
    @set:PropertyName("items")
    var items: List<SalesItemsDto>,
) :ToEntity<ReportSaleEntity>{
    constructor(): this("", 0, "", null, null, null, ClientReceiverDto(), emptyList())

    override fun mapToEntity(): ReportSaleEntity {
        return ReportSaleEntity(
            sale = SaleDataEntity(
                number = number,
                clientRut = receiver.rut,
                date = date,
                token = token,
                resolution = resolution?.mapToEntity(),
                timbre = timbre,
                isSynchronized = true
            ),
            items = items.map { it.mapToEntity() },
            client = receiver.mapToEntity()
        )

    }


}