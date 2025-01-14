package com.blipblipcode.distribuidoraayl.ui.navigationGraph.routes

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    data object Login : Screen()

    @Serializable
    data object Start : Screen()

    @Serializable
    data object VerifiedAccount : Screen()
}