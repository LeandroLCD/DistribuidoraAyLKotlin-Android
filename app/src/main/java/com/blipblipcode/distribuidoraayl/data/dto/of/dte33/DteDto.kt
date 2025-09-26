package com.blipblipcode.distribuidoraayl.data.dto.of.dte33


import com.google.gson.annotations.SerializedName

data class DteDto(
    @SerializedName("Detalle")
    val detail: List<ItemDetailDto>,
    @SerializedName("Encabezado")
    val header: HeaderDto
)