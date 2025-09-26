package com.blipblipcode.distribuidoraayl.data.dto.customer.regions

import com.google.gson.annotations.SerializedName

data class RegionsDto(
    @SerializedName("Regions")
    val regions: List<RegionDto>
)
