package com.blipblipcode.distribuidoraayl.domain.useCase.pdfManager

import com.blipblipcode.distribuidoraayl.domain.models.ResultType
import com.blipblipcode.distribuidoraayl.domain.models.sales.DocumentElectronic
import com.blipblipcode.distribuidoraayl.domain.models.sales.Sale

interface IGeneratePreviewUseCase {
    suspend operator fun invoke(sale: Sale, isLetter: Boolean): ResultType<DocumentElectronic>
}