package com.blipblipcode.distribuidoraayl.data.dto.ecommerce

import com.blipblipcode.distribuidoraayl.data.mapper.Mappable
import com.blipblipcode.distribuidoraayl.domain.models.preferences.ECommerce
import com.google.gson.annotations.SerializedName

data class ECommerceDto(
    @SerializedName("act_eco") val economicActivity: String,
    @SerializedName("cdg_SII_Sucur") val siiOfficeCode: Long,
    @SerializedName("cmna_origen") val communeOrigin: String,
    @SerializedName("correo_emisor") val issuerEmail: String,
    @SerializedName("dir_origen") val addressOrigin: String,
    @SerializedName("cdg_distribuidor") val distributorCode: Long,
    @SerializedName("EmisionSII") val siiEmission: Boolean,
    @SerializedName("giro_emis") val businessLine: String,
    @SerializedName("iva") val iva: Double,
    @SerializedName("office_code") val officeCode: String,
    @SerializedName("RUT_emisor") val rut: String,
    @SerializedName("rzn_soc") val companyName: String,
    @SerializedName("telefono") val phone: String,
    @SerializedName("direccion_regional") val regionalAddress: String
) : Mappable<ECommerce> {
    override fun mapToDomain() = ECommerce(
        economicActivity = economicActivity,
        siiOfficeCode = siiOfficeCode,
        communeOrigin = communeOrigin,
        issuerEmail = issuerEmail,
        addressOrigin = addressOrigin,
        distributorCode = distributorCode,
        siiEmission = siiEmission,
        businessLine = businessLine,
        iva = iva,
        officeCode = officeCode,
        rut = rut,
        companyName = companyName,
        phone = phone,
        regionalAddress = regionalAddress
    )
}