package com.blipblipcode.distribuidoraayl.ui.products.components

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.blipblipcode.distribuidoraayl.R
import com.blipblipcode.distribuidoraayl.ui.auth.login.models.DataField
import com.blipblipcode.distribuidoraayl.ui.widgets.input.stringException

@Composable
fun DescriptionTextField(
    value: DataField<String>,
    modifier: Modifier = Modifier,
    isReadOnly: Boolean =  false,
    onValueChange: (String) -> Unit
){
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        value = value.value, onValueChange = onValueChange,
        isError = value.isError && !isReadOnly,
        label = { Text(text = stringResource(R.string.description)) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
            disabledBorderColor = MaterialTheme.colorScheme.onSurface,
            disabledTextColor = MaterialTheme.colorScheme.onSurface
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(onNext = {
            focusManager.moveFocus(FocusDirection.Down)
        }),
        readOnly = isReadOnly,
        supportingText = {
            if (value.isError && !isReadOnly) {
                value.errorException?.let {
                    Text(text = stringException(it))
                }
            }
        },
        modifier = modifier
    )
}