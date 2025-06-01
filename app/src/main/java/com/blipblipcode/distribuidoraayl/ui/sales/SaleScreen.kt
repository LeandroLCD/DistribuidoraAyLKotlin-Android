package com.blipblipcode.distribuidoraayl.ui.sales

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.animation.AnimatedVisibility
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
import com.blipblipcode.distribuidoraayl.ui.sales.models.SaleUiState
import com.blipblipcode.distribuidoraayl.ui.sales.newSale.NewSaleScreen
import com.blipblipcode.distribuidoraayl.ui.sales.pdfView.PdfScreen
import com.blipblipcode.distribuidoraayl.ui.widgets.dialog.PaymentDialog
import com.blipblipcode.distribuidoraayl.ui.widgets.loading.LoadingScreen

@Composable
fun SaleScreen(viewModel: SaleViewModel = hiltViewModel(),
               openDrawer: () -> Unit){
    val uiState by viewModel.uiState.collectAsState()
    var showPayment by remember {
        mutableStateOf(false)
    }
    Box(Modifier.fillMaxSize()){
        AnimatedContent(uiState) { ui->
            when(ui){
                is SaleUiState.FinishSale -> {
                    PdfScreen(ui.doc.uri, ui.doc.number){
                        viewModel.onUiChanged(SaleUiState.NewSale)
                    }
                }
                SaleUiState.NewSale -> {
                    NewSaleScreen(viewModel, openDrawer)
                }

                is SaleUiState.PreviewSale -> {
                    PdfScreen(ui.uri, onGenerateDte = {
                        showPayment = true
                    }, onInsertNote ={

                    }){
                        viewModel.onUiChanged(SaleUiState.NewSale)
                    }
                    AnimatedVisibility(showPayment){
                        PaymentDialog(
                            title = stringResource(R.string.generate_fc),
                            onDismiss = {
                                showPayment = false
                            }, onConfirm = {pay->
                                viewModel.onGenerateDte(pay, ui.sale)
                                showPayment = false
                            })
                    }
                }
            }
        }
        val isLoading by viewModel.isLoading.collectAsState()
        LoadingScreen(isLoading = isLoading,
            modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f))){
            Text(stringResource(R.string.gnerating_document))
        }

    }
}