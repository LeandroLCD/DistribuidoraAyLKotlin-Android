package com.blipblipcode.distribuidoraayl.ui.widgets.input

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.blipblipcode.distribuidoraayl.R

@Composable
fun PasswordTextField(
    value: String,
    isError: Boolean = false,
    errorMessage: String = "",
    modifier: Modifier = Modifier,
    isVisiblePassword: Boolean = false,
    imeAction: ImeAction = ImeAction.Next,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    onChangedPasswordVisibility: (Boolean) -> Unit,
    onChangedValue: (String) -> Unit,
) {
    OutlinedTextField(value,
        onValueChange = {
            onChangedValue.invoke(it)
        },
        modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
            errorBorderColor = MaterialTheme.colorScheme.error,
            focusedTextColor = MaterialTheme.colorScheme.primary,
            selectionColors = TextSelectionColors(
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = imeAction
        ),
        singleLine = true,
        keyboardActions = keyboardActions,
        shape = RoundedCornerShape(15.dp),
        visualTransformation = if (isVisiblePassword) VisualTransformation.None else PasswordVisualTransformation(),
        label = { Text(stringResource(R.string.password)) },
        isError = isError,
        supportingText = {
            if (isError) {
                Text(errorMessage, color = MaterialTheme.colorScheme.error)
            }
        },
        trailingIcon = {
            IconButton(onClick = {
                onChangedPasswordVisibility.invoke(!isVisiblePassword)
            }) {
                if (isVisiblePassword) {
                    Icon(Icons.Default.VisibilityOff, null)
                } else {
                    Icon(Icons.Default.Visibility, null)
                }
            }
        }
    )
}