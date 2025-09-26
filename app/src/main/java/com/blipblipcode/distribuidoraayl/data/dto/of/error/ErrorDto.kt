package com.blipblipcode.distribuidoraayl.data.dto.of.error


import com.google.gson.annotations.SerializedName

data class ErrorDto(
    @SerializedName("code")
    val code: String,
    @SerializedName("details")
    val details: List<DetailDto>,
    @SerializedName("message")
    val message: String
)