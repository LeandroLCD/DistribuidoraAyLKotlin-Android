package com.blipblipcode.distribuidoraayl.ui.widgets.choices

import kotlinx.serialization.Serializable

@Serializable
data class FieldChoice<T>(val data: T, val display: String)