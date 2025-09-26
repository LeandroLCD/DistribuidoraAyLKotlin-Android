package com.blipblipcode.distribuidoraayl.domain.models.customer

data class Rubro (
    val id:Int,
    val soc:String,
    val description:String,
){
    fun label() = "$id - $description"
}