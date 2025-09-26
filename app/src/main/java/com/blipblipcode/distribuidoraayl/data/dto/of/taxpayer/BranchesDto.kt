package com.blipblipcode.distribuidoraayl.data.dto.of.taxpayer

import com.blipblipcode.distribuidoraayl.data.mapper.Mappable
import com.blipblipcode.distribuidoraayl.domain.models.customer.Branch
import com.google.gson.annotations.SerializedName


data class BranchesDto(
    @SerializedName("cdgSIISucur")
    val code: String,
    @SerializedName("ciudad")
    val city: String,
    @SerializedName("comuna")
    val commune: String,
    @SerializedName("direccion")
    val address: String,
    @SerializedName("telefono")
    val phone: String?
): Mappable<Branch> {
    override fun mapToDomain(): Branch {
        return Branch(
            city = city,
            code = code.toInt(),
            commune = commune,
            address = address,
            phone = phone ?: "",
            isHouseMatrix = false,
            sapCode = null
        )
    }
}