package com.blipblipcode.distribuidoraayl.data.dto.of.dte33


import com.google.gson.annotations.SerializedName

data class ReceiverDto(
    @SerializedName("CmnaRecep")
    val cmnaRecep: String,
    @SerializedName("DirRecep")
    val dirRecep: String,
    @SerializedName("GiroRecep")
    val giroRecep: String,
    @SerializedName("RUTRecep")
    val rUTRecep: String,
    @SerializedName("RznSocRecep")
    val rznSocRecep: String
)