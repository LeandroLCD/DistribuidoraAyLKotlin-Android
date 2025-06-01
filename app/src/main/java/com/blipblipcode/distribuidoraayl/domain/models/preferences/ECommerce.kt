package com.blipblipcode.distribuidoraayl.domain.models.preferences

data class ECommerce(
    val economicActivity: String,
    val siiOfficeCode: Long,
    val communeOrigin: String,
    val issuerEmail: String,
    val addressOrigin: String,
    val distributorCode: Long,
    val siiEmission: Boolean,
    val businessLine: String,
    val iva: Double,
    val officeCode: String,
    val rut: String,
    val companyName: String,
    val phone: String,
    val regionalAddress: String
)