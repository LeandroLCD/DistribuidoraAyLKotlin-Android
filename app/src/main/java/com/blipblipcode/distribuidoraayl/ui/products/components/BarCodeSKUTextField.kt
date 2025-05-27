package com.blipblipcode.distribuidoraayl.ui.products.components

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.blipblipcode.distribuidoraayl.R

@Composable
fun BarCodeSKUTextField(
    value: String,
    @StringRes label: Int,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    isReadOnly: Boolean = false,
    errorMessage: String? = null,
    onClickScanner: () -> Unit,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Next
    ),
    onValueChange: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(stringResource(label))
        },
        modifier = modifier,
        isError = isError && !isReadOnly, colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
            disabledBorderColor = MaterialTheme.colorScheme.onSurface,
            disabledTextColor = MaterialTheme.colorScheme.onSurface,
            errorSupportingTextColor = Color.Blue,
            focusedSupportingTextColor = Color.Blue
        ),
        leadingIcon = {
            IconButton(onClick = {
                onClickScanner.invoke()
            }) {
                Icon(painterResource(R.drawable.barcode_scanner), contentDescription = "Scanner")
            }
        },
        trailingIcon = trailingIcon,
        readOnly = isReadOnly,
        keyboardOptions = keyboardOptions,
        keyboardActions = KeyboardActions(onNext = {
            focusManager.moveFocus(FocusDirection.Down)
        }),
        supportingText = {
            if (isError && !isReadOnly) {
                errorMessage?.let {
                    Text(text = it)
                }
            }
        })


}