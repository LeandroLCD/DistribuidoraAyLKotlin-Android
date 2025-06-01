package com.blipblipcode.distribuidoraayl.domain.useCase.pdfManager

import android.net.Uri
import com.blipblipcode.distribuidoraayl.domain.models.ResultType
import com.blipblipcode.distribuidoraayl.domain.models.sales.Sale

interface IGeneratePreviewUseCase {
    suspend operator fun invoke(sale: Sale): ResultType<Uri>
}