package com.blipblipcode.distribuidoraayl.data.dto.of


import com.blipblipcode.distribuidoraayl.data.mapper.Mappable
import com.blipblipcode.distribuidoraayl.domain.models.preferences.CredentialOf
import com.google.gson.annotations.SerializedName

data class CredentialOfDto(
    @SerializedName("apikey")
    val apikey: String,
    @SerializedName("url")
    val url: String
): Mappable<CredentialOf> {
    override fun mapToDomain(): CredentialOf {
        return CredentialOf(
            apikey = apikey,
            url = url
        )
    }
}