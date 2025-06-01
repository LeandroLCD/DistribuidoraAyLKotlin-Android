package com.blipblipcode.distribuidoraayl.data.dto.of

import com.google.gson.annotations.SerializedName

data class EmissionResponseDto (
    @SerializedName("TOKEN")
    val token: String,
    @SerializedName("FOLIO")
    val number: Int,
    @SerializedName("RESOLUCION")
    val resolution: ResolutionDto,
    @SerializedName("TIMBRE")
    val timbre: String,
    @SerializedName("PDF")
    val pdf: String
)