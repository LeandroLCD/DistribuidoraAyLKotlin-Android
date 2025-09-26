package com.blipblipcode.distribuidoraayl.ui.utils

import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.IntSize

fun Modifier.fillWindowRoot(): Modifier = composed {
    val view = LocalView.current
    var windowSize by remember { mutableStateOf(IntSize.Zero) }

    DisposableEffect(view) {
        val metrics = view.context.resources.displayMetrics
        windowSize = IntSize(metrics.widthPixels, metrics.heightPixels)
        onDispose { }
    }

    this
        .then(Modifier.onGloballyPositioned {
            // no-op, but forces recomposition if needed
        })
        .then(
            with(LocalDensity.current) {
                Modifier
                    .requiredSize(windowSize.width.toDp(), windowSize.height.toDp())

            })
}