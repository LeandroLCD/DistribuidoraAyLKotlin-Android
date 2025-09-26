package com.blipblipcode.distribuidoraayl.data.dto.customer.rubros

import com.google.gson.annotations.SerializedName

data class RubrosDto(
    @SerializedName("rubros")
    val rubros: List<RubroDto>
)