package com.blipblipcode.distribuidoraayl.ui.navigationGraph.routes

import kotlinx.serialization.Serializable

@Serializable
sealed class ProductScreen: Screen  {
    @Serializable
    data object List : ProductScreen()
    @Serializable
    data object Add : ProductScreen()
    @Serializable
    data class Detail(val uId: String, val isEditable: Boolean = false) : ProductScreen()
}