package com.blipblipcode.distribuidoraayl.ui.customer.components

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import com.blipblipcode.distribuidoraayl.R
import com.blipblipcode.distribuidoraayl.ui.auth.login.models.DataField

@Composable
fun RutTextField(
    rut:DataField<String>,
    isReadOnly:Boolean = false,
    onRutChanged: (String) -> Unit,
    alignment: Alignment = Alignment.CenterEnd,
    modifier: Modifier = Modifier,
    trailingIcon: @Composable (() -> Unit)? = null) {

    BoxWithConstraints(contentAlignment = alignment, modifier = modifier.fillMaxWidth()) {
        with(LocalDensity.current){
            OutlinedTextField(
                modifier = modifier.width(maxWidth.toPx().div(2).toDp()),
                readOnly = isReadOnly,
                label = {
                    Text(text = stringResource(R.string.rut))
                },
                value = rut.value,
                trailingIcon = {
                        trailingIcon?.invoke()
                },
                onValueChange = {newValue->
                    onRutChanged.invoke(newValue)
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
                    disabledBorderColor =  MaterialTheme.colorScheme.onSurface,
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                )
            )
        }
    }
}

@Composable
fun RutTextField(
    rut:String,
    isReadOnly:Boolean = false,
    onRutChanged: (String) -> Unit,
    alignment: Alignment = Alignment.CenterEnd,
    modifier: Modifier = Modifier,
    trailingIcon: @Composable (() -> Unit)? = null) {

    BoxWithConstraints(contentAlignment = alignment, modifier = modifier.fillMaxWidth()) {
        with(LocalDensity.current){
            OutlinedTextField(
                modifier = modifier.width(maxWidth.toPx().div(2).toDp()),
                readOnly = isReadOnly,
                label = {
                    Text(text = stringResource(R.string.rut))
                },
                value = rut,
                trailingIcon = {
                    trailingIcon?.invoke()
                },
                onValueChange = {newValue->
                    onRutChanged.invoke(newValue)
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
                    disabledBorderColor =  MaterialTheme.colorScheme.onSurface,
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                )
            )
        }
    }
}