package com.blipblipcode.distribuidoraayl.ui.sales.preview50mm

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import com.blipblipcode.distribuidoraayl.R
import com.blipblipcode.distribuidoraayl.domain.models.printState.PrinterState
import com.blipblipcode.distribuidoraayl.domain.models.sales.DocumentElectronic
import com.blipblipcode.distribuidoraayl.ui.sales.preview50mm.components.Format50mmView
import com.blipblipcode.distribuidoraayl.ui.utils.shareFileByEmail
import com.blipblipcode.distribuidoraayl.ui.widgets.buttons.FabOption
import com.blipblipcode.distribuidoraayl.ui.widgets.buttons.SemicircularFabMenu
import com.blipblipcode.distribuidoraayl.ui.widgets.topBat.PdfViewTopBar
import java.io.File

@Composable
fun Format50mmPreview(
    printerState: PrinterState,
    onPrinter: () -> Unit,
    doc: DocumentElectronic,
    onGenerateDte: () -> Unit,
    onInsertNote: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    Scaffold(topBar = {
        PdfViewTopBar(
            printerSate = printerState,
            print = onPrinter,
            onBackPressed = onBack
        )
    }) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF909090))
                .padding(innerPadding), contentAlignment = Alignment.Center
        ) {

            Format50mmView(doc)

            SemicircularFabMenu(
                modifier = Modifier.align(Alignment.BottomEnd),
                options = buildList {
                    add(
                        FabOption.VectorIcon(
                            icon = Icons.Default.Share,
                            onClick = {
                                val file = File(doc.uri.path!!)
                                val fileUri = FileProvider.getUriForFile(
                                    context,
                                    "${context.packageName}.provider",
                                    file
                                )
                                context.shareFileByEmail(
                                    fileUri, title = "Compartir PDDF",
                                    typeFile = "application/pdf"
                                )
                            }
                        ))
                    add(
                        FabOption.VectorIcon(
                            icon = Icons.AutoMirrored.Filled.ReceiptLong,
                            onClick = {
                                onGenerateDte.invoke()
                            }
                        ))
                    add(
                        FabOption.VectorIcon(
                            icon = Icons.Filled.Receipt,
                            onClick = {
                                onInsertNote.invoke()
                            }
                        ))
                })

        }
        BackHandler {
            onBack.invoke()
        }
    }
}

@Composable
fun Format50mmSale(
    printerState: PrinterState,
    doc: DocumentElectronic,
    onPrinter: () -> Unit,
    onBack: () -> Unit
) {

    val context = LocalContext.current
    Scaffold(
        topBar = {
            PdfViewTopBar(
                printerSate = printerState,
                print = onPrinter,
                onBackPressed = onBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val file = File(doc.uri.path!!)
                    val fileUri = FileProvider.getUriForFile(
                        context,
                        "${context.packageName}.provider",
                        file
                    )
                    context.shareFileByEmail(
                        fileUri, title = context.getString(R.string.share),
                        typeFile = "application/pdf"
                    )
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Share, null)
            }
        }) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF909090))
                .padding(innerPadding), contentAlignment = Alignment.Center
        ) {

            Format50mmView(doc)


        }
        BackHandler {
            onBack.invoke()
        }
    }
}
