package com.blipblipcode.distribuidoraayl.ui.products.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.blipblipcode.distribuidoraayl.R
import com.blipblipcode.distribuidoraayl.ui.widgets.buttons.ButtonRedAyL

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCategoryDialog(show: Boolean, onDismiss: () -> Unit, onSave: (String) -> Unit) {
    AnimatedVisibility(show) {
        var name by remember {
            mutableStateOf("")
        }
        BasicAlertDialog(onDismissRequest = onDismiss) {

            Surface {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        text = stringResource(R.string.add_category),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(name,
                        onValueChange = {
                            name = it
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
                            focusedTextColor = MaterialTheme.colorScheme.primary,
                            selectionColors = TextSelectionColors(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                            )
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Go,
                        ),
                        keyboardActions = KeyboardActions(onGo = {

                        }),
                        shape = RoundedCornerShape(15.dp),
                        label = { Text(stringResource(R.string.name)) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ButtonRedAyL(onClick = {
                        onSave.invoke(name)
                        onDismiss.invoke()
                    }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                        Text(stringResource(R.string.accept))
                    }
                }
            }
        }

    }
}