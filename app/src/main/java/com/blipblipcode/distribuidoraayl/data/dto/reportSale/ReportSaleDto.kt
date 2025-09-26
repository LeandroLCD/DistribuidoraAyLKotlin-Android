package com.blipblipcode.distribuidoraayl.data.dto.reportSale

import com.blipblipcode.distribuidoraayl.core.local.entities.reportSale.ReportSaleEntity
import com.blipblipcode.distribuidoraayl.core.local.entities.reportSale.SaleDataEntity
import com.blipblipcode.distribuidoraayl.data.mapper.ToEntity
import com.blipblipcode.distribuidoraayl.domain.models.sales.DteType
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

@Suppress("unused")
class ReportSaleDto(
    @DocumentId
    var uid: String,
    @get:PropertyName("number")
    @set:PropertyName("number")
    var number: Long,
    @get:PropertyName("date")
    @set:PropertyName("date")
    var date: Long,
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
    @get:PropertyName("dteType")
    @set:PropertyName("dteType")
    var dteType: DteType,
    @get:PropertyName("items")
    @set:PropertyName("items")
    var items: List<SalesItemsDto>,
    @get:PropertyName("totals")
    @set:PropertyName("totals")
    var totals: TotalsSaleDto,
) :ToEntity<ReportSaleEntity>{
    constructor(): this("", 0, 0L, null, null, null, ClientReceiverDto(), DteType.ORDER_NOTE, emptyList(), TotalsSaleDto())

    override fun mapToEntity(): ReportSaleEntity {
        return ReportSaleEntity(
            sale = SaleDataEntity(
                uid = uid,
                number = number,
                clientRut = receiver.rut,
                date = date,
                token = token,
                resolution = resolution?.mapToEntity(),
                timbre = timbre,
                isSynchronized = true,
                dteType = dteType
            ),
            items = items.map { it.mapToEntity() },
            client = receiver.mapToEntity(),
            totals = totals.mapToEntity()
        )

    }


}