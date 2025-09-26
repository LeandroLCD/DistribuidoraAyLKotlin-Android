package com.blipblipcode.distribuidoraayl.ui.widgets.input

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.blipblipcode.distribuidoraayl.R

@Composable
fun EmailTextField(
    value: String,
    isError: Boolean = false,
    errorMessage: String = "",
    modifier: Modifier = Modifier,
    onNext: (() -> Unit)? = null,
    onChangedValue: (String) -> Unit,
) {
    OutlinedTextField(value,
        onValueChange = {
            onChangedValue.invoke(it)
        },
        modifier = modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
            errorBorderColor = MaterialTheme.colorScheme.error,
            focusedTextColor = MaterialTheme.colorScheme.primary,
            selectionColors = TextSelectionColors(
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next,
        ),
        keyboardActions = KeyboardActions(onNext = {
            onNext?.invoke()
        }),
        shape = RoundedCornerShape(15.dp),
        label = { Text(stringResource(R.string.email)) },
        isError = isError,
        supportingText = {
            if (isError) {
                Text(errorMessage, color = MaterialTheme.colorScheme.error)
            }
        },
        trailingIcon = {
            Icon(Icons.Default.Email, null)
        }
    )
}