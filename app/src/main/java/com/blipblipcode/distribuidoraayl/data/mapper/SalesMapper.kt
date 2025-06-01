package com.blipblipcode.distribuidoraayl.data.mapper

import com.blipblipcode.distribuidoraayl.core.local.entities.openFactura.reportSale.ClientReceiverEntity
import com.blipblipcode.distribuidoraayl.core.local.entities.openFactura.reportSale.ReportSaleEntity
import com.blipblipcode.distribuidoraayl.core.local.entities.openFactura.reportSale.SalesItemEntity
import com.blipblipcode.distribuidoraayl.data.dto.of.dte33.CodeDto
import com.blipblipcode.distribuidoraayl.data.dto.of.dte33.DteDto
import com.blipblipcode.distribuidoraayl.data.dto.of.dte33.ElectronicInvoiceDto
import com.blipblipcode.distribuidoraayl.data.dto.of.dte33.EmisorDto
import com.blipblipcode.distribuidoraayl.data.dto.of.dte33.HeaderDto
import com.blipblipcode.distribuidoraayl.data.dto.of.dte33.IdDocDto
import com.blipblipcode.distribuidoraayl.data.dto.of.dte33.ItemDetailDto
import com.blipblipcode.distribuidoraayl.data.dto.of.dte33.ReceiverDto
import com.blipblipcode.distribuidoraayl.data.dto.of.dte33.TotalsDto
import com.blipblipcode.distribuidoraayl.data.dto.reportSale.ClientReceiverDto
import com.blipblipcode.distribuidoraayl.data.dto.reportSale.ReportSaleDto
import com.blipblipcode.distribuidoraayl.data.dto.reportSale.ResolutionTdo
import com.blipblipcode.distribuidoraayl.data.dto.reportSale.SalesItemsDto
import com.blipblipcode.distribuidoraayl.domain.models.preferences.ECommerce
import com.blipblipcode.distribuidoraayl.domain.models.sales.ClientReceiver
import com.blipblipcode.distribuidoraayl.domain.models.sales.Payment
import com.blipblipcode.distribuidoraayl.domain.models.sales.Sale
import com.blipblipcode.distribuidoraayl.domain.models.sales.SalesItem
import com.blipblipcode.distribuidoraayl.domain.models.sales.Totals

fun Sale.toElectronicInvoice(payment: Payment, isLetter: Boolean = true): ElectronicInvoiceDto {
    val pay = (payment is Payment.Cash).transform(1, 2)
    return ElectronicInvoiceDto(
        dte = DteDto(
            detail = this.items.map { it.toDetailDto() },
            header = HeaderDto(
                emisor = eCommerce.toEmisor(),
                idDoc = IdDocDto(
                    fchEmis = date.format("yyyy-MM-dd"),
                    fmaPago = pay,
                    tipoDTE = 33
                ),
                receptor = receiver.toDto(),
                totales = totals.toDto()
            )),
            response = buildList {
                add("TIMBRE")
                add("FOLIO")
                add("RESOLUCION")
                if (isLetter)
                    add("LETTER")
                else
                    add("80MM")
            }
        )
}

fun <T>Boolean.transform(valueIsTrue: T, valueIsFalse: T): T = if (this) valueIsTrue else valueIsFalse

fun Totals.toDto(): TotalsDto{
    return TotalsDto(
        iVA = taxAmount,
        mntNeto = netAmount,
        mntTotal = total,
        montoPeriodo = periodicAmount,
        tasaIVA = tax.toString(),
        vlrPagar = total
    )
}

fun ClientReceiver.toDto(): ReceiverDto {
    return ReceiverDto(
        cmnaRecep = commune,
        dirRecep = address,
        giroRecep = turn?.take(35).orEmpty(),
        rUTRecep = rut,
        rznSocRecep = name
    )
}

fun ECommerce.toEmisor(): EmisorDto{
    return EmisorDto(
        acteco = economicActivity,
        cdgSIISucur = siiOfficeCode.toString(),
        cmnaOrigen = communeOrigin,
        dirOrigen = addressOrigin,
        giroEmis = businessLine,
        rUTEmisor = rut,
        rznSoc = companyName,
        telefono = phone
    )
}

fun SalesItem.toDetailDto(): ItemDetailDto {
    val roundedPrice = price
    return ItemDetailDto(
        montoItem = roundedPrice * quantity,
        nmbItem = name,
        nroLinDet = index,
        prcItem = roundedPrice,
        qtyItem = quantity,
        cdgItem = buildList {
            add(CodeDto("SKU", sku))
            if (!barCode.isNullOrBlank()) {
                add(CodeDto("EAN13", barCode))
            }
        }
    )
}

fun SalesItem.toEntity(saleId: Long): SalesItemEntity {
    return SalesItemEntity(
        saleId = saleId,
        index = index,
        sku = sku,
        barCode = barCode,
        name = name,
        description = description,
        price = price,
        quantity = quantity
    )
}

fun ClientReceiver.toEntity(): ClientReceiverEntity{
    return ClientReceiverEntity(
        rut = rut,
        name = name,
        address = address,
        commune = commune,
        turn = turn
    )

}

fun ReportSaleEntity.toDto(): ReportSaleDto{
    return ReportSaleDto(
        uid = sale.uid,
        number = sale.number,
        date = sale.date,
        token = sale.token,
        resolution = sale.resolution?.let { ResolutionTdo(it.number, it.date) },
        timbre = sale.timbre,
        receiver = client.toDto(),
        items = items.map { it.toDto() }
    )

}
fun ClientReceiverEntity.toDto(): ClientReceiverDto{
    return ClientReceiverDto(
        rut = rut,
        name = name,
        address = address,
        commune = commune,
        turn = turn
    )
}

fun SalesItemEntity.toDto(): SalesItemsDto {
    return SalesItemsDto(
        index = index,
        sku = sku,
        barCode = barCode,
        name = name,
        description = description,
        price = price,
        quantity = quantity
    )

}