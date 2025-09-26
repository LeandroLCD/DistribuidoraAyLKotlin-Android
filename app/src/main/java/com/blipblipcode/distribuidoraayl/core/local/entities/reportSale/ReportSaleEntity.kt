package com.blipblipcode.distribuidoraayl.core.local.entities.reportSale

import androidx.room.Embedded
import androidx.room.Relation
import com.blipblipcode.distribuidoraayl.data.mapper.Mappable
import com.blipblipcode.distribuidoraayl.domain.models.reportSale.ReportSale
import com.blipblipcode.library.DateTime

data class ReportSaleEntity(
    @Embedded 
    val sale: SaleDataEntity,
    
    @Relation(
        parentColumn = "clientRut",
        entityColumn = "rut"
    )
    val client: ClientReceiverEntity,
    
    @Relation(
        parentColumn = "id",
        entityColumn = "sale_id"
    )
    val items: List<SalesItemEntity>,

    @Relation(
        parentColumn = "id",
        entityColumn = "sale_id"
    )
    val totals: TotalsEntity

): Mappable<ReportSale> {
    override fun mapToDomain(): ReportSale {
        return ReportSale(
            id = sale.id,
            uid = sale.uid,
            number = sale.number,
            dteType = sale.dteType,
            client = client.mapToDomain(),
            date = DateTime.fromMillis(sale.date),
            token = sale.token,
            resolution = sale.resolution?.mapToDomain(),
            timbre = sale.timbre,
            isSynchronized = sale.isSynchronized,
            items = items.map { it.mapToDomain() },
            totals = totals.mapToDomain()
        )

    }
}