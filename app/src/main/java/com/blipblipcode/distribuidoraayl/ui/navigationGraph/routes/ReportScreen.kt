package com.blipblipcode.distribuidoraayl.ui.navigationGraph.routes

import kotlinx.serialization.Serializable

@Serializable
sealed class ReportScreen : Screen {

    @Serializable
    data object List : ReportScreen()

    @Serializable
    data class Detail(val id: Int) : ReportScreen()
}