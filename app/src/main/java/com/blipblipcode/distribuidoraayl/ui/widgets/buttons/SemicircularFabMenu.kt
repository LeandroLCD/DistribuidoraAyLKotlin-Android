package com.blipblipcode.distribuidoraayl.ui.widgets.buttons

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.blipblipcode.distribuidoraayl.ui.utils.fillWindowRoot
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

sealed class FabOption(open val onClick: () -> Unit) {
    data class VectorIcon(
        override val onClick: () -> Unit,
        val icon: ImageVector
    ) : FabOption(onClick)

    data class DrawableIcon(
        override val onClick: () -> Unit,
        @DrawableRes val drawable: Int
    ) : FabOption(onClick)
}

@Composable
fun SemicircularFabMenu(
    modifier: Modifier = Modifier,
    radius:Dp = 90.dp,
    options: List<FabOption>
) {
    var isMenuOpen by remember { mutableStateOf(false) }
    val fabIcon = if (isMenuOpen) Icons.Default.Close else Icons.Default.MoreVert

    Box(modifier = modifier
        .fillMaxSize()
        .navigationBarsPadding()) {
        // Fondo para detección de clics fuera del menú
        if (isMenuOpen) {
            Box(
                modifier = Modifier
                    .fillWindowRoot()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = { isMenuOpen = false }
                    )
            )
        }


        val angleStep = 90f.div(options.size.minus(1))
        val radius = 120.dp

        with(LocalDensity.current) {
            options.forEachIndexed { index, fab ->
                val angle = Math.toRadians((angleStep * index).toDouble())

                // Animables para X e Y
                val offsetX = remember { Animatable(0f) }
                val offsetY = remember { Animatable(0f) }

                LaunchedEffect(isMenuOpen) {
                    val targetX = if (isMenuOpen) -radius.toPx() * cos(angle) else 0.0
                    val targetY = if (isMenuOpen) -radius.toPx() * sin(angle) else 0.0

                    launch {
                        offsetX.animateTo(
                            targetX.toFloat(),
                            animationSpec = tween(durationMillis = 300)
                        )
                    }
                    launch {
                        offsetY.animateTo(
                            targetY.toFloat(),
                            animationSpec = tween(durationMillis = 300)
                        )
                    }
                }

                IconButton(
                    onClick = {
                        fab.onClick.invoke()
                        isMenuOpen = !isMenuOpen
                    },
                    modifier = Modifier
                        .offset { IntOffset(offsetX.value.toInt(), offsetY.value.toInt()) }
                        .align(Alignment.BottomEnd)
                        .size(48.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    )
                ) {
                    when (fab) {
                        is FabOption.VectorIcon -> Icon(
                            imageVector = fab.icon,
                            contentDescription = null
                        )

                        is FabOption.DrawableIcon -> Icon(
                            painter = painterResource(id = fab.drawable),
                            contentDescription = null
                        )
                    }
                }
            }
        }

        IconButton(
            onClick = { isMenuOpen = !isMenuOpen },
            modifier = Modifier
                .align(Alignment.BottomEnd),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            )
        ) {
            Icon(imageVector = fabIcon, contentDescription = null)
        }
    }

}