package com.blipblipcode.distribuidoraayl.ui.sales.models

import com.blipblipcode.distribuidoraayl.domain.models.sales.DocumentElectronic

sealed interface SaleUiState {

    data object NewSale: SaleUiState

    data class  PreviewSale(val doc: DocumentElectronic): SaleUiState

    data class FinishSale(val doc: DocumentElectronic): SaleUiState

}