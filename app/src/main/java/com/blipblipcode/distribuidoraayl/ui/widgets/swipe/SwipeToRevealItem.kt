package com.blipblipcode.distribuidoraayl.ui.widgets.swipe

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


class SwipeMenuState(
    initialOffset: Float = 0f,
    val maxMenuWidth: Dp
) {
    val offsetX = Animatable(initialOffset)

    suspend fun closeMenu() {
            offsetX.animateTo(0f)
    }

    suspend fun openMenu(density: Density) {
        val menuWidthPx = with(density) { maxMenuWidth.toPx() }
        offsetX.animateTo(-menuWidthPx)
    }

    suspend fun snapTo(newOffset: Float){
            offsetX.snapTo(newOffset)
    }
}

@Composable
fun rememberSwipeMenuState(maxMenuWidth: Dp = 120.dp): SwipeMenuState {
    return remember { SwipeMenuState(maxMenuWidth = maxMenuWidth) }
}


@Composable
fun SwipeMenuItem(
    state: SwipeMenuState,
    modifier: Modifier = Modifier,
    contentMenu: @Composable RowScope.() -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    val density = LocalDensity.current
    val menuWidthPx = with(density) { state.maxMenuWidth.toPx() }
    val coroutineScope = rememberCoroutineScope()

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragEnd = {
                        coroutineScope.launch {
                            val currentOffset = state.offsetX.value
                            if (currentOffset <= -menuWidthPx / 2) {
                                state.openMenu(density)
                            } else {
                                state.closeMenu()
                            }
                        }
                    },
                    onHorizontalDrag = { _, dragAmount ->
                        coroutineScope.launch {
                            val newOffset = state.offsetX.value + dragAmount
                            state.snapTo(newOffset.coerceIn(-menuWidthPx, 0f))
                        }
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        val weight = minWidth + 10.dp


        // Contenido principal
        Row(
            modifier = modifier
                .fillMaxWidth()
                //.offset { IntOffset(state.offsetX.value.roundToInt(), 0) }
                .background(MaterialTheme.colorScheme.surface)
        ) {
            content()
        }

        // Menú de acciones
        Row(
            modifier = modifier
                .align(Alignment.CenterStart)
                .width(state.maxMenuWidth)
                .offset(x = weight)
                .offset { IntOffset(state.offsetX.value.roundToInt(), 0) }
        ) {
            contentMenu()
        }
    }
}


