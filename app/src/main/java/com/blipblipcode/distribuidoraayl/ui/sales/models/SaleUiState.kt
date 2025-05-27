package com.blipblipcode.distribuidoraayl.ui.sales.models

import android.net.Uri
import com.blipblipcode.distribuidoraayl.domain.models.sales.Sale

sealed interface SaleUiState {

    data object NewSale: SaleUiState

    data class  PreviewSale(val uri: Uri, val sale: Sale): SaleUiState

    data class FinishSale(val uri: Uri, val docNumber:Int): SaleUiState

}