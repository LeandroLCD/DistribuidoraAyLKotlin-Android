package com.blipblipcode.distribuidoraayl.data.dto.of.taxpayer

import com.blipblipcode.distribuidoraayl.data.mapper.Mappable
import com.blipblipcode.distribuidoraayl.domain.models.customer.Activity
import com.google.gson.annotations.SerializedName


data class ActivitiesDto(
    @SerializedName("actividadEconomica")
    val name: String,
    @SerializedName("actividadPrincipal")
    val isMainActivity: Boolean,
    @SerializedName("codigoActividadEconomica")
    val code: Int,
    @SerializedName("giro")
    val turn: String
) : Mappable<Activity> {

    override fun mapToDomain(): Activity {
        return Activity(
            turn = turn,
            code = code,
            name = name,
            isMainActivity = isMainActivity
        )
    }
}