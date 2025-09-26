package com.blipblipcode.distribuidoraayl.domain.models.customer

data class Region(
    val id: String,
    val name: String,
    val communes: List<Commune>
)