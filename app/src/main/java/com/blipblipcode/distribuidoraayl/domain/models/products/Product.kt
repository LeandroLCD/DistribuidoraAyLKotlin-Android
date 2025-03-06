package com.blipblipcode.distribuidoraayl.domain.models.products

data class Product(
    val uid: String,
    val category: Category?,
    val brandId: String,
    val description: String,
    val sku: Int,
    val udm: Udm,
    val barCode: String,
    val name: String,
    val netPrice: Double,
    val offer: Offer
)