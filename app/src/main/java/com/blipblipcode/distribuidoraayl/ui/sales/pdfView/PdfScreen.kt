package com.blipblipcode.distribuidoraayl.ui.sales.pdfView

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.blipblipcode.distribuidoraayl.R
import com.blipblipcode.distribuidoraayl.ui.utils.shareFileByEmail
import com.blipblipcode.distribuidoraayl.ui.widgets.buttons.FabOption
import com.blipblipcode.distribuidoraayl.ui.widgets.buttons.SemicircularFabMenu
import com.blipblipcode.distribuidoraayl.ui.widgets.pdfViewer.PdfViewer
import com.blipblipcode.distribuidoraayl.ui.widgets.loading.LoadingScreen
import com.blipblipcode.distribuidoraayl.ui.widgets.topBat.DocumentTopBar
import com.blipblipcode.distribuidoraayl.ui.widgets.topBat.PreViewTopBar
import java.io.File

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PdfScreen(
    uri: Uri,
    onGenerateDte: () -> Unit,
    onInsertNote: () -> Unit,
    onBack: () -> Unit
) {

    val context = LocalContext.current
    Scaffold(topBar = {
        PreViewTopBar(onBack)
    }) { innerPadding ->
        var isLoading by remember {
            mutableStateOf(Triple<Boolean, Int?, Int?>(false, null, null))
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding), contentAlignment = Alignment.Center
        ) {

            PdfViewer(
                uri,
                modifier = Modifier.fillMaxSize(),
                loadingListener = { loading, currentPage, maxPage ->
                    isLoading = Triple(loading, currentPage, maxPage)
                },

                onError = {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = stringResource(R.string.error_load_pdf))
                    }
                })

            LoadingScreen(isLoading.first) {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    isLoading.second?.let {
                        Text(text = stringResource(R.string.pague, it))
                    }

                    isLoading.third?.let {
                        Text(text = stringResource(R.string.of, it))
                    }
                }
            }
            AnimatedVisibility(!isLoading.first) {
                SemicircularFabMenu(
                    modifier = Modifier.align(Alignment.BottomEnd),
                    options = buildList {
                        add(
                            FabOption.VectorIcon(
                            icon = Icons.Default.Share,
                            onClick = {
                                val file = File(uri.path!!)
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
        }
        BackHandler {
            onBack.invoke()
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PdfScreen(
    uri: Uri,
    docNumber: Int,
    onBack: () -> Unit
) {

    val context = LocalContext.current
    Scaffold(topBar = {
        DocumentTopBar(stringResource(R.string.document, docNumber), onBack)
    }, floatingActionButton = {
        FloatingActionButton(
            onClick = {
                val file = File(uri.path!!)
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
    }
    ) { innerPadding ->
        var isLoading by remember {
            mutableStateOf(Triple<Boolean, Int?, Int?>(false, null, null))
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding), contentAlignment = Alignment.Center
        ) {

            PdfViewer(
                uri,
                modifier = Modifier.fillMaxSize(),
                loadingListener = { loading, currentPage, maxPage ->
                    isLoading = Triple(loading, currentPage, maxPage)
                },

                onError = {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = stringResource(R.string.error_load_pdf))
                    }
                })

            LoadingScreen(isLoading.first) {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    isLoading.second?.let {
                        Text(text = stringResource(R.string.pague, it))
                    }

                    isLoading.third?.let {
                        Text(text = stringResource(R.string.of, it))
                    }
                }
            }
        }
        BackHandler {
            onBack.invoke()
        }
    }
}
