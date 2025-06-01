package com.blipblipcode.distribuidoraayl.domain.useCase.pdfManager.impl

import android.net.Uri
import com.blipblipcode.distribuidoraayl.domain.models.ResultType
import com.blipblipcode.distribuidoraayl.domain.models.sales.Sale
import com.blipblipcode.distribuidoraayl.domain.useCase.pdfManager.IGeneratePreviewUseCase
import com.blipblipcode.distribuidoraayl.domain.useCase.pdfManager.IPdfManagerRepository
import javax.inject.Inject

internal class GeneratePreviewUseCase @Inject constructor(
    private val pdfManagerRepository: IPdfManagerRepository
):IGeneratePreviewUseCase {
    override suspend fun invoke(sale: Sale): ResultType<Uri> {
        return pdfManagerRepository.generatePreview(sale)
    }

}