package com.blipblipcode.distribuidoraayl.data.dto.of.dte33

import com.google.gson.annotations.SerializedName

data class ResponseDto(
    @SerializedName("folio")
    val folio: Int,
    @SerializedName("response")
    val response: List<String>

)