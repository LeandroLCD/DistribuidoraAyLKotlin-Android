package com.blipblipcode.distribuidoraayl.ui.navigationGraph.routes

import kotlinx.serialization.Serializable

@Serializable
sealed class CustomerScreen {
    @Serializable
    data object List : CustomerScreen()
    @Serializable
    data object Add : CustomerScreen()
    @Serializable
    data class Detail(val rut: String, val isEditable: Boolean = false) : CustomerScreen()
}