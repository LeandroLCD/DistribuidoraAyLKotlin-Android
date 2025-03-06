package com.blipblipcode.distribuidoraayl.ui.products.components

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.blipblipcode.distribuidoraayl.R

@Composable
fun BarCodeSKUTextField(
    value: String,
    @StringRes label: Int,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorMessage: String? = null,
    onClickScanner: () -> Unit,
    onValueChange: (String) -> Unit
) {

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(stringResource(label))
        },
        modifier = modifier,
        isError = isError, colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
            disabledBorderColor = MaterialTheme.colorScheme.onSurface,
            disabledTextColor = MaterialTheme.colorScheme.onSurface),
        leadingIcon = {
            IconButton(onClick = {
                onClickScanner.invoke()
            }) {
                Icon(painterResource(R.drawable.barcode_scanner), contentDescription = "Scanner")
            }
        },
        supportingText = {
            if (isError) {
                errorMessage?.let {
                    Text(text = it)
                }
            }
        })
}