package com.blipblipcode.distribuidoraayl.ui.widgets.buttons

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class ToggleOption {
    NONE, LEFT, RIGHT
}

@Composable
fun ToggleButton(
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(50),
    selectedOption: ToggleOption,
    textRight: String,
    textLeft: String,
    selectedDefaultElevation:Dp = 8.dp,
    unselectedElevation: Dp = 0.dp,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    selectedColor: Color = MaterialTheme.colorScheme.surface,
    unSelectedColor: Color = backgroundColor,
    contentPadding: PaddingValues = PaddingValues(4.dp),
    onLeftSelected: () -> Unit,
    onRightSelected: () -> Unit,
) {

    Row(
        modifier = modifier
            .clip(shape)
            .background(backgroundColor)
            .padding(contentPadding)
            .height(48.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        ToggleButtonItem(
            text = textLeft,
            modifier = Modifier.weight(0.5f),
            isSelected = selectedOption == ToggleOption.LEFT,
            shape = shape.copy(topEnd = CornerSize(0.dp), bottomEnd = CornerSize(0.dp)),
            onClick = onLeftSelected,
            selectedElevation = selectedDefaultElevation,
            unSelectedElevation = unselectedElevation,
            background = when(selectedOption){
                ToggleOption.NONE -> backgroundColor
                ToggleOption.LEFT -> selectedColor
                ToggleOption.RIGHT -> unSelectedColor
            },
            contentColor = MaterialTheme.colorScheme.onSurface
        )

        ToggleButtonItem(
            text = textRight,
            modifier = Modifier.weight(0.5f),
            isSelected = selectedOption == ToggleOption.RIGHT,
            shape = shape.copy(topStart = CornerSize(0.dp), bottomStart = CornerSize(0.dp)),
            onClick = onRightSelected,
            selectedElevation = selectedDefaultElevation,
            unSelectedElevation = unselectedElevation,
            background = when(selectedOption){
                ToggleOption.NONE -> backgroundColor
                ToggleOption.LEFT -> unSelectedColor
                ToggleOption.RIGHT -> selectedColor
            },
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
internal fun ToggleButtonItem(
    text: String,
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    shape: Shape,
    background: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary,
    selectedElevation: Dp,
    unSelectedElevation: Dp,
    onClick: () -> Unit
) {
    val transition = updateTransition(!isSelected)
    val elevation = transition.animateDp(label = "elevation") {
        if (it) selectedElevation else unSelectedElevation
    }
    Surface(
        modifier = modifier
            .fillMaxHeight(),
        shape = shape,
        color = background,
        shadowElevation = elevation.value,
        onClick = {
            onClick.invoke()
        }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = text,
                color = contentColor,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Composable
fun rememberToggleButtonState(initialOption: ToggleOption = ToggleOption.NONE): MutableState<ToggleOption> {
    return remember { mutableStateOf(initialOption) }
}


