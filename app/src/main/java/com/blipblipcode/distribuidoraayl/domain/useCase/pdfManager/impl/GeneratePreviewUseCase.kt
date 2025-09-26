package com.blipblipcode.distribuidoraayl.domain.useCase.pdfManager.impl

import com.blipblipcode.distribuidoraayl.domain.models.ResultType
import com.blipblipcode.distribuidoraayl.domain.models.sales.DocumentElectronic
import com.blipblipcode.distribuidoraayl.domain.models.sales.Sale
import com.blipblipcode.distribuidoraayl.domain.useCase.pdfManager.IGeneratePreviewUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.pdfManager.IPdfManagerRepository
import javax.inject.Inject

internal class GeneratePreviewUseCase @Inject constructor(
    private val pdfManagerRepository: IPdfManagerRepository
):IGeneratePreviewUseCase {
    override suspend fun invoke(sale: Sale, isLetter: Boolean): ResultType<DocumentElectronic> {
        return pdfManagerRepository.generatePreview(sale, isLetter)
    }

}