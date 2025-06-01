package com.blipblipcode.distribuidoraayl.domain.useCase.pdfManager

import android.net.Uri
import com.blipblipcode.distribuidoraayl.domain.models.ResultType
import com.blipblipcode.distribuidoraayl.domain.models.sales.DteType
import com.blipblipcode.distribuidoraayl.domain.models.sales.Sale

interface IPdfManagerRepository {

    suspend fun generatePreview(sale: Sale): ResultType<Uri>

    fun generateDte(
        sale: Sale,
        dteType: DteType,
        number: Int,
        resolution: String,
        fiscalTimbre: String
    ): Uri
}