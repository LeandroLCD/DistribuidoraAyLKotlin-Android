package com.blipblipcode.distribuidoraayl.domain.models.sales

data class Resolution (
    val number: Int,
    val date: String
){
    override fun toString(): String {
        return "$number de $date"
    }
}
