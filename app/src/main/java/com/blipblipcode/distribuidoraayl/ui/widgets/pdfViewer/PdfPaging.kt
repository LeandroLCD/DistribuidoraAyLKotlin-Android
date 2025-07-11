package com.blipblipcode.distribuidoraayl.ui.widgets.pdfViewer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.launch

@ExperimentalFoundationApi
@Composable
fun PdfPaging(
    painter: Painter,
    modifier: Modifier = Modifier,
    paddingValue : PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 24.dp),
    backgroundColor: Color = Color.Transparent,
    pageColor:Color = Color.White,
    imageAlign: Alignment = Alignment.Center,
    shape: Shape = RectangleShape,
    maxScale: Float = 1f,
    minScale: Float = 3f,
    contentScale: ContentScale = ContentScale.Fit,
    isRotation: Boolean = false,
    isZoomable: Boolean = true,
    scrollState: ScrollableState? = null,
) {
    val coroutineScope = rememberCoroutineScope()

    val scale = remember { mutableFloatStateOf(1f) }
    val rotationState = remember { mutableFloatStateOf(1f) }
    val offsetX = remember { mutableFloatStateOf(0f) }
    val offsetY = remember { mutableFloatStateOf(0f) }
    Box {
        Box(
            modifier = modifier
                .background(backgroundColor)
                .padding(paddingValue)
                .combinedClickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { /* NADA :) */ },
                    onDoubleClick = {
                        if (scale.floatValue >= 2f) {
                            scale.floatValue = 1f
                            offsetX.floatValue = 0f
                            offsetY.floatValue = 0f
                        } else scale.floatValue = 3f
                    },
                )
                .pointerInput(Unit) {
                    if (isZoomable) {
                        detectTransformGestures { centroid, pan, zoom, rotation ->
                            scale.floatValue *= zoom
                            if (scale.floatValue > 1) {
                                scrollState?.run {
                                    coroutineScope.launch {
                                        setScrolling(false)
                                    }
                                }
                                offsetX.floatValue += pan.x
                                offsetY.floatValue += pan.y
                                rotationState.floatValue += rotation
                                scrollState?.run {
                                    coroutineScope.launch {
                                        setScrolling(true)
                                    }
                                }
                            } else {
                                scale.floatValue = 1f
                                offsetX.floatValue = 0f
                                offsetY.floatValue = 0f
                            }
                        }
                    }
                }
                .graphicsLayer {
                    if (isZoomable) {
                        scaleX = maxOf(maxScale, minOf(minScale, scale.floatValue))
                        scaleY = maxOf(maxScale, minOf(minScale, scale.floatValue))
                        if (isRotation) {
                            rotationZ = rotationState.floatValue
                        }
                        translationX = offsetX.floatValue
                        translationY = offsetY.floatValue
                    }
                }

        ) {
            Surface(
                shape = shape,
                color = pageColor,
                shadowElevation = 5.dp,
            ) {

                Image(
                    painter = painter,
                    contentDescription = null,
                    contentScale = contentScale,
                    modifier = modifier
                        .align(imageAlign)
                )
            }
        }
        AnimatedVisibility(
            visible = scale.floatValue != 1f,
            modifier = Modifier.align(Alignment.TopEnd),
            enter = scaleIn(),
            exit = scaleOut()
        ){

            IconButton(onClick = {
                scale.floatValue = 1f
                rotationState.floatValue = 1f
                offsetX.floatValue = 0f
                offsetY.floatValue = 0f
            },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onSurface)){
                Icon(Icons.Default.Close, contentDescription = null)
            }
        }

    }

}

suspend fun ScrollableState.setScrolling(value: Boolean) {
    scroll(scrollPriority = MutatePriority.PreventUserInput) {
        when (value) {
            true -> Unit
            else -> awaitCancellation()
        }
    }
}