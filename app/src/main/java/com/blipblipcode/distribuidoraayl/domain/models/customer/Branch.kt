package com.blipblipcode.distribuidoraayl.domain.models.customer

data class Branch(
    val city: String,
    val code: Int,
    val commune: String,
    val address: String,
    val phone: String?,
    val isHouseMatrix:Boolean = false
)
