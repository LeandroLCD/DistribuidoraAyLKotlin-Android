package com.blipblipcode.distribuidoraayl.ui.widgets.topBat

import android.util.Log
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.updateTransition
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Print
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.blipblipcode.distribuidoraayl.R
import com.blipblipcode.distribuidoraayl.domain.models.printState.PrinterState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PdfViewTopBar(
    title: String = stringResource(R.string.preview),
    printerSate: PrinterState,
    print: () -> Unit,
    onBackPressed: () -> Unit
) {
    val transition = updateTransition(targetState = printerSate, label = "")
    val color by transition.animateColor { state ->
        when (state) {
            PrinterState.Idle -> Color.Green
            PrinterState.Connected, PrinterState.Ready -> Color.Blue
            PrinterState.BluetoothDisabled, PrinterState.Disconnected, is PrinterState.Exception -> Color.Gray
            PrinterState.Printing -> Color.Yellow
        }
    }
    Log.d("Bluetooth", "printerSate: $printerSate")
    TopAppBar(
        navigationIcon = {
            IconButton(
                onClick = {
                    onBackPressed.invoke()
                }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
            }
        },
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.Bold
            )

        },
        actions = {
            IconButton(onClick = {
                print.invoke()
            }) {
                Icon(Icons.Default.Print, tint = color, contentDescription = null)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentTopBar(title: String, onBackPressed: () -> Unit) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = {
                onBackPressed.invoke()
            }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
            }
        },
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.Bold
            )

        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}