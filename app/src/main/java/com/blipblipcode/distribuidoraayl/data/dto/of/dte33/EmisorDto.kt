package com.blipblipcode.distribuidoraayl.data.dto.of.dte33


import com.google.gson.annotations.SerializedName

data class EmisorDto(
    @SerializedName("Acteco")
    val acteco: String,
    @SerializedName("CdgSIISucur")
    val cdgSIISucur: String,
    @SerializedName("CmnaOrigen")
    val cmnaOrigen: String,
    @SerializedName("DirOrigen")
    val dirOrigen: String,
    @SerializedName("GiroEmis")
    val giroEmis: String,
    @SerializedName("RUTEmisor")
    val rUTEmisor: String,
    @SerializedName("RznSoc")
    val rznSoc: String,
    @SerializedName("Telefono")
    val telefono: String
)