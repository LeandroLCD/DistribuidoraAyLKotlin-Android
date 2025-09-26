package com.blipblipcode.distribuidoraayl.data.dto.of.dte33


import com.google.gson.annotations.SerializedName

data class TotalsDto(
    @SerializedName("IVA")
    val iVA: Int,
    @SerializedName("MntNeto")
    val mntNeto: Int,
    @SerializedName("MntTotal")
    val mntTotal: Int,
    @SerializedName("MontoPeriodo")
    val montoPeriodo: Int,
    @SerializedName("TasaIVA")
    val tasaIVA: String,
    @SerializedName("VlrPagar")
    val vlrPagar: Int
)