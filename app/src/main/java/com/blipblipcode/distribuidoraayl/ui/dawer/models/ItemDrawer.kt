package com.blipblipcode.distribuidoraayl.ui.dawer.models

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import com.blipblipcode.distribuidoraayl.ui.navigationGraph.routes.Screen

sealed class ItemDrawer(open val route: Screen, @StringRes open val name: Int) {
    data class VectorIcon(
        override val route: Screen,
        @StringRes override val name: Int,
        val icon: ImageVector
    ) : ItemDrawer(route, name)

    data class DrawableIcon(
        override val route: Screen,
        @StringRes override val name: Int,
        @DrawableRes val drawable: Int
    ) : ItemDrawer(route, name)
}