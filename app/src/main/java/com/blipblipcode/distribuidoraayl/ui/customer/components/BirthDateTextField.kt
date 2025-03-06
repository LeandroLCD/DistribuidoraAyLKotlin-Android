package com.blipblipcode.distribuidoraayl.ui.customer.components

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextOverflow
import com.blipblipcode.distribuidoraayl.R
import com.blipblipcode.distribuidoraayl.ui.auth.login.models.DataField
import com.blipblipcode.distribuidoraayl.ui.widgets.input.stringException

@Composable
fun BirthDateTextField(birthDate: DataField<String>, onBirthdayChanged:(String)->Unit,
              isReadOnly:Boolean = false,
              modifier: Modifier = Modifier,
              trailingIcon: @Composable (() -> Unit)? = null,) {
    OutlinedTextField(
        value = birthDate.value,
        singleLine = true,
        textStyle = LocalTextStyle.current.copy(lineBreak = LineBreak.Simple),
        enabled = !isReadOnly,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
            disabledBorderColor =  MaterialTheme.colorScheme.onSurface,
            disabledTextColor = MaterialTheme.colorScheme.onSurface,
        ),
        isError = birthDate.isError,
        supportingText = {
            if (birthDate.isError) {
                Text(
                    text = stringException(birthDate.errorException),
                    color = MaterialTheme.colorScheme.error)
            }
        },
        onValueChange = { date ->
            onBirthdayChanged(date)
        },
        trailingIcon = {
            if(!isReadOnly){
                trailingIcon?.invoke()
            }
        },
        label = {
            Text(
                text = stringResource(R.string.birth_day),
                softWrap = false,
                overflow = TextOverflow.Ellipsis
            )
        },
        modifier = modifier,
    )
}