package com.blipblipcode.distribuidoraayl.data.dto.of.dte33

import com.google.gson.annotations.SerializedName

data class CodeDto(
    @SerializedName("TpoCodigo")
    val type: String,
    @SerializedName("VlrCodigo")
    val value: String
)