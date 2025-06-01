package com.blipblipcode.distribuidoraayl.data.repositiry.of

import android.content.Context
import androidx.room.withTransaction
import com.blipblipcode.distribuidoraayl.core.local.entities.openFactura.reportSale.ResolutionEntity
import com.blipblipcode.distribuidoraayl.core.local.entities.openFactura.reportSale.SaleDataEntity
import com.blipblipcode.distribuidoraayl.core.local.room.DataBaseApp
import com.blipblipcode.distribuidoraayl.core.network.IOpenFacturaApi
import com.blipblipcode.distribuidoraayl.data.dto.of.EmissionResponseDto
import com.blipblipcode.distribuidoraayl.data.mapper.toElectronicInvoice
import com.blipblipcode.distribuidoraayl.data.mapper.toEntity
import com.blipblipcode.distribuidoraayl.data.repositiry.BaseRepository
import com.blipblipcode.distribuidoraayl.data.repositiry.EventManager
import com.blipblipcode.distribuidoraayl.domain.models.ResultType
import com.blipblipcode.distribuidoraayl.domain.models.of.Taxpayer
import com.blipblipcode.distribuidoraayl.domain.models.sales.DocumentElectronic
import com.blipblipcode.distribuidoraayl.domain.models.sales.DteType
import com.blipblipcode.distribuidoraayl.domain.models.sales.Payment
import com.blipblipcode.distribuidoraayl.domain.models.sales.Sale
import com.blipblipcode.distribuidoraayl.domain.useCase.openFactura.IOpenFacturaRepository
import com.blipblipcode.distribuidoraayl.domain.useCase.pdfManager.IPdfManagerRepository
import com.blipblipcode.library.model.FormatType
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class OpenFacturaRepository @Inject constructor(
    dispatcher: CoroutineDispatcher,
    context: Context,
    private val eventManager: EventManager,
    private val pdfManager: IPdfManagerRepository,
    private val openFacturaApi: IOpenFacturaApi,
    private val dataBaseApp: DataBaseApp
):BaseRepository(dispatcher, context), IOpenFacturaRepository {
    override suspend fun getTaxpayer(rut: String): ResultType<Taxpayer> {
        return makeCallNetwork {
            openFacturaApi.getTaxpayer(rut).mapToDomain()
        }
    }

    override suspend fun generateInvoice(
        payment: Payment,
        sale: Sale
    ): ResultType<DocumentElectronic> {
        return makeCallNetwork {
            val dto = sale.toElectronicInvoice(payment)
            val response =  openFacturaApi.generateSale(dto)

            eventManager.addCall {
                saveReportSale(sale, response)
            }
            val uri =  pdfManager.generateDte(
                sale,
                DteType.INVOICE,
                number = response.number,
                resolution = response.resolution.toString(),
                fiscalTimbre = response.timbre
            )

            DocumentElectronic(uri = uri, number = response.number)
        }
    }

    private suspend fun saveReportSale(sale: Sale, response: EmissionResponseDto){
        val saleEntity = SaleDataEntity(
            number = response.number,
            clientRut = sale.receiver.rut,
            date = sale.date.format("yyyy-MM-dd"),
            token = response.token,
            resolution = ResolutionEntity(date =response.resolution.date, number =  response.resolution.number),
            timbre = response.timbre
        )
        dataBaseApp.apply {
            withTransaction {
                reportSaleDao().insertClient(sale.receiver.toEntity())
                val saleId = reportSaleDao().insertSale(saleEntity)
                val items = sale.items.map {
                    it.toEntity(saleId)
                }
                reportSaleDao().insertSaleItems(items)
            }
        }
    }
}