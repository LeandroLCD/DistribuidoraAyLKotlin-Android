package com.blipblipcode.distribuidoraayl.ui.widgets.buttons

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ButtonRedAyL(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onSurface
    ),
    enabled:Boolean = true,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable () -> Unit
) {
    Button(
        enabled = enabled,
        contentPadding = contentPadding,
        onClick = {
            onClick.invoke()
        },
        modifier = modifier,
        colors = colors,
        shape = MaterialTheme.shapes.small
    ) {
        content()
    }
}