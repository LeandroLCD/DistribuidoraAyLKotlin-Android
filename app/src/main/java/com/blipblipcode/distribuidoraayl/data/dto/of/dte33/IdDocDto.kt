package com.blipblipcode.distribuidoraayl.data.dto.of.dte33


import com.google.gson.annotations.SerializedName

data class IdDocDto(
    @SerializedName("FchEmis")
    val fchEmis: String,
    @SerializedName("FmaPago")
    val fmaPago: Int,
    @SerializedName("Folio")
    val folio: Int = 0,
    @SerializedName("TipoDTE")
    val tipoDTE: Int
)