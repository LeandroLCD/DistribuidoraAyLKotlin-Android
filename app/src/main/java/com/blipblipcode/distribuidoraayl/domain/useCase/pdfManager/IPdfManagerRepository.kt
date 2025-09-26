package com.blipblipcode.distribuidoraayl.domain.useCase.pdfManager

import android.net.Uri
import com.blipblipcode.distribuidoraayl.domain.models.ResultType
import com.blipblipcode.distribuidoraayl.domain.models.sales.DocumentElectronic
import com.blipblipcode.distribuidoraayl.domain.models.sales.DteType
import com.blipblipcode.distribuidoraayl.domain.models.sales.Payment
import com.blipblipcode.distribuidoraayl.domain.models.sales.Sale

interface IPdfManagerRepository {

    suspend fun generatePreview(sale: Sale, isLetter: Boolean): ResultType<DocumentElectronic>

    fun generateDteLetter(
        sale: Sale,
        dteType: DteType,
        number: Long,
        resolution: String,
        fiscalTimbre: String,
        payment: Payment
    ): Uri

    fun generateDte80mm(
        sale: Sale,
        dteType: DteType,
        number: Long,
        resolution: String,
        fiscalTimbre: String,
        payment: Payment
    ): Uri
}