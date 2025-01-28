package com.blipblipcode.distribuidoraayl.core.network

import com.blipblipcode.distribuidoraayl.data.dto.of.taxpayer.TaxpayerDto
import retrofit2.http.GET
import retrofit2.http.Path

interface IOpenFacturaApi {
    @GET("taxpayer/{rut}")
    suspend fun getTaxpayer(@Path("rut") rut: String): TaxpayerDto
}