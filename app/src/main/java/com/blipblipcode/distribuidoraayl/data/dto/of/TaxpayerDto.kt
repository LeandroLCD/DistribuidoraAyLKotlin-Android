package com.blipblipcode.distribuidoraayl.data.dto.of


import com.blipblipcode.distribuidoraayl.data.dto.customer.ActivityDto
import com.blipblipcode.distribuidoraayl.data.dto.customer.BranchDto
import com.blipblipcode.distribuidoraayl.data.mapper.Mappable
import com.blipblipcode.distribuidoraayl.domain.models.of.Taxpayer
import com.google.gson.annotations.SerializedName

data class TaxpayerDto(
    @SerializedName("actividades")
    val activities: List<ActivityDto>,
    @SerializedName("comuna")
    val commune: String,
    @SerializedName("direccion")
    val address: String,
    @SerializedName("email")
    val email: String?,
    @SerializedName("razonSocial")
    val companyName: String,
    @SerializedName("rut")
    val rut: String,
    @SerializedName("sucursales")
    val branches: List<BranchDto>,
    @SerializedName("telefono")
    val phone: String
): Mappable<Taxpayer>{
    override fun mapToDomain(): Taxpayer {
        return Taxpayer(
            activities = activities.map { it.mapToDomain() },
            commune = commune,
            address = address,
            email = email,
            company = companyName,
            rut = rut,
            branches = branches.map { it.mapToDomain() },
            phone = phone
        )

    }
}