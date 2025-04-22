package com.blipblipcode.distribuidoraayl.domain.models

data class ECommerce(
    val economicActivity: String,
    val apiKey: String, 
    val siiOfficeCode: Long,
    val communeOrigin: String,
    val issuerEmail: String,
    val addressOrigin: String,
    val distributorCode: Long,
    val siiEmission: Boolean,
    val businessLine: String,
    val iva: Double,
    val officeCode: String,
    val issuerRut: String,
    val businessName: String,
    val phone: Long,
    val regionalAddress: String
)