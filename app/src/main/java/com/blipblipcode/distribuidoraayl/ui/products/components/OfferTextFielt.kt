package com.blipblipcode.distribuidoraayl.ui.products.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
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
import androidx.compose.ui.unit.dp
import com.blipblipcode.distribuidoraayl.R
import com.blipblipcode.distribuidoraayl.ui.widgets.input.stringException
import java.lang.Error

@Composable
fun OfferTextField(
    value:String,
    isError: Boolean,
    errorException: Throwable?,
    modifier: Modifier = Modifier,
    isOffer:Boolean = false,
    isReadOnly:Boolean = false,
    onOfferChange: (Boolean) -> Unit,
    onValueChange: (String) -> Unit,
) {
    var text by remember {
        mutableStateOf(value)
    }
    val focusManager = LocalFocusManager.current
    LaunchedEffect(value){
        if(isReadOnly) text = value
    }
    OutlinedTextField(
        value = text,
        singleLine = true,
        readOnly = isReadOnly,
        isError = isError && !isReadOnly,
        onValueChange = {
            val newValue = when{
                it.isEmpty() -> "0"
                it.length>1 && it.first() == '0' -> it.substring(1)
                else -> it
            }
            text = newValue
            onValueChange(newValue)
        },
        leadingIcon = {
            Checkbox(isOffer,
                enabled = !isReadOnly,
                modifier = Modifier.padding(vertical = 2.dp),
                onCheckedChange = {
              onOfferChange(it)
            })
        },
        label = { Text(text = stringResource(R.string.offert)) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
            disabledBorderColor = MaterialTheme.colorScheme.onSurface,
            disabledTextColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = modifier,

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