package com.blipblipcode.distribuidoraayl.ui.widgets.choices

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize

@Composable
inline fun  <T> OptionsSelector(
    initial:String,
    modifier: Modifier = Modifier,
    isReadOnly:Boolean = false,
    isError:Boolean = false,
    errorText:String? = null,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = MaterialTheme.colorScheme.primary,
        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
        disabledBorderColor = MaterialTheme.colorScheme.onSurface,
        disabledTextColor = MaterialTheme.colorScheme.onSurface,
        focusedSuffixColor = Color.Blue,
        errorSupportingTextColor = Color.Blue
    ),
    choices:List<FieldChoice<T>>,
    crossinline label: @Composable ()->Unit,
    crossinline onValueChange: (FieldChoice<T>) -> Unit
) {
    var selected by remember{
        mutableStateOf<FieldChoice<T>?>(null)
    }

    LaunchedEffect(initial, choices) {
        if(initial.isNotBlank()){
            selected = choices.find { it.display == initial }
            selected?.let(onValueChange)
        }else{
            selected = null
        }
    }

    var expander by remember {
        mutableStateOf(false)
    }
    var rowSize by remember {
        mutableStateOf(Size.Zero)
    }
    val iterations = remember { MutableInteractionSource() }
        .also { interactionSource ->
            LaunchedEffect(interactionSource) {
                interactionSource.interactions.collect {
                    if (it is PressInteraction.Release && !isReadOnly) {
                        expander = !expander
                    }
                }
            }
        }

    Column(modifier = modifier) {

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { layoutCoordinates ->
                    rowSize = layoutCoordinates.size.toSize()
                },
            value = selected?.display.orEmpty(),
            interactionSource = iterations,
            label = {
                label()
            },
            trailingIcon = {
                if (!isReadOnly) {
                    TriangleExtender(
                        expander
                    ) { expander = !expander }

                }
            },
            singleLine = true,
            colors = colors,
            enabled = !isReadOnly,
            onValueChange = { },
            readOnly = true,
            isError = isError && !isReadOnly,
            supportingText = {
                if(errorText != null && !isReadOnly){
                    Text(errorText, color = MaterialTheme.colorScheme.error)
                }
            }
        )
        with(LocalDensity.current){
            Box(Modifier.graphicsLayer {
                if(expander){
                    translationY = -16.dp.toPx()
                }
            }){
                DropdownMenu(
                    expanded = expander,
                    modifier = Modifier
                        .width(rowSize.width.toDp())
                        .heightIn(max = 250.dp)
                        .background(colors.focusedContainerColor),
                    onDismissRequest = { expander = false }) {
                    choices.forEach { op ->
                        DropdownMenuItem(
                            colors = MenuDefaults.itemColors(
                                textColor =
                                if (op == selected) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurface
                            ),
                            onClick = {
                                expander = false
                                selected = op
                                onValueChange(op)
                            },
                            text = {
                                Text(
                                    text = op.display,
                                    modifier = Modifier
                                        .padding(top = 10.dp)
                                        .fillMaxWidth(),
                                    softWrap = false,
                                    overflow = TextOverflow.Ellipsis
                                )
                            })

                    }
                }

            }

        }
    }
}


@Composable
fun TriangleExtender(expander: Boolean, onClick: () -> Unit) {
    val icon = if (expander) {
        Icons.Default.ArrowDropUp
    } else {
        Icons.Default.ArrowDropDown
    }
    IconButton(onClick = {
        onClick.invoke()
    }) {
        Icon(
            imageVector = icon,
            contentDescription = "open Selector",
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .padding(2.dp)
        )
    }
}
