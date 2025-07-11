package com.blipblipcode.distribuidoraayl.data.dto.of

import com.google.gson.annotations.SerializedName

data class ResolutionDto(
    @SerializedName("numero")
    val number: Int,
    @SerializedName("fecha")
    val date: String
){
    override fun toString(): String {
        return "$number de $date"
    }
}