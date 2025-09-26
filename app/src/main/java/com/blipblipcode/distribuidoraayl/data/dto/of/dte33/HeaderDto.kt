package com.blipblipcode.distribuidoraayl.data.dto.of.dte33


import com.google.gson.annotations.SerializedName

data class HeaderDto(
    @SerializedName("Emisor")
    val emisor: EmisorDto,
    @SerializedName("IdDoc")
    val idDoc: IdDocDto,
    @SerializedName("Receptor")
    val receptor: ReceiverDto,
    @SerializedName("Totales")
    val totales: TotalsDto
)