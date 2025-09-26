package com.blipblipcode.distribuidoraayl.domain.models.products

data class Product(
    val uid: String = "",
    val category: Category?,
    val brandId: String,
    val description: String,
    val sku: String,
    val udm: Udm,
    val barCode: String,
    val name: String,
    val grossPrice: Double,
    val offer: Offer
)