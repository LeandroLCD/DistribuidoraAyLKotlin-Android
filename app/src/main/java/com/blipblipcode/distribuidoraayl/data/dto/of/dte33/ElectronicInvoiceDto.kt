package com.blipblipcode.distribuidoraayl.data.dto.of.dte33


import com.google.gson.annotations.SerializedName

data class ElectronicInvoiceDto(
    @SerializedName("dte")
    val dte: DteDto,
    @SerializedName("response")
    val response: List<String>
)