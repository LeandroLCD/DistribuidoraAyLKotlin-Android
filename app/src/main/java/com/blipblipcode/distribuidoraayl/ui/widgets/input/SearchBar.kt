package com.blipblipcode.distribuidoraayl.ui.widgets.input

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Composable
fun SearchBar(
    value:String,
    modifier: Modifier = Modifier,
    shape:Shape = RoundedCornerShape(8.dp),
    label: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onSearch:(String)->Unit) {


    SelectionContainer(modifier){

        OutlinedTextField(
            modifier = Modifier.then(modifier),
            value = value,
            onValueChange = {
                onSearch(it)
            },
            leadingIcon = {
                Icon(
                    Icons.Filled.Search,
                    "Search",
                    modifier = Modifier
                        .padding(top = 2.dp),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            },
            trailingIcon = {
                when {
                    value.isNotEmpty() -> IconButton(onClick = {
                        onSearch("")
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Clear,
                            contentDescription = "Clear",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            },
            shape = shape,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                errorCursorColor = MaterialTheme.colorScheme.error,
                errorBorderColor = MaterialTheme.colorScheme.error,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
                cursorColor = MaterialTheme.colorScheme.primary,
                selectionColors = TextSelectionColors(
                    MaterialTheme.colorScheme.onSurface,
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                )
            ),
            label = {
                Text(
                    text = label
                )
            },
            maxLines = 1,
            singleLine = true,
            keyboardOptions =keyboardOptions
        )

    }
}