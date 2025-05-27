package com.blipblipcode.distribuidoraayl.domain.models.sales


class SalesItem(
    val index: Int,
    val sku:String,
    val barCode:String? = null,
    val name:String,
    val description:String? = null,
    val price:Int,
    val quantity:Int = 1
)