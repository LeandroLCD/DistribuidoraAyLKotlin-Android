package com.blipblipcode.distribuidoraayl.ui.navigationGraph.routes

import kotlinx.serialization.Serializable

@Serializable
sealed class AuthScreen {
    @Serializable
    data object Login : AuthScreen()

    @Serializable
    data object Start : AuthScreen()

    @Serializable
    data object VerifiedAccount : AuthScreen()
}