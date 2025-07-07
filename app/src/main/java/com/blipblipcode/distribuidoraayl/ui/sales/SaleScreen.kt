package com.blipblipcode.distribuidoraayl.ui.sales

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.blipblipcode.distribuidoraayl.R
import com.blipblipcode.distribuidoraayl.domain.models.sales.DocFormat
import com.blipblipcode.distribuidoraayl.ui.sales.models.SaleUiState
import com.blipblipcode.distribuidoraayl.ui.sales.newSale.NewSaleScreen
import com.blipblipcode.distribuidoraayl.ui.sales.pdfView.PdfScreen
import com.blipblipcode.distribuidoraayl.ui.sales.preview50mm.Format50mmPreview
import com.blipblipcode.distribuidoraayl.ui.sales.preview50mm.Format50mmSale
import com.blipblipcode.distribuidoraayl.ui.widgets.dialog.PaymentDialog
import com.blipblipcode.distribuidoraayl.ui.widgets.loading.LoadingScreen

@Composable
fun SaleScreen(
    viewModel: SaleViewModel = hiltViewModel(),
    openDrawer: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val isLetter by viewModel.isLetter.collectAsState()
    var showPayment by remember {
        mutableStateOf(false)
    }
    Box(Modifier.fillMaxSize()) {
        AnimatedContent(uiState) { ui ->
            when (ui) {

                SaleUiState.NewSale -> {
                    NewSaleScreen(viewModel, openDrawer)
                }

                is SaleUiState.PreviewSale -> {
                    when (ui.doc.format) {
                        DocFormat.F80MM -> {
                            Format50mmPreview(ui.doc, onGenerateDte = {
                                showPayment = true
                            }, onInsertNote = {
                                //TODO implementar insertNOTE
                            }) {
                                viewModel.onUiChanged(SaleUiState.NewSale)
                            }
                        }
                        DocFormat.LETTER -> {
                            PdfScreen(ui.doc.uri, onGenerateDte = {
                                showPayment = true
                            }, onInsertNote = {
                                //TODO implementar insertNOTE
                            }) {
                                viewModel.onUiChanged(SaleUiState.NewSale)
                            }
                        }
                    }

                    AnimatedVisibility(showPayment) {
                        PaymentDialog(
                            title = stringResource(R.string.generate_fc),
                            onDismiss = {
                                showPayment = false
                            }, onConfirm = { pay ->
                                viewModel.onGenerateDte(pay, ui.doc.sale, isLetter = isLetter)
                                showPayment = false
                            })
                    }

                }

                is SaleUiState.FinishSale -> {
                    when (ui.doc.format) {
                        DocFormat.F80MM -> {
                            Format50mmSale(ui.doc) {
                                viewModel.onUiChanged(SaleUiState.NewSale)
                            }
                        }

                        DocFormat.LETTER -> {
                            PdfScreen(ui.doc.uri, ui.doc.number) {
                                viewModel.onUiChanged(SaleUiState.NewSale)
                            }
                        }
                    }
                }
            }
        }
        val isLoading by viewModel.isLoading.collectAsState()
        LoadingScreen(
            isLoading = isLoading,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
        ) {
            Text(stringResource(R.string.generating_document))
        }

    }
}