package com.blipblipcode.distribuidoraayl.ui.products.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.blipblipcode.distribuidoraayl.R
import com.blipblipcode.distribuidoraayl.ui.auth.login.models.DataField
import com.blipblipcode.distribuidoraayl.ui.widgets.input.stringException

@Composable
fun ProductNameTextField(value: DataField<String>, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value.value, onValueChange = onValueChange,
        isError = value.isError,
        label = { Text(text = stringResource(R.string.product_name)) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
            disabledBorderColor = MaterialTheme.colorScheme.onSurface,
            disabledTextColor = MaterialTheme.colorScheme.onSurface
        ),
        supportingText = {
            if (value.isError) {
                value.errorException?.let {
                    Text(text = stringException(it))
                }
            }
        }
    )
}