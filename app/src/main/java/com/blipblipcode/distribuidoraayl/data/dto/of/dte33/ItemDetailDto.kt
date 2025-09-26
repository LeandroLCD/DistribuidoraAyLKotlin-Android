package com.blipblipcode.distribuidoraayl.data.dto.of.dte33


import com.google.gson.annotations.SerializedName

data class ItemDetailDto(
    @SerializedName("CdgItem")
    val cdgItem: List<CodeDto>?,
    @SerializedName("MontoItem")
    val montoItem: Int,
    @SerializedName("NmbItem")
    val nmbItem: String,
    @SerializedName("NroLinDet")
    val nroLinDet: Int,
    @SerializedName("PrcItem")
    val prcItem: Int,
    @SerializedName("QtyItem")
    val qtyItem: Int
)