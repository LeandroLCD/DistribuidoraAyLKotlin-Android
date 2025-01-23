package com.blipblipcode.distribuidoraayl.domain.models.of.error


import com.google.gson.annotations.SerializedName

data class ErrorRootDto(
    @SerializedName("error")
    val error: ErrorDto
)