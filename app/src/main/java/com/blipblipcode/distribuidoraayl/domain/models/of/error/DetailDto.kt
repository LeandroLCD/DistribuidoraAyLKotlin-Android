package com.blipblipcode.distribuidoraayl.domain.models.of.error


import com.google.gson.annotations.SerializedName

data class DetailDto(
    @SerializedName("field")
    val field: String,
    @SerializedName("issue")
    val issue: String,
){
    override fun toString(): String {
        return "$field: $issue"
    }
}