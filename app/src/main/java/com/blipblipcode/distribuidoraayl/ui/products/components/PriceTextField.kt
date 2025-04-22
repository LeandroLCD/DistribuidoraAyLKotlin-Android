package com.blipblipcode.distribuidoraayl.ui.products.components

import androidx.annotation.StringRes
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.blipblipcode.distribuidoraayl.ui.widgets.input.stringException

@Composable
fun PriceTextField(
    value: String,
    @StringRes labelText: Int,
    isError: Boolean = false,
    isReadOnly: Boolean = false,
    errorException: Throwable? = null,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit
) {
    var text by remember {
        mutableStateOf(value)
    }
    LaunchedEffect(value){
        if(isReadOnly){
            text = value
        }else if(value != text){
            text = value
        }
    }
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        value = text, onValueChange = {
            onValueChange.invoke(it)
            text = it
        },
        isError = isError && !isReadOnly,
        modifier = modifier,
        label = { Text(text = stringResource(labelText)) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
            disabledBorderColor = MaterialTheme.colorScheme.onSurface,
            disabledTextColor = MaterialTheme.colorScheme.onSurface
        ),
        readOnly = isReadOnly,

        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(onNext = {
            focusManager.moveFocus(FocusDirection.Down)
        }),
        supportingText = {
            if (isError && !isReadOnly) {
                errorException?.let {
                    Text(text = stringException(it))
                }
            }
        }
    )
}